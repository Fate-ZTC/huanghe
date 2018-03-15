package com.parkbobo.utils.httpmodel;

import java.io.Serializable;

public class SendCarpark implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7075632162424023434L;
	
	private String parkingCode;
	private String parkingName;
	private String beginTime;
	private String endTime;
	private Integer queryType;
	public String getParkingCode() {
		return parkingCode;
	}
	public void setParkingCode(String parkingCode) {
		this.parkingCode = parkingCode;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getQueryType() {
		return queryType;
	}
	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}
	public String getParkingName() {
		return parkingName;
	}
	public void setParkingName(String parkingName) {
		this.parkingName = parkingName;
	}
	

}
