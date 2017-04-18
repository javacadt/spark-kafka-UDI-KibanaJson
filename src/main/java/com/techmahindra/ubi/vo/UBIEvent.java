package com.techmahindra.ubi.vo;

import java.io.Serializable;

public class UBIEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String DUMMY = "DUMMY";
	private String vin = DUMMY;
	private String model = DUMMY;
	private String timestamp = "";
	private float outsideTemp = 0;
	private float engineTemp = 0;
	private float speed = 0;
	private float fuel = 0;
	private float engineOil = 0;
	private float tirePressure = 0;
	private long odometer = 0;
	private String city = DUMMY;
	private int accPedalPos = 0;
	private int parkBrakeStatus = 0;
	private int headlampStatus = 0;
	private int brakePedalStatus = 0;
	private String transGearPosition = "";
	private int ignitionStatus = 0;
	private int windshieldWiperStatus = 0;
	private int abs = 0;
	private String tripId = "";
	private String incidentType = "";
	
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public float getOutsideTemp() {
		return outsideTemp;
	}
	public void setOutsideTemp(float outsideTemp) {
		this.outsideTemp = outsideTemp;
	}
	public float getEngineTemp() {
		return engineTemp;
	}
	public void setEngineTemp(float engineTemp) {
		this.engineTemp = engineTemp;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public float getFuel() {
		return fuel;
	}
	public void setFuel(float fuel) {
		this.fuel = fuel;
	}
	public float getEngineOil() {
		return engineOil;
	}
	public void setEngineOil(float engineOil) {
		this.engineOil = engineOil;
	}
	public float getTirePressure() {
		return tirePressure;
	}
	public void setTirePressure(float tirePressure) {
		this.tirePressure = tirePressure;
	}
	public long getOdometer() {
		return odometer;
	}
	public void setOdometer(long odometer) {
		this.odometer = odometer;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getAccPedalPos() {
		return accPedalPos;
	}
	public void setAccPedalPos(int accPedalPos) {
		this.accPedalPos = accPedalPos;
	}
	public int getParkBrakeStatus() {
		return parkBrakeStatus;
	}
	public void setParkBrakeStatus(int parkBrakeStatus) {
		this.parkBrakeStatus = parkBrakeStatus;
	}
	public int getHeadlampStatus() {
		return headlampStatus;
	}
	public void setHeadlampStatus(int headlampStatus) {
		this.headlampStatus = headlampStatus;
	}
	public int getBrakePedalStatus() {
		return brakePedalStatus;
	}
	public void setBrakePedalStatus(int brakePedalStatus) {
		this.brakePedalStatus = brakePedalStatus;
	}
	public String getTransGearPosition() {
		return transGearPosition;
	}
	public void setTransGearPosition(String transGearPosition) {
		this.transGearPosition = transGearPosition;
	}
	public int getIgnitionStatus() {
		return ignitionStatus;
	}
	public void setIgnitionStatus(int ignitionStatus) {
		this.ignitionStatus = ignitionStatus;
	}
	public int getWindshieldWiperStatus() {
		return windshieldWiperStatus;
	}
	public void setWindshieldWiperStatus(int windshieldWiperStatus) {
		this.windshieldWiperStatus = windshieldWiperStatus;
	}
	public int getAbs() {
		return abs;
	}
	public void setAbs(int abs) {
		this.abs = abs;
	}
	public String getTripId() {
		return tripId;
	}
	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
	public String getIncidentType() {
		return incidentType;
	}
	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}
	@Override
	public String toString() {
		return vin + ", " + model + ", " + city;
	}
}
