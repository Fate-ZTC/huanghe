package com.parkbobo.utils.httpmodel;

import java.io.Serializable;

/**
 * @version 1.1
 * @author RY
 * @since 2017-7-11 14:30:26
 * 增加楼层字段
 *
 */

public class PassCarPort implements Serializable {

	/**
	 * 车位数据
	 * 
	 */
	private static final long serialVersionUID = -2949099304327413824L;
	private String parkId;
	private String spotId;
	private Integer direct;
	private String passTime;
	private String carPlate;
	private Integer plateColor;
	private Integer plateType;
	private Integer carType;
	private Integer parkType;
	private String imageUrl;
	private String flourCode;//楼层编码
	public String getParkId() {
		return parkId;
	}
	public void setParkId(String parkId) {
		this.parkId = parkId;
	}
	public String getSpotId() {
		return spotId;
	}
	public void setSpotId(String spotId) {
		this.spotId = spotId;
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
	public Integer getPlateColor() {
		return plateColor;
	}
	public void setPlateColor(Integer plateColor) {
		this.plateColor = plateColor;
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
	public String getFlourCode() {
		return flourCode;
	}
	public void setFlourCode(String flourCode) {
		this.flourCode = flourCode;
	}
	
}
