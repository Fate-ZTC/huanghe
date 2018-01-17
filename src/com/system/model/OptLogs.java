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
@Table(name="lq_opt_logs")
@SequenceGenerator(name="generator", sequenceName = "lq_opt_logs_log_id_seq", allocationSize =1)
public class OptLogs  implements java.io.Serializable {

	private static final long serialVersionUID = 3269457105500439150L;
	private Integer logId;
    private String fromModel;
    private Integer userId;
    private String username;
    private String description;
    private Date createTime;
    private String memo;
    public OptLogs()
    {
    }
    public OptLogs(String fromModel, Integer userId, String username,
		String description, Date createTime) {
		this.fromModel = fromModel;
		this.userId = userId;
		this.username = username;
		this.description = description;
		this.createTime = createTime;
    }

	@Id 
    @GeneratedValue(strategy=GenerationType.AUTO, generator="generator")    
    @Column(name="log_id", unique=true, nullable=false)

    public Integer getLogId() {
        return this.logId;
    }
    
    public void setLogId(Integer logId) {
        this.logId = logId;
    }
    
    @Column(name="from_model", nullable=false, length=200)

    public String getFromModel() {
        return this.fromModel;
    }
    
    public void setFromModel(String fromModel) {
        this.fromModel = fromModel;
    }
    
    @Column(name="user_id", nullable=false)

    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    @Column(name="username", nullable=false, length=100)

    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @Column(name="description")

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name="create_time", nullable=false, length=29)

    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @Column(name="memo")

    public String getMemo() {
        return this.memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }
   








}