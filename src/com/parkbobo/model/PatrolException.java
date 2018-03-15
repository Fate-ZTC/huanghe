package com.parkbobo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="patrol_exception")
@SequenceGenerator(name="generator", sequenceName="patrol_exeception_id_seq", allocationSize = 1)
public class PatrolException implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3611650260159180016L;
	/**
	 * 错误类型id
	 */
	private Integer id;
	/**
	 * 错误类型
	 */
	private Integer type;
	/**
	 * 错误名称
	 */
	private String exceptionName;
	@Id
	@Column(name="id",unique=true,nullable=false)
	@GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column(name="exception_name")
	public String getExceptionName() {
		return exceptionName;
	}
	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}
	
	

}
