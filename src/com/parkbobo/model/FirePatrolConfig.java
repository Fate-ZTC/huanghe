package com.parkbobo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fire_patrol_config")
public class FirePatrolConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8278502394483036290L;
	/**
	 * id 主键
	 */
	private Integer id;
	/**
	 * 最远扫码距离
	 */
	private Integer distance;
	/**
	 * 消防设备类型id
     */
	private String equipmentType;

	/**
	 * 室外所有正常设备经度
	 */
	private double outAllNormalSweepLon;

	/**
	 * 室外所有正常设备纬度
	 */
	private double outAllNormalSweepLat;

	@Id
	@Column(name="id",nullable=false,unique=true)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="distance")
	public Integer getDistance() {
		return distance;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	@Column(name = "equipment_type")
	public String getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}

	@Column(name = "out_all_normal_sweep_lon")
	public double getOutAllNormalSweepLon() {
		return outAllNormalSweepLon;
	}

	public void setOutAllNormalSweepLon(double outAllNormalSweepLon) {
		this.outAllNormalSweepLon = outAllNormalSweepLon;
	}

	@Column(name = "out_all_normal_sweep_lat")
	public double getOutAllNormalSweepLat() {
		return outAllNormalSweepLat;
	}

	public void setOutAllNormalSweepLat(double outAllNormalSweepLat) {
		this.outAllNormalSweepLat = outAllNormalSweepLat;
	}
}
