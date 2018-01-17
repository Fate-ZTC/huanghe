package com.parkbobo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * 月租办理
 * */
@Entity
@Table(name="lq_handle_monthly_vehicle")
public class HandleMonthlyVehicle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1703120600852932571L;
	private String mid;
	private Carpark carpark;
	private String carPlate;
	private Integer plateColor;
	private Integer ruleId;
	private String ruleName;
	private Date startTime;
	private Date endTime;
	private String vehicleHost;
	private Integer vehicleType;
	private Integer payFee;
	private Integer payType;
	private Date handleTime;
	private Integer dataSource;
	private Integer status;
	private Integer pushStatus;
	private Integer isRenew;
	private Integer ruleType;
	private Users users;
	private String openid;
	@Id 
	@Column(name="mid", unique=true, nullable=false)
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="carparkid")
	public Carpark getCarpark() {
		return carpark;
	}
	public void setCarpark(Carpark carpark) {
		this.carpark = carpark;
	}
	@Column(name="car_plate")
	public String getCarPlate() {
		return carPlate;
	}
	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}
	@Column(name="plate_color")
	public Integer getPlateColor() {
		return plateColor;
	}
	public void setPlateColor(Integer plateColor) {
		this.plateColor = plateColor;
	}
	@Column(name="rule_id")
	public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	@Column(name="rule_name")
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	@Column(name="start_time")
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@Column(name="end_time")
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Column(name="vehicle_host")
	public String getVehicleHost() {
		return vehicleHost;
	}
	public void setVehicleHost(String vehicleHost) {
		this.vehicleHost = vehicleHost;
	}
	@Column(name="vehicle_type")
	public Integer getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(Integer vehicleType) {
		this.vehicleType = vehicleType;
	}
	@Column(name="pay_fee")
	public Integer getPayFee() {
		return payFee;
	}
	public void setPayFee(Integer payFee) {
		this.payFee = payFee;
	}
	@Column(name="pay_type")
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	@Column(name="handle_time")
	public Date getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	@Column(name="data_source")
	public Integer getDataSource() {
		return dataSource;
	}
	public void setDataSource(Integer dataSource) {
		this.dataSource = dataSource;
	}
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name="push_status")
	public Integer getPushStatus() {
		return pushStatus;
	}
	public void setPushStatus(Integer pushStatus) {
		this.pushStatus = pushStatus;
	}
	@Column(name="is_renew")
	public Integer getIsRenew() {
		return isRenew;
	}
	public void setIsRenew(Integer isRenew) {
		this.isRenew = isRenew;
	}
	@Column(name="rule_type")
	public Integer getRuleType() {
		return ruleType;
	}
	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}
	@Column(name="openid")
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="mobile")
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}


}
