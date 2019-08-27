package com.mobile.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="lq_app_version")
public class AppVersion implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -583813268634702383L;
	/**
	 * 版本号
	 */
	private String versioncode;
	/**
	 * 版本名称
	 */
	private String name;
	/**
	 * 类型，1掌上黄淮，2黄淮图书馆APP
	 */
	private Integer type;
	/**
	 * 文件下载地址
	 */
	private String attached;
	/**
	 * 提交时间
	 */
	private Date posttime;
	/**
	 * 下载次数
	 */
	private int downloadcount;
	/**
	 * 是否删除，0：否；1：是
	 */
	private int isDel;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 更新内容
	 */
	private String content;
	/**
	 * 是否需要强制更新
	 */
	private String needUpdate;
	
	@Id 
    @Column(name="versioncode", unique=true, nullable=false)
	public String getVersioncode() {
		return versioncode;
	}
	
	public void setVersioncode(String versioncode) {
		this.versioncode = versioncode;
	}
	
	@Column(name="attached")
	public String getAttached() {
		return attached;
	}
	
	public void setAttached(String attached) {
		this.attached = attached;
	}
	
	@Column(name="posttime")
	public Date getPosttime() {
		return posttime;
	}
	
	public void setPosttime(Date posttime) {
		this.posttime = posttime;
	}
	
	@Column(name="downloadcount")
	public int getDownloadcount() {
		return downloadcount;
	}
	
	public void setDownloadcount(int downloadcount) {
		this.downloadcount = downloadcount;
	}
	
	@Column(name="memo")
	public String getMemo() {
		return memo;
	}
	
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Column(name="is_del")
	public int getIsDel() {
		return isDel;
	}
	
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	
	@Column(name="content")
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "type")
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Column(name = "need_update")
	public String getNeedUpdate() {
		return needUpdate;
	}
	public void setNeedUpdate(String needUpdate) {
		this.needUpdate = needUpdate;
	}
	
	

}