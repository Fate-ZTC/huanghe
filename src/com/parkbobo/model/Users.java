package com.parkbobo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 平台用户实体
 * @author RY
 *
 */

@Entity
@Table(name = "lq_users")
public class Users implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8339798622378262390L;
	/**
	 * 手机号作为唯一主键
	 */
	private String mobile;
	/**
	 * 在平台公众号上的openid
	 */
	private String openid;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 性别，1男，2女
	 */
	private Integer sex;
	/**
	 * 所在城市
	 */
	private String city;
	/**
	 * 所在省份
	 */
	private String province;
	/**
	 * 所在国家
	 */
	private String country;
	/**
	 * 头像地址
	 */
	private String head;
	/**
	 * 最后活跃时间
	 */
	private Date lastAcces;
	/**
	 * 添加时间
	 */
	private Date posttime;
	/**
	 * 行驶证图片
	 * */
	private String driverUrl;
	/**
	 * 认证图片
	 * */
	private Integer authStatus;
	/**
	 * 认证不通过原因
	 * */
	private String authReason;
	@Id
	@Column(name = "mobile", nullable = false, unique = true)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "openid")
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Column(name = "nickname")
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "sex")
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Column(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "province")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "country")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "head_img_url")
	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	@Column(name = "last_access_time")
	public Date getLastAcces() {
		return lastAcces;
	}

	public void setLastAcces(Date lastAcces) {
		this.lastAcces = lastAcces;
	}

	@Column(name = "posttime")
	public Date getPosttime() {
		return posttime;
	}

	public void setPosttime(Date posttime) {
		this.posttime = posttime;
	}

	@Column(name="driver_url")
	public String getDriverUrl() {
		return driverUrl;
	}
	public void setDriverUrl(String driverUrl) {
		this.driverUrl = driverUrl;
	}

	@Column(name="auth_status")
	public Integer getAuthStatus() {
		return authStatus;
	}
	public void setAuthStatus(Integer authStatus) {
		this.authStatus = authStatus;
	}
	@Column(name="auth_reason")
	public String getAuthReason() {
		return authReason;
	}

	public void setAuthReason(String authReason) {
		this.authReason = authReason;
	}
	
	
}
