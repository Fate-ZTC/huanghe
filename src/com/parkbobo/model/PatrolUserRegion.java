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
@Table(name="patrol_user_region")
@SequenceGenerator(name="generator", sequenceName="patrol_user_region_id_seq", allocationSize = 1)
public class PatrolUserRegion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8656030003865974574L;
	
	/**
	 * 用户区域id
	 */
	private Integer id;
	/**
	 * 用户名称
	 */
	private String username;
	/**
	 * 区域id
	 */
	private Integer regionId;
	/**
	 * 工号
	 */
	private String jobNum;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 当前状态  1:正常  2:异常
	 */
	private Integer status;
	/**
	 * 异常类型
	 */
	private Integer exceptionType;
	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;
	
	@Id
	@Column(name="id",unique=true,nullable=false)
	@GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="region_id")
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	@Column(name="job_num")
	public String getJobNum() {
		return jobNum;
	}
	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
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
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name="exception_type")
	public Integer getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(Integer exceptionType) {
		this.exceptionType = exceptionType;
	}
	@Column(name="last_update_time")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	
	
}
