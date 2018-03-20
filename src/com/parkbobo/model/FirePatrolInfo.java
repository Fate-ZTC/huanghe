package com.parkbobo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="fire_patrol_info")
@SequenceGenerator(name="generator", sequenceName="fire_patrol_info_id_seq", allocationSize = 1)
public class FirePatrolInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7623324663655191769L;
	/**
	 * 异常信息id
	 */
	private Integer id;
	/**
	 * 用户区域
	 */
	private PatrolUser patrolUser;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 校区
	 */
	private Integer campusNum;
	/**
	 * 时间戳
	 */
	private Date timestamp;
	/**
	 * 异常状态 0异常 1正常
	 */
	private Integer patrolStatus;
	/**
	 * 异常码 格式(1,2,3,)
	 */
	private String exceptionTypes;
	/**
	 * 设备id
	 */
	private FireFightEquipment fireFightEquipment;
	/**
	 * 异常描述
	 */
	private String description;
	/**
	 * 是否是最新的一条
	 */
	private Short isNewest;
	@Id
	@Column(name="id",nullable=false,unique=true)
	@GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@OneToOne
	@JoinColumn(name="user_id")
	public PatrolUser getPatrolUser() {
		return patrolUser;
	}
	public void setPatrolUser(PatrolUser patrolUser) {
		this.patrolUser = patrolUser;
	}
	@Column(name="username")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	@Column(name="patrol_status")
	public Integer getPatrolStatus() {
		return patrolStatus;
	}
	public void setPatrolStatus(Integer patrolStatus) {
		this.patrolStatus = patrolStatus;
	}
	@Column(name="exception_types")
	public String getExceptionTypes() {
		return exceptionTypes;
	}
	public void setExceptionTypes(String exceptionTypes) {
		this.exceptionTypes = exceptionTypes;
	}
	@OneToOne
	@JoinColumn(name="equipment_id")
	public FireFightEquipment getFireFightEquipment() {
		return fireFightEquipment;
	}
	public void setFireFightEquipment(FireFightEquipment fireFightEquipment) {
		this.fireFightEquipment = fireFightEquipment;
	}
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name="is_newest")
	public Short getIsNewest() {
		return isNewest;
	}
	public void setIsNewest(Short isNewest) {
		this.isNewest = isNewest;
	}
	
}
