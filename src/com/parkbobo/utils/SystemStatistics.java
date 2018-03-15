package com.parkbobo.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class SystemStatistics {
	/**
	 * 	起始时间
	 * */
	private Date startTime;
	/**
	 * 终止时间
	 * */
	private Date endTime;
	/**
	 * 停车场名称
	 * */
	private String carparkname;
	/**
	 * 地区
	 * */
	private String area;
	/**
	 * 停车场id
	 * */
	private Long carparkid;
	/**
	 * 总小时数
	 * */
	private BigDecimal totalTime;
	/**
	 * 超期停车数
	 * */
	private BigInteger overTimeNum;
	/**
	 *总收费 
	 * */
	private BigDecimal totalMoney;
	/**
	 *总收费 
	 * */
	private BigDecimal totalOverTimeMoney;
	/**
	 * 总出租车位
	 * */
	private BigInteger totalCarNum;
	  /**
     * 车位主收益
     */
    private BigDecimal totalOwnerRevenue;
    /**
     * 物业收益
     */
    private BigDecimal totalPropertyRevenue;
    /**
     * 平台收益
     */
    private BigDecimal totalCompanyRevenue;
	
	public BigDecimal getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(BigDecimal totalTime) {
		this.totalTime = totalTime;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public BigInteger getOverTimeNum() {
		return overTimeNum;
	}
	public void setOverTimeNum(BigInteger overTimeNum) {
		this.overTimeNum = overTimeNum;
	}
	public BigDecimal getTotalMoney() {
		if(this.totalMoney==null){
			return new BigDecimal("0");
		}else{
			BigDecimal b2 = new BigDecimal("100");
			BigDecimal b3 = this.totalMoney.divide(b2,2,BigDecimal.ROUND_HALF_EVEN);
			return b3;
		}
	}
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
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
	public BigInteger getTotalCarNum() {
		return totalCarNum;
	}
	public void setTotalCarNum(BigInteger totalCarNum) {
		this.totalCarNum = totalCarNum;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getCarparkname() {
		return carparkname;
	}
	public void setCarparkname(String carparkname) {
		this.carparkname = carparkname;
	}
	
	public Long getCarparkid() {
		return carparkid;
	}
	public void setCarparkid(Long carparkid) {
		this.carparkid = carparkid;
	}
	public BigDecimal getTotalOwnerRevenue() {
		if(this.totalOwnerRevenue==null){
			return new BigDecimal("0");
		}else{
			BigDecimal b2 = new BigDecimal("100");
			BigDecimal b3 = this.totalOwnerRevenue.divide(b2,2,BigDecimal.ROUND_HALF_EVEN);
			return b3;
		}
	}
	public void setTotalOwnerRevenue(BigDecimal totalOwnerRevenue) {
		this.totalOwnerRevenue = totalOwnerRevenue;
	}
	public BigDecimal getTotalPropertyRevenue() {
		if(this.totalPropertyRevenue==null){
			return new BigDecimal("0");
		}else{
			BigDecimal b2 = new BigDecimal("100");
			BigDecimal b3 = this.totalPropertyRevenue.divide(b2,2,BigDecimal.ROUND_HALF_EVEN);
			return b3;
		}
	}
	public void setTotalPropertyRevenue(BigDecimal totalPropertyRevenue) {
		this.totalPropertyRevenue = totalPropertyRevenue;
	}
	public BigDecimal getTotalCompanyRevenue() {
		if(this.totalCompanyRevenue==null){
			return new BigDecimal("0");
		}else{
			BigDecimal b2 = new BigDecimal("100");
			BigDecimal b3 = this.totalCompanyRevenue.divide(b2,2,BigDecimal.ROUND_HALF_EVEN);
			return b3;
		}
	}
	public void setTotalCompanyRevenue(BigDecimal totalCompanyRevenue) {
		this.totalCompanyRevenue = totalCompanyRevenue;
	}
	public String getTotalTimeHours(){
		if(this.totalTime!=null && !this.totalTime.equals(BigDecimal.ZERO)){
			String totalTimeStr = String.valueOf(this.totalTime);
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
	

	@Override
	public String toString() {
		return "SystemStatistics [carparkname=" + carparkname + ", endTime="
				+ endTime + ", overTimeNum=" + overTimeNum + ", startTime="
				+ startTime + ", totalCarNum=" + totalCarNum
				+ ", totalCompanyRevenue=" + totalCompanyRevenue
				+ ", totalMoney=" + totalMoney + ", totalOwnerRevenue="
				+ totalOwnerRevenue + ", totalPropertyRevenue="
				+ totalPropertyRevenue + ", totalTime=" + totalTime + "]";
	}
	
	
	
}
