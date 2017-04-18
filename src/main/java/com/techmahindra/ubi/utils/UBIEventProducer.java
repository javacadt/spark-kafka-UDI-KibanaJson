package com.techmahindra.ubi.utils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.techmahindra.ubi.vo.UBIEvent;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class UBIEventProducer {
	
	private Producer<String, String> producer;
	private Logger logger = Logger.getLogger("CarEventProducer");
	
	public UBIEventProducer() throws IOException {
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader().getResourceAsStream("ubi.properties"));
		// Prepare kafka properties
		Properties kafkaProps = new Properties();
		kafkaProps.put("metadata.broker.list", props.getProperty("KAFKA_BROKER_LIST"));
        kafkaProps.put("serializer.class", "kafka.serializer.StringEncoder");
        kafkaProps.put("request.required.acks", "1");
        // Instantiate producer
        ProducerConfig config = new ProducerConfig(kafkaProps);
        producer = new Producer<>(config);
	}
	
	public static void main(String[] args) throws IOException {
		// Generate events using file
		UBIEventProducer cep = new UBIEventProducer();
		cep.eventsFromFile(args[0], args[1]);
	}
	
	public void sendEvent(String topic, String msg) {
		// Send event to topic
		KeyedMessage<String, String> data = new KeyedMessage<>(topic, msg);
		producer.send(data);
	}
	
	private void eventsFromFile(String topic, String file) throws IOException {
		// Read a file and generate events
		int msgCount = -1;
        try (Scanner scanner = new Scanner(new File(file))) {
	        logger.log(Level.INFO, "Sending messages...");
	        while(scanner.hasNextLine()) {
	        	msgCount++;
	        	String line = scanner.nextLine();
				if(msgCount > 0) {
		        	UBIEvent event = parse(line);
		        	Gson gson = new Gson();
		        	String msg = gson.toJson(event);
					sendEvent(topic, msg);
				}
				pause();
			}
        } catch(IOException e) {
        	logger.log(Level.SEVERE, "IOE", e);
        	throw e;
        }
        logger.log(Level.INFO, "Sent " + msgCount + " messages");
	}
	
	private void pause() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "IE", e);
			Thread.currentThread().interrupt();
		}
	}
	
	private UBIEvent parse(String record) {
		// Parse lines of file into car event
		UBIEvent event = new UBIEvent();
		String[] fields = record.split(",");
		event.setVin(fields[0]);
		event.setModel(fields[1]);
		event.setTimestamp(fields[2]);
		event.setOutsideTemp(Float.parseFloat(fields[3]));
		event.setEngineTemp(Float.parseFloat(fields[4]));
		event.setSpeed(Float.parseFloat(fields[5]));
		event.setFuel(Float.parseFloat(fields[6]));
		event.setEngineOil(Float.parseFloat(fields[7]));
		event.setTirePressure(Float.parseFloat(fields[8]));
		event.setOdometer(Long.parseLong(fields[9]));
		event.setCity(fields[10]);
		event.setAccPedalPos(Integer.parseInt(fields[11]));
		event.setParkBrakeStatus(Integer.parseInt(fields[12]));
		event.setHeadlampStatus(Integer.parseInt(fields[13]));
		event.setBrakePedalStatus(Integer.parseInt(fields[14]));
		event.setTransGearPosition(fields[15]);
		event.setIgnitionStatus(Integer.parseInt(fields[16]));
		event.setWindshieldWiperStatus(Integer.parseInt(fields[17]));
		event.setAbs(Integer.parseInt(fields[18]));
		event.setTripId(fields[19]);
		event.setIncidentType(fields[20]);
		return event;
	}
}