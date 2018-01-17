package com.parkbobo.utils.httpmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParkFlowList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5503657397154391168L;
	private String message;
	private Integer errorCode;
	private String token;
	private Integer totalNum;
	private List<ParkFlow> data = new ArrayList<ParkFlow>();
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
	public List<ParkFlow> getData() {
		return data;
	}
	public void setData(List<ParkFlow> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ParkFlowList [data=" + data + ", errorCode=" + errorCode
				+ ", message=" + message + ", token=" + token + ", totalNum="
				+ totalNum + "]";
	}
	
	
}
