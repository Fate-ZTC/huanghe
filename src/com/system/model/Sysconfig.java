package com.system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 系统参数配置
 * @author LH
 *
 */
@Entity
@Table(name="lq_sysconfig")
@SequenceGenerator(name = "generator", sequenceName = "lq_sysconfig_configid_seq", allocationSize = 1)
public class Sysconfig implements java.io.Serializable {
	private static final long serialVersionUID = -6226706568411214394L;
	private Integer configid;
	private String varname;
	private String varinfo;
	private String varvalue;
	private Integer orderid;
	private String memo;
	@Id 
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generator")
    @Column(name="configid", unique=true, nullable=false)
	public Integer getConfigid() {
		return configid;
	}
	public void setConfigid(Integer configid) {
		this.configid = configid;
	}
	@Column(name="varname")
	public String getVarname() {
		return varname;
	}
	public void setVarname(String varname) {
		this.varname = varname;
	}
	@Column(name="varinfo")
	public String getVarinfo() {
		return varinfo;
	}
	public void setVarinfo(String varinfo) {
		this.varinfo = varinfo;
	}
	@Column(name="varvalue")
	public String getVarvalue() {
		return varvalue;
	}
	public void setVarvalue(String varvalue) {
		this.varvalue = varvalue;
	}
	@Column(name="orderid")
	public Integer getOrderid() {
		return orderid;
	}
	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}
	@Column(name="memo")
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}

