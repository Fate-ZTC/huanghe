package com.parkbobo.utils.httpmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SendSysIncome implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2171214612428105354L;

	private String token;
	private Integer totalNum;
	private List<SendCarpark> data = new ArrayList<SendCarpark>();
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
	public List<SendCarpark> getData() {
		return data;
	}
	public void setData(List<SendCarpark> data) {
		this.data = data;
	}
	
	
}
