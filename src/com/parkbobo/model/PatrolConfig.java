package com.parkbobo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="patrol_config")
@SequenceGenerator(name="generator", sequenceName="patrol_config_id_seq", allocationSize = 1)
public class PatrolConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5553416368156133751L;
	
	/**
	 * 配置id
	 */
	private Integer id;
	/**
	 * 上传频率(秒)
	 */
	private Integer uploadTime;
	/**
	 * 异常报警距离 m
	 */
	private Integer leaveRegionDistance;
	/**
	 * 开始巡查后多少分钟到达指定区域
	 */
	private Integer startPatrolTime;
	/**
	 * 校区编号
	 */
	private Integer campusNum;
	/**
	 * 0:正常    1:紧急
	 * 是否突发紧急状态
	 */
	private Integer isEmergency;
	/**
	 * 刷新周期  单位秒
	 */
	private Integer refreshTime;
	/**
	 * 可以原地不动时间 分钟
	 */
	private Integer lazyTime;
	/**
	 * 可以离开范围时间 分钟
	 */
	private Integer leaveRegionTime;
	/**
	 * 人员丢失时长(分钟)
     */
	private Integer personnelLossTime;
	/**
	 * 异常推送频率(分钟/次)
     */
	private Integer exceptionPushTime;
	/**
	 * 蓝牙签到有效距离(米）：beacon_sign_distance,单位米
	 */
	private Integer beaconSignDistance;
	/**
	 * GPS签到有效距离(米）：gps_sign_distance,单位米
	 */
	private Integer gpsSignDistance;
	/**
	 * 循环签到周期(多少分钟一次)：sign_range,单位分钟
	 */
	private Integer signRange;
	/**
	 * 超出循环周期时间处理方式：overtime_deal，1记为应签，2丢弃
	 */
	private Integer overtimeDeal;
	
	@Id
	@Column(name="id",nullable=false,unique=true)
	@GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="upload_time")
	public Integer getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Integer uploadTime) {
		this.uploadTime = uploadTime;
	}
	@Column(name="leave_region_distance")
	public Integer getLeaveRegionDistance() {
		return leaveRegionDistance;
	}
	public void setLeaveRegionDistance(Integer leaveRegionDistance) {
		this.leaveRegionDistance = leaveRegionDistance;
	}
	@Column(name="start_patrol_time")
	public Integer getStartPatrolTime() {
		return startPatrolTime;
	}
	public void setStartPatrolTime(Integer startPatrolTime) {
		this.startPatrolTime = startPatrolTime;
	}
	@Column(name="campus_num")
	public Integer getCampusNum() {
		return campusNum;
	}
	public void setCampusNum(Integer campusNum) {
		this.campusNum = campusNum;
	}
	@Column(name="is_emergency")
	public Integer getIsEmergency() {
		return isEmergency;
	}
	public void setIsEmergency(Integer isEmergency) {
		this.isEmergency = isEmergency;
	}
	@Column(name="refresh_time")
	public Integer getRefreshTime() {
		return refreshTime;
	}
	public void setRefreshTime(Integer refreshTime) {
		this.refreshTime = refreshTime;
	}
	@Column(name="lazy_time")
	public Integer getLazyTime() {
		return lazyTime;
	}
	public void setLazyTime(Integer lazyTime) {
		this.lazyTime = lazyTime;
	}
	@Column(name="leave_region_time")
	public Integer getLeaveRegionTime() {
		return leaveRegionTime;
	}
	public void setLeaveRegionTime(Integer leaveRegionTime) {
		this.leaveRegionTime = leaveRegionTime;
	}
	@Column(name = "personnel_loss_time")
	public Integer getPersonnelLossTime() {
		return personnelLossTime;
	}

	public void setPersonnelLossTime(Integer personnelLossTime) {
		this.personnelLossTime = personnelLossTime;
	}
	@Column(name = "exception_push_time")
	public Integer getExceptionPushTime() {
		return exceptionPushTime;
	}

	public void setExceptionPushTime(Integer exceptionPushTime) {
		this.exceptionPushTime = exceptionPushTime;
	}
	@Column(name = "beacon_sign_distance")
	public Integer getBeaconSignDistance() {
		return beaconSignDistance;
	}

	public void setBeaconSignDistance(Integer beaconSignDistance) {
		this.beaconSignDistance = beaconSignDistance;
	}

	@Column(name = "gps_sign_distance")
	public Integer getGpsSignDistance() {
		return gpsSignDistance;
	}

	public void setGpsSignDistance(Integer gpsSignDistance) {
		this.gpsSignDistance = gpsSignDistance;
	}

	@Column(name = "sign_range")
	public Integer getSignRange() {
		return signRange;
	}

	public void setSignRange(Integer signRange) {
		this.signRange = signRange;
	}

	@Column(name = "overtime_deal")
	public Integer getOvertimeDeal() {
		return overtimeDeal;
	}

	public void setOvertimeDeal(Integer overtimeDeal) {
		this.overtimeDeal = overtimeDeal;
	}
}
