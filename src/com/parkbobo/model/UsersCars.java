package com.parkbobo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户车辆信息实体
 * @author RY
 *
 */

@Entity
@Table(name = "lq_users_cars")
@SequenceGenerator(name="generator", sequenceName="lq_users_cars_kid_seq", allocationSize = 1)
public class UsersCars implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2472702343146090738L;
	/**
	 * 主键ID
	 */
	private Integer kid;
	/**
	 * 车牌号码
	 */
	private String carPlate;
	/**
	 * 所属用户手机号码
	 */
	private String mobile;
	/**
	 * 绑定时间
	 */
	private Date posttime;
	/**
	 * 车牌颜色
	 * */
	private Integer plateColor;
	/**
	 * 车辆颜色
	 * */
	private Integer carColor;
	/**
	 * 车辆类型
	 * */
	private Integer vehicleType;
	/**
	 * 车主姓名
	 * */
	private String vehicleHost;
	/**
	 * 联系电话
	 * */
	private String contactNum;
	/**
	 * 使用人
	 * */
	private String userHost;
	/**
	 * 认证状态
	 * */
	private Integer authStatus;
	/**
	 * 认证不通过原因
	 * */
	private String authReason;
	/**
	 * 行驶证图片
	 * */
	private String drivingUrl;

	@Id
	@Column(name="kid", unique = true, nullable = false)
	@GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
	public Integer getKid() {
		return kid;
	}

	public void setKid(Integer kid) {
		this.kid = kid;
	}

	@Column(name = "car_plate")
	public String getCarPlate() {
		return carPlate;
	}

	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}
	
	@Column(name = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "posttime")
	public Date getPosttime() {
		return posttime;
	}

	public void setPosttime(Date posttime) {
		this.posttime = posttime;
	}
	@Column(name="plate_color")
	public Integer getPlateColor() {
		return plateColor;
	}

	public void setPlateColor(Integer plateColor) {
		this.plateColor = plateColor;
	}
	@Column(name="car_color")
	public Integer getCarColor() {
		return carColor;
	}

	public void setCarColor(Integer carColor) {
		this.carColor = carColor;
	}
	@Column(name="vehicle_type")
	public Integer getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(Integer vehicleType) {
		this.vehicleType = vehicleType;
	}
	@Column(name="vehicle_host")
	public String getVehicleHost() {
		return vehicleHost;
	}

	public void setVehicleHost(String vehicleHost) {
		this.vehicleHost = vehicleHost;
	}
	@Column(name="contact_num")
	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}
	@Column(name="user_host")
	public String getUserHost() {
		return userHost;
	}

	public void setUserHost(String userHost) {
		this.userHost = userHost;
	}
	@Column(name="auth_status")
	public Integer getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(Integer authStatus) {
		this.authStatus = authStatus;
	}
	@Column(name="auth_reason")
	public String getAuthReason() {
		return authReason;
	}

	public void setAuthReason(String authReason) {
		this.authReason = authReason;
	}
	@Column(name="driving_url")
	public String getDrivingUrl() {
		return drivingUrl;
	}

	public void setDrivingUrl(String drivingUrl) {
		this.drivingUrl = drivingUrl;
	}

}
