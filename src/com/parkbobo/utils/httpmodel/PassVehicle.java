package com.parkbobo.utils.httpmodel;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PassVehicle implements Serializable{

	/**
	 * 过车数据
	 */
	private static final long serialVersionUID = -8114490501971163007L;
	private String unid;
	private String parkId;
	private Integer gateId;
	private Integer plateColor;
	private Integer direct;
	private String passTime;
	private String carPlate;
	private Integer plateType;
	private Integer carType;
	private String cardNo;
	private Integer parkType;
	private String imageUrl;
	public String getUnid() {
		return unid;
	}
	public void setUnid(String unid) {
		this.unid = unid;
	}
	public String getParkId() {
		return parkId;
	}
	public void setParkId(String parkId) {
		this.parkId = parkId;
	}
	public Integer getGateId() {
		return gateId;
	}
	public void setGateId(Integer gateId) {
		this.gateId = gateId;
	}
	public Integer getPlateColor() {
		return plateColor;
	}
	public void setPlateColor(Integer plateColor) {
		this.plateColor = plateColor;
	}
	public Integer getDirect() {
		return direct;
	}
	public void setDirect(Integer direct) {
		this.direct = direct;
	}
	public String getPassTime() {
		return passTime;
	}
	public void setPassTime(String passTime) {
		this.passTime = passTime;
	}
	public String getCarPlate() {
		return carPlate;
	}
	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}
	public Integer getPlateType() {
		return plateType;
	}
	public void setPlateType(Integer plateType) {
		this.plateType = plateType;
	}
	public Integer getCarType() {
		return carType;
	}
	public void setCarType(Integer carType) {
		this.carType = carType;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Integer getParkType() {
		return parkType;
	}
	public void setParkType(Integer parkType) {
		this.parkType = parkType;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Date formatPassTime(){
		SimpleDateFormat sdf = new SimpleDateFormat();
		try {
			if(passTime!=null){
				return sdf.parse(this.getPassTime());				
			}
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
