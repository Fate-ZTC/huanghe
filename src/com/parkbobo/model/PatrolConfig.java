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
@Table
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
	 * 异常报警距离
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
	
	
}
