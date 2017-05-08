package com.techmahindra.ubi.vo;

import java.io.Serializable;

public class UBIData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String DUMMY = "DUMMY";
	private String vin = DUMMY;
	private String city = DUMMY;
	private String model = DUMMY;
	private float driverScore = 0;
	private int driverRank = 0;
	private float premiumDisc = 0;
	private String infoDateTime;
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public float getDriverScore() {
		return driverScore;
	}
	public void setDriverScore(float driverScore) {
		this.driverScore = driverScore;
	}
	public int getDriverRank() {
		return driverRank;
	}
	public void setDriverRank(int driverRank) {
		this.driverRank = driverRank;
	}
	public float getPremiumDisc() {
		return premiumDisc;
	}
	public void setPremiumDisc(float premiumDisc) {
		this.premiumDisc = premiumDisc;
	}
	public String getInfoDateTime() {
		return infoDateTime;
	}
	public void setInfoDateTime(String infoDateTime) {
		this.infoDateTime = infoDateTime;
	}
	
	
}
