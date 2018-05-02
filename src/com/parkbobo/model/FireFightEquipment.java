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
@Table(name="fire_fight_equipment")
@SequenceGenerator(name="generator", sequenceName="fire_fight_equipment_info_id_seq", allocationSize = 1)
public class FireFightEquipment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3369331990416418104L;
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
	private float lon;
	/**
	 * 纬度
	 */
	private float lat;
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
	 * 楼层id
     */
	private String floorid;
	/**
	 * 图标
     */
	private String icon;
	/**
	 * 更新的id
     */
	private Integer pointid;

	/**
	 * 消防设备分类id
     */
	private Integer categoryid;




//	      "coordinate": "[112.54907589175903,32.96925426267295]",
//				  "floorid": "103214",
//				  "geometry": "POINT (112.54907589175903 32.96925426267295)",
//				  "icon": "zhuantitu/icon/xfk.png",
//				  "images": "",
//				  "name": "消防栓",
//				  "pointid": 333



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
	public float getLon() {
		return lon;
	}
	public void setLon(float lon) {
		this.lon = lon;
	}
	@Column(name="lat")
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
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

	public String getFloorid() {
		return floorid;
	}

	public void setFloorid(String floorid) {
		this.floorid = floorid;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getPointid() {
		return pointid;
	}

	public void setPointid(Integer pointid) {
		this.pointid = pointid;
	}

	public Integer getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(Integer categoryid) {
		this.categoryid = categoryid;
	}
}
