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
}
