package com.parkbobo.utils.httpmodel;

import java.io.Serializable;

public class Carpark implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5090853192776444726L;
	private String parkingName;
	private String  parkingCode;
	public String getParkingName() {
		return parkingName;
	}
	public void setParkingName(String parkingName) {
		this.parkingName = parkingName;
	}
	public String getParkingCode() {
		return parkingCode;
	}
	public void setParkingCode(String parkingCode) {
		this.parkingCode = parkingCode;
	}
	
}
