package com.techmahindra.ubi.service;

import java.io.IOException;
import java.io.Serializable;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import com.google.gson.Gson;
import com.techmahindra.ubi.utils.UBIEventProducer;
import com.techmahindra.ubi.utils.UBIUtils;
import com.techmahindra.ubi.vo.UBIData;

public class UBIAnalyzerService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String OUT_TOPIC = "ubidata";
	
	public void process(Dataset<Row> tempData) throws IOException {
		if(tempData != null && tempData.count() > 0) {
			tempData.foreach(new ForeachFunction<Row>() {
				private static final long serialVersionUID = 1L;
				@Override
				public void call(Row row) throws Exception {
					String vin = row.getString(row.fieldIndex("vin"));
					String date = row.getString(row.fieldIndex("date"));
					float totalTrips = Float.valueOf(row.getLong(row.fieldIndex("trips")));
					float tripsHavingOS = Float.valueOf(row.getLong(row.fieldIndex("OSCount")));
					float tripsHavingHB = Float.valueOf(row.getLong(row.fieldIndex("HBCount")));
					float tripsHavingHC = Float.valueOf(row.getLong(row.fieldIndex("HCCount")));
					float tripsHavingHA = Float.valueOf(row.getLong(row.fieldIndex("HACount")));
					float noOfTripsNight = Float.valueOf(row.getLong(row.fieldIndex("NightTripCount")));
					float noOfTripsWeekend = Float.valueOf(row.getLong(row.fieldIndex("WekendTripCount")));
					if((tripsHavingHB+tripsHavingHA+tripsHavingHC+tripsHavingOS) > totalTrips)
					{
						System.out.println("There is a data issue at database, please re-check total trips and reletive counts of vin :"+vin
								+". on ("+date+").Ignoring the record !!! ");
					}else{
						System.out.println("Driver VIN: " + vin);
						float driverScore=getDriverScore(totalTrips, tripsHavingHB, tripsHavingHC, tripsHavingHA, tripsHavingOS);
						System.out.println("Driver Score: "+driverScore);
						int driverRank=getDriverRank(driverScore);
						System.out.println("Driver Rank: "+driverRank);
						float discount=getDiscount(totalTrips, noOfTripsNight, noOfTripsWeekend, driverScore);
						System.out.println("UBI Discount: "+discount);
						System.out.println("--------------------------");
						UBIData data = new UBIData();
						data.setVin(vin);
						data.setInfoDateTime(date);
						data.setDriverScore(driverScore);
						data.setDriverRank(driverRank);
						data.setPremiumDisc(discount);
						Gson gson = new Gson();
			        	String msg = gson.toJson(data);
			        	UBIEventProducer cep = new UBIEventProducer();
			    		cep.sendEvent(OUT_TOPIC, msg);
					}
				}
			});
		} else 
			System.out.println("No UBI data to process!");
	}
	
	private int getDriverRank(float driverScoreInPercentage) {
		if(driverScoreInPercentage>UBIUtils.getSCORE_PERC_GT80()){
			return (UBIUtils.getSCORE_5());
		}else if(driverScoreInPercentage>UBIUtils.getSCORE_PERC_GT60()){
			return (UBIUtils.getSCORE_4());
		}else if(driverScoreInPercentage>UBIUtils.getSCORE_PERC_GT40()){
			return (UBIUtils.getSCORE_3());
		}else if(driverScoreInPercentage>UBIUtils.getSCORE_PERC_GT20()){
			return (UBIUtils.getSCORE_2());
		}else
			return (UBIUtils.getSCORE_1());
	}

	private float getDriverScore(float totalTrips,float tripsHavingHB,float tripsHavingHC,float tripsHavingHA,float tripsHavingOS) {
		float confOSPerc=UBIUtils.getOS_PERC()/100f;//read it from property
		float confHAPerc=UBIUtils.getHA_PERC()/100f;
		float confHCPerc=UBIUtils.getHC_PERC()/100f;
		float confHBPerc=UBIUtils.getHB_PERC()/100f;
		float derivedOSPerc=((totalTrips-tripsHavingOS)/totalTrips)*100f;
		float derivedHAPerc=((totalTrips-tripsHavingHA)/totalTrips)*100f;
		float derivedHCPerc=((totalTrips-tripsHavingHC)/totalTrips)*100f;
		float derivedHBPerc=((totalTrips-tripsHavingHB)/totalTrips)*100f;
		return (confOSPerc*derivedOSPerc+confHAPerc*derivedHAPerc+confHBPerc*derivedHBPerc+confHCPerc*derivedHCPerc);
	}

	private float getDiscount(float totalTrips,float noOfTripsNight,float noOfTripsWeekend, float driverScorePercentage) {
		float maxDiscount=UBIUtils.getMAX_DISC()/100f;//get it from property
		float maxNDB=UBIUtils.getMAX_NDB()/100f;
		float maxDB=UBIUtils.getMAX_DB()/100f;
		float maxTOD=UBIUtils.getMAX_TOD()/100f;
		float maxDOW=UBIUtils.getMAX_DOW()/100f;
		float derivedNoOfTripsNight=((totalTrips-noOfTripsNight)/totalTrips)*100f;
		float derivedNoOfTripsWeekend=((totalTrips-noOfTripsWeekend)/totalTrips)*100f;
		float discount=(maxDiscount*(maxNDB*((maxTOD*derivedNoOfTripsNight)+(maxDOW*derivedNoOfTripsWeekend))+(maxDB*driverScorePercentage)));
		return discount;
	}
	
}