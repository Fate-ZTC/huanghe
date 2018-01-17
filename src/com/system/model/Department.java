package com.system.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="lq_department")
@SequenceGenerator(name="generator",sequenceName="lq_department_departmentid_seq",allocationSize=1)
public class Department  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4427873959054198733L;
	private Integer departmentid;
    private String name;
    private Date createTime;
    private String description;
    private Integer orderid;
    private String memo;

    
    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO, generator="generator")
    @Column(name="departmentid", unique=true, nullable=false)

    public Integer getDepartmentid() {
        return this.departmentid;
    }
    
    public void setDepartmentid(Integer departmentid) {
        this.departmentid = departmentid;
    }
    
    @Column(name="name", length=100)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="create_time", length=29)

    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @Column(name="description")

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name="orderid")

    public Integer getOrderid() {
        return this.orderid;
    }
    
    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }
    
    @Column(name="memo")

    public String getMemo() {
        return this.memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }
}