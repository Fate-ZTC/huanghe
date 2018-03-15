package com.parkbobo.utils.httpmodel;

import java.io.Serializable;

public class CarparkIncome implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -315962942397627944L;
	
	private String parkingName;
	private String  parkingCode;
	private Integer allPay;
	private Integer tempPay;
	private Integer monthlyPay;
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
	public Integer getAllPay() {
		return allPay;
	}
	public void setAllPay(Integer allPay) {
		this.allPay = allPay;
	}
	public Integer getTempPay() {
		return tempPay;
	}
	public void setTempPay(Integer tempPay) {
		this.tempPay = tempPay;
	}
	public Integer getMonthlyPay() {
		return monthlyPay;
	}
	public void setMonthlyPay(Integer monthlyPay) {
		this.monthlyPay = monthlyPay;
	}

}
