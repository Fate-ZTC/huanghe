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
@Table(name="fire_patrol_exception")
@SequenceGenerator(name="generator", sequenceName="fire_patrol_exception_id_seq", allocationSize = 1)
public class FirePatrolException implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7483135285157482606L;
	/**
	 * id 主键
	 */
	private Integer id;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 异常名称
	 */
	private String exceptionName;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	@Id
	@Column(name="id",nullable=false,unique=true)
	@GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="sort")
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	@Column(name="exception_name")
	public String getExceptionName() {
		return exceptionName;
	}
	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}
	@Column(name="update_time")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
