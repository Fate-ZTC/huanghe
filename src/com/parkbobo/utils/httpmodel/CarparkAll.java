package com.parkbobo.utils.httpmodel;

import java.io.Serializable;
import java.util.List;

public class CarparkAll implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3430740448338954623L;
	private String message;
	private Integer errorCode;
	private Integer totalNum;
	private String token;
	private List<Carpark> data;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public List<Carpark> getData() {
		return data;
	}
	public void setData(List<Carpark> data) {
		this.data = data;
	}
	
}
