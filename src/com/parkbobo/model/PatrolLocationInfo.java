package com.parkbobo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="patrol_location_info")
@SequenceGenerator(name="generator", sequenceName="patrol_location_info_id_seq", allocationSize = 1)
public class PatrolLocationInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3617407055441658923L;
	/**
	 * 巡查轨迹信息id
	 */
	private Integer id;
	/**
	 * 区域id
	 */
	private Integer usregId;
	/**
	 * 用户id
	 */
	private Integer userId;
	/**
	 * 用户姓名
	 */
	private String username;
	/**
	 * 纬度
	 */
	private double lat;
	/**
	 * 经度
	 */
	private double lon;
	/**
	 * 学校id
	 */
	private Integer campusNum;
	/**
	 * 时间戳
	 */
	private Date timestamp;
	/**
	 * 异常类型
	 */
	private Integer exceptionType;
	/**
	 * 是否异常   1正常  2异常
	 */
	private Integer status;
	
	@Id
	@Column(name="id",unique=true,nullable=false)
	@GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="usreg_id")
	public Integer getUsregId() {
		return usregId;
	}
	public void setUsregId(Integer usregId) {
		this.usregId = usregId;
	}
	@Column(name="user_id")
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	@Column(name="username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="lat")
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	@Column(name="lon")
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	@Column(name="campus_num")
	public Integer getCampusNum() {
		return campusNum;
	}
	public void setCampusNum(Integer campusNum) {
		this.campusNum = campusNum;
	}
	@Column(name="timestamp")
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	@Column(name="exception_type")
	public Integer getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(Integer exceptionType) {
		this.exceptionType = exceptionType;
	}
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
	
}
