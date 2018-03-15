package com.parkbobo.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class SystemCarStatistics implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3075425677739727002L;
	/**
	 *停车场id 
	 * */
	private BigInteger carparkid;
	/**
	 * 停车场名称
	 * */
	private String carparkname;
	/**
	 * 城市
	 * */
	private String city;
	/**
	 * 总订单数
	 * */
	private BigInteger carparkTotalOrder;
	/**
	 * 总车位数
	 * */
	private BigInteger carparkBerthShareNum;
	/**
	 * 总小时数
	 * */
	private BigDecimal carparkTime;
	/**
	 *总收费 
	 * */
	private BigDecimal carparkMoney;
	/**
	 * 总出租车位
	 * */
	private BigInteger carparkCarNum;
	/**
	 * 超期停车数
	 * */
	private BigInteger overTimeNum;
	  /**
     * 车位主收益
     */
    private BigDecimal carparkOwnerRevenue;
    /**
     * 物业收益
     */
    private BigDecimal carparkPropertyRevenue;
    /**
     * 平台收益
     */
    private BigDecimal carparkCompanyRevenue;
    /**
	 *总收费 
	 * */
	private BigDecimal totalOverTimeMoney;

    
	public BigInteger getCarparkid() {
		return carparkid;
	}

	public void setCarparkid(BigInteger carparkid) {
		this.carparkid = carparkid;
	}



	public String getCarparkname() {
		return carparkname;
	}



	public void setCarparkname(String carparkname) {
		this.carparkname = carparkname;
	}



	public BigInteger getCarparkTotalOrder() {
		return carparkTotalOrder;
	}



	public void setCarparkTotalOrder(BigInteger carparkTotalOrder) {
		this.carparkTotalOrder = carparkTotalOrder;
	}
	public BigInteger getOverTimeNum() {
		return overTimeNum;
	}
	public void setOverTimeNum(BigInteger overTimeNum) {
		this.overTimeNum = overTimeNum;
	}



	public BigDecimal getCarparkTime() {
		return carparkTime;
	}



	public void setCarparkTime(BigDecimal carparkTime) {
		this.carparkTime = carparkTime;
	}



	public BigDecimal getCarparkMoney() {
		if(this.carparkMoney==null){
			return new BigDecimal("0");
			
		}else{
			return this.formatBigDecimal(carparkMoney);
		}
	}



	public void setCarparkMoney(BigDecimal carparkMoney) {
		this.carparkMoney = carparkMoney;
	}



	public BigInteger getCarparkCarNum() {
		return carparkCarNum;
	}



	public void setCarparkCarNum(BigInteger carparkCarNum) {
		this.carparkCarNum = carparkCarNum;
	}



	public BigDecimal getCarparkOwnerRevenue() {
		if(this.carparkOwnerRevenue==null){
			return new BigDecimal("0");
			
		}else{
			return this.formatBigDecimal(carparkOwnerRevenue);
		}
	}

	public void setCarparkOwnerRevenue(BigDecimal carparkOwnerRevenue) {
		this.carparkOwnerRevenue = carparkOwnerRevenue;
	}



	public BigDecimal getCarparkPropertyRevenue() {
		if(this.carparkPropertyRevenue==null){
			return new BigDecimal("0");
			
		}else{
			return this.formatBigDecimal(carparkPropertyRevenue);
		}
	}



	public void setCarparkPropertyRevenue(BigDecimal carparkPropertyRevenue) {
		this.carparkPropertyRevenue = carparkPropertyRevenue;
	}



	public BigDecimal getCarparkCompanyRevenue() {
		if(this.carparkCompanyRevenue==null){
			return new BigDecimal("0");
		}else{
			return this.formatBigDecimal(carparkCompanyRevenue);
		}
	}



	public void setCarparkCompanyRevenue(BigDecimal carparkCompanyRevenue) {
		this.carparkCompanyRevenue = carparkCompanyRevenue;
	}

	public String getCarparkTotalTimeHours(){
		if(this.carparkTime!=null && !this.carparkTime.equals(BigDecimal.ZERO)){
			String totalTimeStr = String.valueOf(this.carparkTime);
			return this.formatMillisecondToHour(Long.valueOf(totalTimeStr));			
		}else{
			return String.valueOf(new BigDecimal("0"));
		}
	}


	private String formatMillisecondToHour(Long millisecond)
	{
		if((millisecond % (1000L * 60 * 60)) == 0)
		{
			return millisecond / (1000L * 60 * 60) + "小时";
		}
		else
		{
			if(millisecond < 1000l * 60 * 60)
			{
				return (millisecond % (1000L * 60 * 60)) / (1000L * 60) + "分钟";
			}
			else
			{
				return millisecond / (1000L * 60 * 60) + "小时" 
				+ (millisecond % (1000L * 60 * 60)) / (1000L * 60) + "分钟";
			}
		}
	}
	
	
	
	public BigDecimal formatBigDecimal(BigDecimal num){
		BigDecimal b2 = new BigDecimal("100");
		return num.divide(b2,2,BigDecimal.ROUND_HALF_EVEN);
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setCarparkBerthShareNum(BigInteger carparkBerthShareNum) {
		this.carparkBerthShareNum = carparkBerthShareNum;
	}

	public BigInteger getCarparkBerthShareNum() {
		return carparkBerthShareNum;
	}
	public BigDecimal getTotalOverTimeMoney() {
		if(this.totalOverTimeMoney==null){
			return new BigDecimal("0");
		}else{
			BigDecimal b2 = new BigDecimal("100");
			BigDecimal b3 = this.totalOverTimeMoney.divide(b2,2,BigDecimal.ROUND_HALF_EVEN);
			return b3;
		}
	}
	public void setTotalOverTimeMoney(BigDecimal totalOverTimeMoney) {
		this.totalOverTimeMoney = totalOverTimeMoney;
	}
	@Override
	public String toString() {
		return "SystemCarStatistics [carparkBerthShareNum="
				+ carparkBerthShareNum + ", carparkCarNum=" + carparkCarNum
				+ ", carparkCompanyRevenue=" + carparkCompanyRevenue
				+ ", carparkMoney=" + carparkMoney + ", carparkOwnerRevenue="
				+ carparkOwnerRevenue + ", carparkPropertyRevenue="
				+ carparkPropertyRevenue + ", carparkTime=" + carparkTime
				+ ", carparkTotalOrder=" + carparkTotalOrder + ", carparkid="
				+ carparkid + ", carparkname=" + carparkname + ", city=" + city
				+ "]";
	}
	
	
}
