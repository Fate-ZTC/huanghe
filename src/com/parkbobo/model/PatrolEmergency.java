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
import javax.persistence.Transient;

@Entity
@Table(name="patrol_emergency")
@SequenceGenerator(name="generator", sequenceName="patrol_emergency_id_seq", allocationSize = 1)
public class PatrolEmergency implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 985014630996726581L;
	/**
	 * id
	 */
	private Integer id;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 工号
	 */
	private String jobNum;
	/**
	 * 用户姓名
	 */
	private String username;
	
	/**
	 * 校区id
	 */
	private Integer campusNum;
	/**
	 * 紧急事件持续时长
	 */
	private String checkDuration;
	
	@Id
	@Column(name="id",nullable=false,unique=true)
	@GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	@Column(name="campus_num")
	public Integer getCampusNum() {
		return campusNum;
	}
	public void setCampusNum(Integer campusNum) {
		this.campusNum = campusNum;
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
}
