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
@Table(name="fire_fight_equipment_history")
@SequenceGenerator(name="generator", sequenceName="fire_fight_equipment_history_id_seq", allocationSize = 1)
public class FireFightEquipmentHistory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8256001129924164070L;
	
	/**
	 * id
	 */
	private Integer id;
	/**
	 * 设备状态 1正常 0异常
	 */
	private Short status;
	/**
	 * 设备名
	 */
	private String name;
	/**
	 * 经度
	 */
	private Float lon;
	/**
	 * 纬度
	 */
	private Float lat;
	/**
	 * 巡查状态 0未检查 1已检查
	 */
	private Short checkStatus;
	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;
	/**
	 * 校区
	 */
	private Integer campusNum;
	@Id
	@Column(name="id",nullable=false,unique=true)
	@GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="status")
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="lon")
	public Float getLon() {
		return lon;
	}
	public void setLon(Float lon) {
		this.lon = lon;
	}
	@Column(name="lat")
	public Float getLat() {
		return lat;
	}
	public void setLat(Float lat) {
		this.lat = lat;
	}
	@Column(name="check_status")
	public Short getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(Short checkStatus) {
		this.checkStatus = checkStatus;
	}
	@Column(name="last_update_time")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	@Column(name="campus_num")
	public Integer getCampusNum() {
		return campusNum;
	}
	public void setCampusNum(Integer campusNum) {
		this.campusNum = campusNum;
	}
}
