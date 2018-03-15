package com.parkbobo.utils.httpmodel;

import java.io.Serializable;

public class LoginToken implements Serializable{

	/**
	 * 登录成功返回
	 */
	private static final long serialVersionUID = -2465046847021739273L;
	private Integer errorCode;
	private String message;
	private Integer right;
	private String token;
	private String validTime;
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getRight() {
		return right;
	}
	public void setRight(Integer right) {
		this.right = right;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getValidTime() {
		return validTime;
	}
	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
	
}
