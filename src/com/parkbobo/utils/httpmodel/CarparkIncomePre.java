package com.parkbobo.utils.httpmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarparkIncomePre implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -315962942397627944L;
	private Integer totalShouldPay =0;
	private Integer totalRealPay  =0;
	private Integer totalDeposit  =0;
	private List<CarparkIncomes> carparkIncomes = new ArrayList<CarparkIncomes>();
	public Integer getTotalShouldPay() {
		return totalShouldPay;
	}
	public void setTotalShouldPay(Integer totalShouldPay) {
		this.totalShouldPay = totalShouldPay;
	}
	public Integer getTotalRealPay() {
		return totalRealPay;
	}
	public void setTotalRealPay(Integer totalRealPay) {
		this.totalRealPay = totalRealPay;
	}
	public Integer getTotalDeposit() {
		return totalDeposit;
	}
	public void setTotalDeposit(Integer totalDeposit) {
		this.totalDeposit = totalDeposit;
	}
	public List<CarparkIncomes> getCarparkIncomes() {
		return carparkIncomes;
	}
	public void setCarparkIncomes(List<CarparkIncomes> carparkIncomes) {
		this.carparkIncomes = carparkIncomes;
	}
	
	

}
