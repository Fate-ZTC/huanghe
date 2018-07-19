package com.parkbobo.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 巡更签到标签信息
 * @version 1.0
 * @author RY
 * @since 2018-7-6 17:45:37
 */

@Entity
@Table(name="patrol_beacon_info")
@SequenceGenerator(name="generator", sequenceName="patrol_beacon_info_beacon_id_seq", allocationSize = 1)
public class PatrolBeaconInfo implements Serializable {


	private static final long serialVersionUID = 7436531003637290959L;
	/**
	 * 标签ID
	 */
	private Integer beaconId;
	/**
	 * 签到点位信息
	 */
	@JSONField(serialize = false)
	private PatrolSignPointInfo patrolSignPointInfo;
	/**
	 * 标签UUID
	 */
	private String uuid;
	/**
	 * 标签major参数
	 */
	private Integer major;
	/**
	 * 标签minor参数
	 */
	private Integer minor;
	/**
	 * 标签安装位置
	 */
	private String address;
	/**
	 * 标签安装经度
	 */
	private Double lng;
	/**
	 * 标签安装纬度
	 */
	private Double lat;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	@Id
	@Column(name = "beacon_id", unique = true, nullable = false)
	@GeneratedValue(generator = "generator", strategy =  GenerationType.AUTO)
	public Integer getBeaconId() {
		return beaconId;
	}

	public void setBeaconId(Integer beaconId) {
		this.beaconId = beaconId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "point_id")
	public PatrolSignPointInfo getPatrolSignPointInfo() {
		return patrolSignPointInfo;
	}

	public void setPatrolSignPointInfo(PatrolSignPointInfo patrolSignPointInfo) {
		this.patrolSignPointInfo = patrolSignPointInfo;
	}

	@Column(name = "beacon_uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "beacon_major")
	public Integer getMajor() {
		return major;
	}

	public void setMajor(Integer major) {
		this.major = major;
	}

	@Column(name = "beacon_minor")
	public Integer getMinor() {
		return minor;
	}

	public void setMinor(Integer minor) {
		this.minor = minor;
	}

	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "lng")
	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	@Column(name = "lat")
	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	@Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
