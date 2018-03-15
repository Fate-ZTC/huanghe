package com.parkbobo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="patrol_data_info")
public class PatrolDataInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5067263948164734147L;
	/**
	 * 学校id
	 */
	private Integer id;
	/**
	 * 学校名称
	 */
	private String campusName;
	
	@Id
	@Column(name="id",nullable=false,unique=true)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="campus_name")
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	
	
}
