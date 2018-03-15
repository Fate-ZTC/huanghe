package com.parkbobo.utils.httpmodel;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QueryVehicleMonthly implements Serializable{

	/**
	 * 是否为包期车
	 */
	private static final long serialVersionUID = -8795562441301694444L;
	private Integer isBagVehicle;
	private String endTime;
	private Integer ruleId;
	private String ruleName;
	private Integer ruleType;
	private Integer payPee;
	private Long carparkid;
	private String carparkName;
	private String carPlate;
	private Integer kid;
	public Integer getIsBagVehicle() {
		return isBagVehicle;
	}
	public void setIsBagVehicle(Integer isBagVehicle) {
		this.isBagVehicle = isBagVehicle;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public Integer getRuleType() {
		return ruleType;
	}
	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}
	public Integer getPayPee() {
		return payPee;
	}
	public void setPayPee(Integer payPee) {
		this.payPee = payPee;
	}
	public Long getCarparkid() {
		return carparkid;
	}
	public void setCarparkid(Long carparkid) {
		this.carparkid = carparkid;
	}
	public String getCarparkName() {
		return carparkName;
	}
	public void setCarparkName(String carparkName) {
		this.carparkName = carparkName;
	}
	public Integer getCardStatus() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(this.getEndTime()!=null && !"".equals(this.getEndTime())){
			Date endTime = sdf.parse(this.getEndTime());
			if(endTime.getTime()>=new Date().getTime()){
				return 1;
			}
		}
		return 0;
	}
	public String getCarPlate() {
		return carPlate;
	}
	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}
	public Integer getKid() {
		return kid;
	}
	public void setKid(Integer kid) {
		this.kid = kid;
	}
	
}
