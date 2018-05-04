package com.parkbobo.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

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
	 * 设备原来的id
	 */
	private Integer oldId;
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
	private double lon;
	/**
	 * 纬度
	 */
	private double lat;
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
	/**
	 * 异常信息
	 */
	private Integer fpid;

	/**
	 * 巡查人姓名
     */
	private String userName;
	/**
	 * 巡查人工号
     */
	private String jobNum;

	/**
	 *  楼层id
     */
	private String floorid;


	/**
	 * 大楼id
	 */
	private String buildingCode;


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
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	@Column(name="lat")
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
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
	@Column(name="old_id")
	public Integer getOldId() {
		return oldId;
	}
	public void setOldId(Integer oldId) {
		this.oldId = oldId;
	}
	@Transient
	public Integer getFpid() {
		return fpid;
	}
	public void setFpid(Integer fpid) {
		this.fpid = fpid;
	}

	@Column(name = "username")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name = "job_num")
	public String getJobNum() {
		return jobNum;
	}

	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}

	@Column(name = "floorid")
	public String getFloorid() {
		return floorid;
	}

	public void setFloorid(String floorid) {
		this.floorid = floorid;
	}

	@Column(name = "building_code")
	public String getBuildingCode() {
		return buildingCode;
	}

	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}
}
