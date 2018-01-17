package com.parkbobo.utils.httpmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarparkUsagePre implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8373348815523223336L;
	private String message;
	private Integer errorCode;
	private String token;
	private Integer totalNum;
	private List<CarparkUsage> data = new ArrayList<CarparkUsage>();
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public List<CarparkUsage> getData() {
		return data;
	}
	public void setData(List<CarparkUsage> data) {
		this.data = data;
	}
	
	
}
