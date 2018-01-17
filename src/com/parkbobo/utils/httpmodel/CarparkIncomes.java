package com.parkbobo.utils.httpmodel;

import java.io.Serializable;
import java.util.List;

public class CarparkIncomes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -315962942397627944L;
	private String message;
	private Integer errorCode;
	private String token;
	private Integer totalNum;
	private List<CarparkIncome> data;
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
	public List<CarparkIncome> getData() {
		return data;
	}
	public void setData(List<CarparkIncome> data) {
		this.data = data;
	}
	public Integer getTotalShouldPay(){
		Integer totalShouldPay = 0;
		if(this.data!=null && this.data.size()>0){
			for (int i = 0; i < data.size(); i++) {
				totalShouldPay += this.data.get(i).getAllPay();
			}
		}
		return totalShouldPay;
	}
	

}
