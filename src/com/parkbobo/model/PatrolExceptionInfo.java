package com.parkbobo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="patrol_exception_info")
@SequenceGenerator(name="generator", sequenceName="patrol_exeception_info_id_seq", allocationSize = 1)
public class PatrolExceptionInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6901109507268310526L;
	/**
	 * 异常信息id
	 */
	private Integer id;
	/**
	 * 用户区域
	 */
	private Integer usregId;
	/*private PatrolUserRegion patrolUserRegion;*/
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 工号
	 */
	private String jobNum;
	/**
	 * 异常名称
	 */
	private String exceptionName;
	/**
	 * 异常类型
	 */
	private Integer exceptionType;
	/**
	 * 时间戳
	 */
	private Date createTime;
	/**
	 * 巡更
	 */
	@Id
	@Column(name="id",nullable=false,unique=true)
	@GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}


/*	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name="usregid")
	public PatrolUserRegion getPatrolUserRegion() {
		return patrolUserRegion;
	}

	public void setPatrolUserRegion(PatrolUserRegion patrolUserRegion) {
		this.patrolUserRegion = patrolUserRegion;
	}*/
	public Integer getUsregId() {
		return usregId;
	}
	public void setUsregId(Integer usregId) {
		this.usregId = usregId;
	}

	@Column(name="username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="job_num")
	public String getJobNum() {
		return jobNum;
	}
	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}
	@Column(name="exception_name")
	public String getExceptionName() {
		return exceptionName;
	}
	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}
	@Column(name="exception_type")
	public Integer getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(Integer exceptionType) {
		this.exceptionType = exceptionType;
	}
	@Column(name="createtime")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
