package com.parkbobo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="fire_patrol_img")
@SequenceGenerator(name="generator", sequenceName="fire_patrol_img_id_seq", allocationSize = 1)
public class FirePatrolImg implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7438756138909102303L;
	/**
	 * 主键id
	 */
	private Integer id;
	/**
	 * 图片地址
	 */
	private String imgUrl;
	/**
	 * 上传时间
	 */
	private Date uploadTIme;
	/**
	 * 巡查员信息
	 */
	private PatrolUser patrolUser;
	/**
	 * 设备信息
	 */
	private FireFightEquipment fireFightEquipment;
	
	@Id
	@Column(name="id",nullable=false,unique=true)
	@GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="img_url")
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	@Column(name="upload_time")
	public Date getUploadTIme() {
		return uploadTIme;
	}
	public void setUploadTIme(Date uploadTIme) {
		this.uploadTIme = uploadTIme;
	}
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="user_id")
	public PatrolUser getPatrolUser() {
		return patrolUser;
	}
	public void setPatrolUser(PatrolUser patrolUser) {
		this.patrolUser = patrolUser;
	}
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="equipment_id")
	public FireFightEquipment getFireFightEquipment() {
		return fireFightEquipment;
	}
	public void setFireFightEquipment(FireFightEquipment fireFightEquipment) {
		this.fireFightEquipment = fireFightEquipment;
	}
	
	
	
}
