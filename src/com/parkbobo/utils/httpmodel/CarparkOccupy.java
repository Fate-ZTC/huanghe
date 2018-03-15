package com.parkbobo.utils.httpmodel;

import java.io.Serializable;

public class CarparkOccupy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5328817716517283718L;
	private String parkingCode;
	private String parkingName;
	private float usage;
	public String getParkingCode() {
		return parkingCode;
	}
	public void setParkingCode(String parkingCode) {
		this.parkingCode = parkingCode;
	}
	public String getParkingName() {
		return parkingName;
	}
	public void setParkingName(String parkingName) {
		this.parkingName = parkingName;
	}
	public float getUsage() {
		return usage;
	}
	public void setUsage(float usage) {
		this.usage = usage;
	}
	
}
