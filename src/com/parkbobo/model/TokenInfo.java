package com.parkbobo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 存储酒城停车token的实体
 * @author RY
 * @version 1.0
 * @since 2017-6-26 19:42:17
 *
 */

@Entity
@Table(name="lq_token_info")
@SequenceGenerator(name="generator", sequenceName="lq_token_info_kid_seq", allocationSize=1)
public class TokenInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2039715871462993222L;
	/**
	 * 主键ID
	 */
	private Integer kid;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * token数据
	 */
	private String token;
	/**
	 * 失效日期
	 */
	private String validTime;
	/**
	 * 权限
	 */
	private Integer right;

	@Id
	@Column(name="kid", unique=true, nullable=false)
	@GeneratedValue(generator="generator", strategy=GenerationType.AUTO)
	public Integer getKid() {
		return kid;
	}

	public void setKid(Integer kid) {
		this.kid = kid;
	}

	@Column(name="username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name="password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name="token")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Column(name="valid_time")
	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	@Column(name="right")
	public Integer getRight() {
		return right;
	}

	public void setRight(Integer right) {
		this.right = right;
	}

}
