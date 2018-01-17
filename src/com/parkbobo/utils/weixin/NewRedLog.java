package com.parkbobo.utils.weixin;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewRedLog implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6583388351461761855L;
	private String usersname;
	private Integer totalAmount;
	private Integer ticketAmount;
	private String sendTime;
	private Integer isAmRed;//1:现金红包
	private String sponsored;
	private String sponImgage;
	private String userhead;
	
	
	
	public NewRedLog() {
		super();
	}
	public NewRedLog(String usersname, Integer totalAmount,
			Integer ticketAmount, String sendTime,String userhead) {
		super();
		this.usersname = usersname;
		this.totalAmount = totalAmount;
		this.ticketAmount = ticketAmount;
		this.sendTime = sendTime;
		this.userhead = userhead;
		if(totalAmount==0){
			this.isAmRed = 0;			
		}else{
			this.isAmRed = 1;
		}
	}
	
	
	public NewRedLog(Integer totalAmount, Integer ticketAmount,
			String sponsored, String sponImgage) {
		super();
		this.totalAmount = totalAmount;
		this.ticketAmount = ticketAmount;
		this.sponsored = sponsored;
		this.sponImgage = sponImgage;
		if(totalAmount==0){
			this.isAmRed = 0;			
		}else{
			this.isAmRed = 1;
		}
	}
	public String getUsersname() {
		return usersname;
	}
	public void setUsersname(String usersname) {
		this.usersname = usersname;
	}
	public Integer getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Integer getTicketAmount() {
		return ticketAmount;
	}
	public void setTicketAmount(Integer ticketAmount) {
		this.ticketAmount = ticketAmount;
	}
	public String getSendTime() {
		if(this.sendTime!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				Date parse = sdf.parse(this.sendTime);
				sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
				return sdf.format(parse);
			} catch (ParseException e) {
				e.printStackTrace();
				return "";
			}
		}else{
			return "";			
		}
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getIsAmRed() {
		return isAmRed;
	}
	public void setIsAmRed(Integer isAmRed) {
		this.isAmRed = isAmRed;
	}
	public String getSponsored() {
		return sponsored;
	}
	public void setSponsored(String sponsored) {
		this.sponsored = sponsored;
	}
	public String getSponImgage() {
		return sponImgage;
	}
	public void setSponImgage(String sponImgage) {
		this.sponImgage = sponImgage;
	}
	public String getUserhead() {
		return userhead;
	}
	public void setUserhead(String userhead) {
		this.userhead = userhead;
	}
	
}
