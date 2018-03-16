package com.parkbobo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	 * 工号
	 */
	private String jobNum;
	/**
	 * 用户姓名
	 */
	private String username;
	/**
	 * 纬度
	 */
	private Double lat;
	/**
	 * 经度
	 */
	private Double lon;
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
	private PatrolException patrolException;
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
	@Column(name="job_num")
	public String getJobNum() {
		return jobNum;
	}
	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}
	@Column(name="username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="lat")
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	@Column(name="lon")
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
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
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="exception_type")
	public PatrolException getPatrolException() {
		return patrolException;
	}
	public void setPatrolException(PatrolException patrolException) {
		this.patrolException = patrolException;
	}
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}




}
