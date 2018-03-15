package com.parkbobo.utils.httpmodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 停车收费对象
 * @author RY
 * @version 1.0
 * @since 2017-6-27 09:44:46
 *
 */

public class VehicleOrder implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6742290651230624060L;
	private Integer errorCode;//返回码
	private String message;//信息提示
	private String terminCode;//终端编号
	private String parkingcode;//停车点编号
	private String payUnid;//账单唯一ID
	private String plateInfo;//车牌号码
	private String inUnid;//入场唯一ID
	private String inTime;//入场时间
	private String lastPayTime;//上次结算时间
	private String planPayTime;//此次结算时间
	private Integer shouldPayMoney;//需缴金额

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTerminCode() {
		return terminCode;
	}

	public void setTerminCode(String terminCode) {
		this.terminCode = terminCode;
	}

	public String getParkingcode() {
		return parkingcode;
	}

	public void setParkingcode(String parkingcode) {
		this.parkingcode = parkingcode;
	}

	public String getPayUnid() {
		return payUnid;
	}

	public void setPayUnid(String payUnid) {
		this.payUnid = payUnid;
	}

	public String getPlateInfo() {
		return plateInfo;
	}

	public void setPlateInfo(String plateInfo) {
		this.plateInfo = plateInfo;
	}

	public String getInUnid() {
		return inUnid;
	}

	public void setInUnid(String inUnid) {
		this.inUnid = inUnid;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getLastPayTime() {
		return lastPayTime;
	}

	public void setLastPayTime(String lastPayTime) {
		this.lastPayTime = lastPayTime;
	}

	public String getPlanPayTime() {
		return planPayTime;
	}

	public void setPlanPayTime(String planPayTime) {
		this.planPayTime = planPayTime;
	}

	public Integer getShouldPayMoney() {
		return shouldPayMoney;
	}

	public void setShouldPayMoney(Integer shouldPayMoney) {
		this.shouldPayMoney = shouldPayMoney;
	}
	
	public Date formatInTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(inTime!=null){
				return sdf.parse(this.getInTime());				
			}
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Date formatLastPayTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(lastPayTime!=null && !"".equals(lastPayTime)){
				return sdf.parse(this.getLastPayTime());				
			}
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Date formatPlanPayTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(planPayTime!=null){
				return sdf.parse(this.getPlanPayTime());				
			}
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
