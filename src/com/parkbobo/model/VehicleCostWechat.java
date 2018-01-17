package com.parkbobo.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import javax.persistence.Entity;

/**
 * 微信支付记录
 * @author RY
 *
 */

@Entity
@Table(name="lq_park_vehicle_cost_wechat")
@SequenceGenerator(name="generator", sequenceName="lq_park_vehicle_cost_wechat_kid_seq", allocationSize=1)
public class VehicleCostWechat implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2513754381056577142L;
	private Integer kid;//流水号
	private String terminCode;//终端编号
	private Long carparkid;//停车场编号
	private String payUnid;//账单编号
	private String carPlate;//车牌号码
	private String inUnid;//入场ID
	private Date inTime;//入场时间
	private Date lastPayTime;//上次结算时间
	private Date planPayTime;//此次结算时间
	private Date payTime;//支付时间
	private Integer shouldPayMoney;//需缴金额
	private Integer status;//缴费状态，1已缴费，0未缴费，-1已退款
	private Integer pushStatus;//推送状态，1推送成功，0未推送，-1推送失败
	private Integer leaveOut;//缴费成功后的离场时间

	@Id
	@Column(name="kid", unique=true, nullable=false)
	@GeneratedValue(generator="generator", strategy=GenerationType.AUTO)
	public Integer getKid() {
		return kid;
	}

	public void setKid(Integer kid) {
		this.kid = kid;
	}

	@Column(name="termin_code")
	public String getTerminCode() {
		return terminCode;
	}

	public void setTerminCode(String terminCode) {
		this.terminCode = terminCode;
	}

	@Column(name="carparkid")
	public Long getCarparkid() {
		return carparkid;
	}

	public void setCarparkid(Long carparkid) {
		this.carparkid = carparkid;
	}

	@Column(name="pary_unid")
	public String getPayUnid() {
		return payUnid;
	}

	public void setPayUnid(String payUnid) {
		this.payUnid = payUnid;
	}

	@Column(name="car_plate")
	public String getCarPlate() {
		return carPlate;
	}

	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}

	@Column(name="inunid")
	public String getInUnid() {
		return inUnid;
	}

	public void setInUnid(String inUnid) {
		this.inUnid = inUnid;
	}

	@Column(name="in_time")
	public Date getInTime() {
		return inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	@Column(name="last_pay_time")
	public Date getLastPayTime() {
		return lastPayTime;
	}

	public void setLastPayTime(Date lastPayTime) {
		this.lastPayTime = lastPayTime;
	}

	@Column(name="plan_pay_time")
	public Date getPlanPayTime() {
		return planPayTime;
	}

	public void setPlanPayTime(Date planPayTime) {
		this.planPayTime = planPayTime;
	}

	@Column(name="pay_time")
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	@Column(name="should_pay_money")
	public Integer getShouldPayMoney() {
		return shouldPayMoney;
	}

	public void setShouldPayMoney(Integer shouldPayMoney) {
		this.shouldPayMoney = shouldPayMoney;
	}

	@Column(name="status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name="push_status")
	public Integer getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Integer pushStatus) {
		this.pushStatus = pushStatus;
	}
	
	@Column(name="leave_out_time")
	public Integer getLeaveOut() {
		return leaveOut;
	}

	public void setLeaveOut(Integer leaveOut) {
		this.leaveOut = leaveOut;
	}

	@Transient
	public String formatInTime(){
		if(inTime != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(this.inTime);
		}
		return "";
	}
	
	@Transient
	public String formatPlanyTime(){
		if(planPayTime != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(this.planPayTime);
		}
		return "";
	}
	
	@Transient
	public String formatParkTime(){
		if(planPayTime != null && inTime != null){
			Long parkTime = planPayTime.getTime() - inTime.getTime();
			return formatMillisecondToHour(parkTime);
		}
		else{
			return "未查询到停车时间";
		}
	}
	
	@Transient
	public String formatPayTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(this.payTime);
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
	
	@Transient
	public String formateShouldPayMoney(){
		try {
			if(shouldPayMoney != null){
				if(shouldPayMoney < 10){
					return "0.0" + shouldPayMoney;
				}
				else if (shouldPayMoney < 100){
					return "0." + shouldPayMoney;
				}
				else{
					return shouldPayMoney / 100 +  "." + shouldPayMoney % 100;
				}
			}
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


}
