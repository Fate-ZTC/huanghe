package com.parkbobo.utils.httpmodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 收费数据对象
 * @author RY
 * @version 1.0
 * @since 2017-6-27 10:23:23
 *
 */

public class VehicleCostModel implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6742290651230624060L;
	private Integer errorCode;//返回码
	private String message;//信息提示
	private String parkId;//停车点编号
	private String inTime;//入场时间
	private String outTime;//出场时间
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

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getParkId() {
		return parkId;
	}

	public void setParkId(String parkId) {
		this.parkId = parkId;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public Integer getParkTime() {
		return parkTime;
	}

	public void setParkTime(Integer parkTime) {
		this.parkTime = parkTime;
	}

	public String getCarPlate() {
		return carPlate;
	}

	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}

	public Integer getPlateType() {
		return plateType;
	}

	public void setPlateType(Integer plateType) {
		this.plateType = plateType;
	}

	public Integer getPlateColor() {
		return plateColor;
	}

	public void setPlateColor(Integer plateColor) {
		this.plateColor = plateColor;
	}

	public Integer getCarType() {
		return carType;
	}

	public void setCarType(Integer carType) {
		this.carType = carType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getInUnid() {
		return inUnid;
	}

	public void setInUnid(String inUnid) {
		this.inUnid = inUnid;
	}

	public String getOutUnid() {
		return outUnid;
	}

	public void setOutUnid(String outUnid) {
		this.outUnid = outUnid;
	}

	public Integer getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Integer payMoney) {
		this.payMoney = payMoney;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getBookUnid() {
		return bookUnid;
	}

	public void setBookUnid(String bookUnid) {
		this.bookUnid = bookUnid;
	}

	public Integer getNeedPay() {
		return needPay;
	}

	public void setNeedPay(Integer needPay) {
		this.needPay = needPay;
	}

	public Integer getGateId() {
		return gateId;
	}

	public void setGateId(Integer gateId) {
		this.gateId = gateId;
	}

	public String getCollectorCode() {
		return collectorCode;
	}

	public void setCollectorCode(String collectorCode) {
		this.collectorCode = collectorCode;
	}

	public String getCollectorName() {
		return collectorName;
	}

	public void setCollectorName(String collectorName) {
		this.collectorName = collectorName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCollectorPhone() {
		return collectorPhone;
	}

	public void setCollectorPhone(String collectorPhone) {
		this.collectorPhone = collectorPhone;
	}

	public String getCollectorID() {
		return collectorID;
	}

	public void setCollectorID(String collectorID) {
		this.collectorID = collectorID;
	}

	public Integer getParkType() {
		return parkType;
	}

	public void setParkType(Integer parkType) {
		this.parkType = parkType;
	}

	public Integer getIsLeave() {
		return isLeave;
	}

	public void setIsLeave(Integer isLeave) {
		this.isLeave = isLeave;
	}
	
	public Integer getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Integer totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Date formatInTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(inTime!=null){
				return sdf.parse(this.getInTime());				
			}
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Date formatOutTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(outTime!=null){
				return sdf.parse(this.getOutTime());				
			}
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
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

}
