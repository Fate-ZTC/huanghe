package com.parkbobo.utils.httpmodel;

/**
 * 账单支付请求对象
 * @author RY
 * @version 1.0
 * @since 2017-6-27 17:12:53
 *
 */

public class BillInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3732000967193475986L;
	private String parkId;//停车点编号
	private String carPlate;//车牌号码
	private Integer plateColor;//车牌颜色
	private String inUnid;//入场ID

	public String getParkId() {
		return parkId;
	}

	public void setParkId(String parkId) {
		this.parkId = parkId;
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

	public String getInUnid() {
		return inUnid;
	}

	public void setInUnid(String inUnid) {
		this.inUnid = inUnid;
	}

}
