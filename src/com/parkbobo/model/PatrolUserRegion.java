package com.parkbobo.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

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
	/**
	 * 离开巡更区域开始时间
     */
	private Date leaveRegionStartTime;
	/**
	 * 人员位置不变化开始时间
     */
	private Date locationNotChangeStartTime;
	/**
	 * 异常推动开始时间
     */
	private Date exceptionPushTime;
	/**
	 * 是否到达
     */
	private boolean isArrive;
	/**
	 * 区域id
     */
	private int campusNum;

/*	*//**
	 * 异常数据
	 *//*
	private PatrolExceptionInfo patrolExceptionInfo;*/

	public PatrolUserRegion() {}

	public PatrolUserRegion(String username, String jobNum) {
		this.username = username;
		this.jobNum = jobNum;
	}

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

	@Column(name = "leave_region_start_time")
	public Date getLeaveRegionStartTime() {
		return leaveRegionStartTime;
	}

	public void setLeaveRegionStartTime(Date leaveRegionStartTime) {
		this.leaveRegionStartTime = leaveRegionStartTime;
	}

	@Column(name = "not_change_start_time")
	public Date getLocationNotChangeStartTime() {
		return locationNotChangeStartTime;
	}

	public void setLocationNotChangeStartTime(Date locationNotChangeStartTime) {
		this.locationNotChangeStartTime = locationNotChangeStartTime;
	}
	@Column(name = "exception_push_time")
	public Date getExceptionPushTime() {
		return exceptionPushTime;
	}

	public void setExceptionPushTime(Date exceptionPushTime) {
		this.exceptionPushTime = exceptionPushTime;
	}
	@Column(name = "is_arrive")
	public boolean isArrive() {
		return isArrive;
	}

	public void setArrive(boolean arrive) {
		isArrive = arrive;
	}

	@Column(name = "campus_num")
	public int getCampusNum() {
		return campusNum;
	}

	public void setCampusNum(int campusNum) {
		this.campusNum = campusNum;
	}
}
