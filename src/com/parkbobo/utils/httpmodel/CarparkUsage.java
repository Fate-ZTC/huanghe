package com.parkbobo.utils.httpmodel;

import java.io.Serializable;

public class CarparkUsage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5426335138504088997L;
	private String parkingCode;
	private String parkingName;
	private Integer inCars;
	private Integer outCars;
	private Integer allCars;
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
	public Integer getInCars() {
		return inCars;
	}
	public void setInCars(Integer inCars) {
		this.inCars = inCars;
	}
	public Integer getOutCars() {
		return outCars;
	}
	public void setOutCars(Integer outCars) {
		this.outCars = outCars;
	}
	public Integer getAllCars() {
		return allCars;
	}
	public void setAllCars(Integer allCars) {
		this.allCars = allCars;
	}
	public float getUsage() {
		return usage;
	}
	public void setUsage(float usage) {
		this.usage = usage;
	}
	
	

}
