package com.parkbobo.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
/**
 * @author gjxchn
 * */
public class UserAuth implements Serializable {

	/**
	 * 用户信息审核
	 */
	private static final long serialVersionUID = 1453709351607250587L;
	private String mobile;
	private Date posttime;
	private Integer drivingStauts;
	private String driverStatus;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Date getPosttime() {
		return posttime;
	}
	public void setPosttime(Date posttime) {
		this.posttime = posttime;
	}
	public Integer getDrivingStauts() {
		return drivingStauts;
	}
	public void setDrivingStauts(Integer drivingStauts) {
		this.drivingStauts = drivingStauts;
	}
	public String getDriverStatus() {
		if(this.driverStatus!=null){
			if(driverStatus.contains("0")){
				return "待审核";
			}else if(!driverStatus.contains("0") && !driverStatus.contains("-1")){
				return "全部通过";
			}else if(!driverStatus.contains("0") && !driverStatus.contains("1")){
				return "全部不通过";
			}else{
				return "部分通过";
			}
		}else{
			return "未上传";
		}
	}
	public void setDriverStatus(String driverStatus) {
		this.driverStatus = driverStatus;
	}
	

}
