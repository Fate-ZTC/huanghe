package com.parkbobo.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import javax.persistence.Transient;

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
	 * 当前连续异常次数统计
	 */
	private Integer abnormalCount;
	/**
	 * 异常类型
	 */
	private PatrolException patrolException;
	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;
	/**
	 * 巡更时长
	 */
	private String checkDuration;
	private String formatStartTime;
	private String formatEndTime;
	private String regionName;
	
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
	@Column(name="last_update_time")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="exception_type")
	public PatrolException getPatrolException() {
		return patrolException;
	}
	public void setPatrolException(PatrolException patrolException) {
		this.patrolException = patrolException;
	}
	@Column(name="abnormal_count")
	public Integer getAbnormalCount() {
		return abnormalCount;
	}
	public void setAbnormalCount(Integer abnormalCount) {
		this.abnormalCount = abnormalCount;
	}
	@Transient
	public String formatStartTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(startTime != null)
			return sdf.format(startTime);
		return "";
	}
	@Transient
	public String formatEndTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(endTime != null)
			return sdf.format(endTime);
		return "";
	}
	@Transient
	public String getFormatStartTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(startTime != null)
			return sdf.format(startTime);
		return "";
	}
	@Transient
	public String getFormatEndTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(endTime != null)
			return sdf.format(endTime);
		return "";
	}
	@Transient
	public String getCheckDuration(){
		String dm = null;
		if(endTime != null && startTime != null) {
			long second = 0l;
			second = (endTime.getTime()-startTime.getTime())/1000;
			long hours = second / 3600;            //转换小时
	        second = second % 3600;                //剩余秒数
	        long minutes = second /60;            //转换分钟
	        second = second % 60;                //剩余秒数
			dm=hours+"小时"+minutes+"分钟"+second+"秒";
		}
		return dm;
	}
	@Transient
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
}
