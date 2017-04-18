package com.techmahindra.ubi.vo;

import java.io.Serializable;

public class UBIData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String vin;
	private float driverScore;
	private int driverRank;
	private float premiumDisc;
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
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
	private String infoDateTime;
	
}
