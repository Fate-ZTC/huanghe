package com.parkbobo.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 收费数据
 * @author RY
 * @version 1.0
 * @since 2017-6-27 10:52:57
 *
 */

@Entity
@Table(name="lq_park_vehicle_cost")
@SequenceGenerator(name="generator", sequenceName="lq_park_vehicle_cost_kid_seq", allocationSize=1)
public class VehicleCost implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7236201580631961782L;
	private Integer kid;//主键ID
	private Long parkid;//停车点编号
	private Date inTime;//入场时间
	private Date outTime;//出场时间
	private Integer parkTime;//停车时长，单位分钟
	private String carPlate;//车牌号码
	private Integer plateType;//车牌类型
	private Integer plateColor;//车牌颜色
	private Integer carType;//车辆类型
	private String cardNo;//卡号
	private String inUnid;//入场ID
	private String outUnid;//出场ID
	private Integer payMoney;//线下收费金额
	private Integer payType;//字符方式
	private String bookUnid;//订单号
	private Integer needPay;//应收金额
	private Integer gateId;//出入口边傲
	private String collectorCode;//操作员编号
	private String collectorName;//操作员名称
	private String remark;//备注信息
	private String collectorPhone;//操作员电话号码
	private String collectorID;//操作员身份ID
	private Integer parkType;//
	private Integer isLeave;//是否离场对账，1是，0否
	private Integer totalMoney;//实收总金额

	@Id
	@Column(name="kid", unique=true, nullable=false)
	@GeneratedValue(generator="generator", strategy=GenerationType.AUTO)
	public Integer getKid() {
		return kid;
	}

	public void setKid(Integer kid) {
		this.kid = kid;
	}

	@Column(name="carparkid")
	public Long getParkid() {
		return parkid;
	}

	public void setParkid(Long parkid) {
		this.parkid = parkid;
	}

	@Column(name="in_time")
	public Date getInTime() {
		return inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	@Column(name="out_time")
	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	@Column(name="park_time")
	public Integer getParkTime() {
		return parkTime;
	}

	public void setParkTime(Integer parkTime) {
		this.parkTime = parkTime;
	}

	@Column(name="car_plate")
	public String getCarPlate() {
		return carPlate;
	}

	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}

	@Column(name="plate_type")
	public Integer getPlateType() {
		return plateType;
	}

	public void setPlateType(Integer plateType) {
		this.plateType = plateType;
	}

	@Column(name="plate_color")
	public Integer getPlateColor() {
		return plateColor;
	}

	public void setPlateColor(Integer plateColor) {
		this.plateColor = plateColor;
	}

	@Column(name="car_type")
	public Integer getCarType() {
		return carType;
	}

	public void setCarType(Integer carType) {
		this.carType = carType;
	}

	@Column(name="card_no")
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	@Column(name="inunid")
	public String getInUnid() {
		return inUnid;
	}

	public void setInUnid(String inUnid) {
		this.inUnid = inUnid;
	}

	@Column(name="outunid")
	public String getOutUnid() {
		return outUnid;
	}

	public void setOutUnid(String outUnid) {
		this.outUnid = outUnid;
	}

	@Column(name="pay_money")
	public Integer getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Integer payMoney) {
		this.payMoney = payMoney;
	}

	@Column(name="pay_type")
	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	@Column(name="book_unid")
	public String getBookUnid() {
		return bookUnid;
	}

	public void setBookUnid(String bookUnid) {
		this.bookUnid = bookUnid;
	}

	@Column(name="need_pay")
	public Integer getNeedPay() {
		return needPay;
	}

	public void setNeedPay(Integer needPay) {
		this.needPay = needPay;
	}

	@Column(name="gate_id")
	public Integer getGateId() {
		return gateId;
	}

	public void setGateId(Integer gateId) {
		this.gateId = gateId;
	}

	@Column(name="collector_code")
	public String getCollectorCode() {
		return collectorCode;
	}

	public void setCollectorCode(String collectorCode) {
		this.collectorCode = collectorCode;
	}

	@Column(name="collector_name")
	public String getCollectorName() {
		return collectorName;
	}

	public void setCollectorName(String collectorName) {
		this.collectorName = collectorName;
	}

	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="collector_phone")
	public String getCollectorPhone() {
		return collectorPhone;
	}

	public void setCollectorPhone(String collectorPhone) {
		this.collectorPhone = collectorPhone;
	}

	@Column(name="collecotr_id")
	public String getCollectorID() {
		return collectorID;
	}

	public void setCollectorID(String collectorID) {
		this.collectorID = collectorID;
	}

	@Column(name="park_type")
	public Integer getParkType() {
		return parkType;
	}

	public void setParkType(Integer parkType) {
		this.parkType = parkType;
	}

	@Column(name="is_leave")
	public Integer getIsLeave() {
		return isLeave;
	}

	public void setIsLeave(Integer isLeave) {
		this.isLeave = isLeave;
	}
	
	@Column(name="total_money")
	public Integer getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Integer totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	@Transient
	public String formateParkTime(){
		try {
			if(parkTime != null){
				if(parkTime < 60){
					return parkTime + "分钟";
				}
				else{
					return parkTime / 60 + "小时" + parkTime % 60 + "分钟";
				}
			}
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Transient
	public String formatOutTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(outTime != null)
			return sdf.format(outTime);
		return "";
	}
	
	@Transient
	public String formatePayMoney(){
		try {
			if(payMoney != null){
				if(payMoney < 10){
					return "0.0" + payMoney + "元";
				}
				else if (payMoney < 100){
					return "0." + payMoney + "元";
				}
				else{
					return payMoney / 100 +  "." + payMoney % 100 + "元";
				}
			}
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Transient
	public String formatTotalMoney(){
		try {
			if(totalMoney != null){
				if(totalMoney < 10){
					return "0.0" + totalMoney + "元";
				}
				else if (totalMoney < 100){
					return "0." + totalMoney + "元";
				}
				else{
					return totalMoney / 100 +  "." + totalMoney % 100 + "元";
				}
			}
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Transient
	public String formateTotalMoney(){
		try {
			if(totalMoney != null){
				if(totalMoney < 10){
					return "0.0" + totalMoney + "元";
				}
				else if (totalMoney < 100){
					return "0." + totalMoney + "元";
				}
				else{
					return totalMoney / 100 +  "." + totalMoney % 100 + "元";
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
