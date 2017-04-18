package com.techmahindra.ubi;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.google.gson.Gson;
import com.techmahindra.ubi.service.UBIAnalyzerService;
import com.techmahindra.ubi.vo.UBIEvent;

import scala.Tuple2;

public class UBIEventProcessor implements Serializable {

	private static final long serialVersionUID = 1L;

	public void run(String topic) throws IOException, AnalysisException {
		// Get properties
		Properties props = new Properties();
		props.load(UBIEventProcessor.class.getClassLoader().getResourceAsStream("ubi.properties"));
		
		// Initialize
		int numThreads = Integer.parseInt(props.getProperty("KAFKA_THREAD_COUNT"));
		int batchDuration = Integer.parseInt(props.getProperty("KAFKA_BATCH_DURATION"));
		String zkQuorum = props.getProperty("ZK_QUORUM");
		String kafkaGroup = props.getProperty("KAFKA_GROUP");
		
		// Instantiate streaming context & spark session
		SparkConf sparkConf = new SparkConf().setAppName("CarEventsProcessor");
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(batchDuration));
		jssc.checkpoint(".\\checkpoint");
		SparkSession spark = JavaSparkSessionSingleton.getInstance(sparkConf);
		Properties connProps = new Properties();
		connProps.put("user", props.getProperty("JDBC_USER"));
		connProps.put("password", props.getProperty("JDBC_PASSWORD"));
		connProps.put("driver", props.getProperty("JDBC_DRIVER"));
		
		// Prepare topic Map
		Map<String, Integer> topicMap = new HashMap<>();
		topicMap.put(topic, numThreads);
		
		// Get events history and register as temp view
		Dataset<Row> dbDF = spark.read().jdbc(props.getProperty("JDBC_CONN_STRING"),
											props.getProperty("JDBC_TABLE"), connProps);
//		dbDF.show(10);
		dbDF.createTempView("events_history");
		
		// Process streaming events
		JavaPairReceiverInputDStream<String, String> messages = KafkaUtils.createStream(jssc, zkQuorum, kafkaGroup, topicMap);
		JavaDStream<UBIEvent> events = messages.map(new Function<Tuple2<String, String>, UBIEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
		    public UBIEvent call(Tuple2<String, String> tuple2) {
				Gson gson = new Gson();
				return gson.fromJson(tuple2._2, UBIEvent.class);
	    	}
	    });
		
		events.foreachRDD(new VoidFunction<JavaRDD<UBIEvent>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void call(JavaRDD<UBIEvent> eventRDD) throws Exception {
				if(eventRDD.count() > 0) {
					Dataset<Row> streamDF = spark.createDataFrame(eventRDD, UBIEvent.class);
					if(streamDF != null && streamDF.count() > 0) {
//						streamDF.show(10);
						streamDF.createOrReplaceTempView("events_stream");
					}

					System.out.println("Applying trip analysis for UBI...");
					String sql = "SELECT history.vin as vin"
							+ ",date_format(history.timestamp,'hh:mm:ss') as timestamp"
							+ ",date_format(history.timestamp,'YYYY-MM-dd') as date"
							+ ",(CASE WHEN hour(history.timestamp)>= 18 THEN 'night' ELSE 'day' END) AS tripTime"
			                + ",(CASE WHEN (date_format(history.timestamp,'E')='Sat' OR date_format(history.timestamp,'E')='Sun') THEN 'weekend' ELSE 'weekday' END) AS tripDay"
		                    +",history.tripId as tripId"
		                    +",history.incidentType as incidentType"
		                    +" FROM events_history as history,events_stream as stream"
		                    +" WHERE history.vin=stream.vin"
		                    +" GROUP BY history.timestamp,history.vin,history.incidentType,history.tripId"
		                    + " ORDER BY history.timestamp";
					Dataset<Row> incidentData = spark.sql(sql);
//					incidentData.show(10);
					incidentData.createOrReplaceTempView("incident_data");
					
					Dataset<Row> tripsData = incidentData.groupBy("date","vin","tripId").count().orderBy(new Column("date"));
					tripsData.createOrReplaceTempView("trip_data");
//					tripsData.show(10);
					
					sql = "select"
							+ " incident.date as date"
							+ ",incident.vin as vin"
							+ ",trip.count as trips"
							 + ",count(CASE WHEN incident.incidentType='OverSpeed' THEN incident.tripId ELSE NULL END) AS OSCount"
			                 + ",count(CASE WHEN incident.incidentType='HardBraking' THEN incident.tripId ELSE NULL END) AS HBCount"
			                 + ",count(CASE WHEN incident.incidentType='HardCornering' THEN incident.tripId ELSE NULL END) AS HCCount"
			                 + ",count(CASE WHEN incident.incidentType='HardAcceleration' THEN incident.tripId ELSE NULL END) AS HACount"
			                 + ",count(CASE WHEN incident.tripTime='night' THEN incident.tripId ELSE NULL END) AS NightTripCount"
			                 + ",count(CASE WHEN incident.tripDay='weekend' THEN incident.tripId ELSE NULL END) AS WekendTripCount"
							+ " FROM incident_data as incident, trip_data as trip"
							+ " WHERE incident.date = trip.date"
							+ " AND incident.vin = trip.vin"
							+ " GROUP BY incident.date,incident.vin,trip.count"
							+ " ORDER BY incident.date";
					Dataset<Row> ubiData = spark.sql(sql);
//					ubiData.show(10);
					UBIAnalyzerService ubiService = new UBIAnalyzerService();
					ubiService.process(ubiData);
					
					System.out.println("Processed " + eventRDD.count() + " events.");
				}
				else	
					System.out.println("No events to process!!!");
			}
		});
		jssc.start();
		try {
			jssc.awaitTermination();
		} catch (InterruptedException e) {
			jssc.stop();
			Thread.currentThread().interrupt();
		}
	}
	
	public static void main(String[] args) throws IOException, AnalysisException {
		if (args.length < 1) {
	      System.exit(1);
	    }
		UBIEventProcessor cep = new UBIEventProcessor();
		cep.run(args[0]);
	}
}

/** Lazily instantiated singleton instance of SparkSession */
class JavaSparkSessionSingleton implements Serializable {
  private static final long serialVersionUID = 1L;
  private static transient SparkSession instance = null;
  private JavaSparkSessionSingleton(){}
  public static SparkSession getInstance(SparkConf sparkConf) {
    if (instance == null) {
      instance = SparkSession
        .builder()
        .config(sparkConf)
        .getOrCreate();
    }
    return instance;
  }
}