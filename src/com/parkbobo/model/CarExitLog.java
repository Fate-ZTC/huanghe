package com.parkbobo.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="lq_car_exit_log")
public class CarExitLog implements Serializable{
	/**
	 * 车辆出口记录
	 */
	private static final long serialVersionUID = -2294418657128825299L;
	/**
	 * 出口记录
	 * */
	private String exitLogid;
	/**
	 * 停车场ID
	 * */
	private Long carparkid;
	/**
	 * 是否为进口：is_import,0:否； 1是
	 * */
	private Integer isImport;
	/**
	 * 通道号码，对于每一门的出入口进行编号
	 * */
	private Integer channelId;
	/**
	 * 车辆离开记录ID号码，不能玩为空，唯一标记一条主键:d
	 * */
	private Long dzeLogid;
	/**
	 * 离开时间
	 * */
	private Timestamp leaveTime;
	/**
	 * 车牌号码
	 * */
	private String plateNo;
	/**
	 * 车牌置信度
	 * */
	private Integer npCredit;
	/**
	 * 卡号
	 * */
	private String cardNo;
	/**
	 * 通道身份认证类型
	 * */
	private String identityType;
	/**
	 * 匹配的门ID编号
	 * */
	private Integer matchGateId;
	/**
	 * 匹配的对应门的通道ID号
	 * */
	private Integer matchChannelId;
	/**
	 * 匹配的用户操作码
	 * */
	private String matchUsercode;
	/**
	 * 入口匹配时间
	 * */
	private Timestamp matchEnterTime;
	/**
	 * 匹配车牌号
	 * */
	private String matchPlateNo;
	/**
	 * 匹配的卡号
	 * */
	private String matchCardNo;
	/**
	 * 
	 * 匹配通道身份认证类型
	 * */
	private String entifity_type;
	/**
	 * 备注
	 * */
	private String remarks;
	/**
	 * 是否匹配
	 * */
	private Integer flagMatched;
	/**
	 *  出口车牌场景图片
	 * */
	private String capImage;
	/**
	 * 车辆记录ID号码，不能玩为空，唯一标记一条
	 * */
	private Long enterid;
	/**
	 * 门编号
	 * */
	private Integer gateId;
	/**
	 * 用户操作码
	 * */
	private String usercode;
	/**
	 * 状态
	 * */
	private Integer status;
	/**
	 * 是否校正
	 * */
	private String npCorrected;
	/**
	 * 是否注册车
	 * */
	private String regType;
	/**
	 * 通行状态
	 * */
	private Integer passStatus;
	/**
	 * 用户类型，通进入记录表
	 * */
	private String customType;
	/**
	 * 车牌颜色
	 * */
	private Integer plateColor;
	/**
	 * 传输标志，同进入记录表
	 * */
	private Integer transmittedFlag;
	private Integer isMatchOrder;
	/**
	 * 车身颜色
	 * */
	private Integer carBodyColor;
	/**
	 * 车卡号码（平台中添加的车卡号）
	 * */
	private Integer parkingType;
	/**
	 * 车辆类型
	 * */
	private Integer carType;
	/**
	 * 车定位上停车抓拍图片
	 * */
	private String berthCarImgPath;
	/**
	 * 车位号
	 * */
	private String berthNum;
	/**
	 * 车牌图片路径（外网能访问的路径）
	 * */
	private String carNumImgPath;
	/**
	 * 刷卡时间（可为空）
	 * */
	private Timestamp creditCardTime;
	/**
	 * 过车类型（0-正常过车，1-报警过车）
	 * */
	private Integer througType;
	/**
	 * 放行方式
	 * */
	private Integer dischargedype;

	/**
	 * 操作时间
	 * */
	private Timestamp operateTime;
	
	@Id 
	@Column(name="exit_logid", unique=true, nullable=false)
	public String getExitLogid() {
		return exitLogid;
	}
	public void setExitLogid(String exitLogid) {
		this.exitLogid = exitLogid;
	}
	@Column(name="carparkid")
	public Long getCarparkid() {
		return carparkid;
	}
	public void setCarparkid(Long carparkid) {
		this.carparkid = carparkid;
	}
	@Column(name="is_import")
	public Integer getIsImport() {
		return isImport;
	}
	public void setIsImport(Integer isImport) {
		this.isImport = isImport;
	}
	@Column(name="channel_id")
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	@Column(name="dz_elogid")
	public Long getDzeLogid() {
		return dzeLogid;
	}
	public void setDzeLogid(Long dzeLogid) {
		this.dzeLogid = dzeLogid;
	}
	@Column(name="leave_time")
	public Timestamp getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(Timestamp leaveTime) {
		this.leaveTime = leaveTime;
	}
	@Column(name="plate_no")
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	@Column(name="np_credit")
	public Integer getNpCredit() {
		return npCredit;
	}
	public void setNpCredit(Integer npCredit) {
		this.npCredit = npCredit;
	}
	@Column(name="card_no")
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	@Column(name="identity_type")
	public String getIdentityType() {
		return identityType;
	}
	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}
	@Column(name="match_gate_id")
	public Integer getMatchGateId() {
		return matchGateId;
	}
	public void setMatchGateId(Integer matchGateId) {
		this.matchGateId = matchGateId;
	}
	@Column(name="match_channel_id")
	public Integer getMatchChannelId() {
		return matchChannelId;
	}
	public void setMatchChannelId(Integer matchChannelId) {
		this.matchChannelId = matchChannelId;
	}
	@Column(name="match_usercode")
	public String getMatchUsercode() {
		return matchUsercode;
	}
	public void setMatchUsercode(String matchUsercode) {
		this.matchUsercode = matchUsercode;
	}
	@Column(name="match_enter_time")
	public Timestamp getMatchEnterTime() {
		return matchEnterTime;
	}
	public void setMatchEnterTime(Timestamp matchEnterTime) {
		this.matchEnterTime = matchEnterTime;
	}
	@Column(name="match_plate_no")
	public String getMatchPlateNo() {
		return matchPlateNo;
	}
	public void setMatchPlateNo(String matchPlateNo) {
		this.matchPlateNo = matchPlateNo;
	}
	@Column(name="match_card_no")
	public String getMatchCardNo() {
		return matchCardNo;
	}
	public void setMatchCardNo(String matchCardNo) {
		this.matchCardNo = matchCardNo;
	}
	@Column(name="entifity_type")
	public String getEntifity_type() {
		return entifity_type;
	}
	public void setEntifity_type(String entifityType) {
		entifity_type = entifityType;
	}
	@Column(name="remarks")
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Column(name="flag_matched")
	public Integer getFlagMatched() {
		return flagMatched;
	}
	public void setFlagMatched(Integer flagMatched) {
		this.flagMatched = flagMatched;
	}
	@Column(name="cap_image")
	public String getCapImage() {
		return capImage;
	}
	public void setCapImage(String capImage) {
		this.capImage = capImage;
	}
	@Column(name="enterid")
	public Long getEnterid() {
		return enterid;
	}
	public void setEnterid(Long enterid) {
		this.enterid = enterid;
	}
	@Column(name="gate_id")
	public Integer getGateId() {
		return gateId;
	}
	public void setGateId(Integer gateId) {
		this.gateId = gateId;
	}
	@Column(name="usercode")
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name="np_corrected")
	public String getNpCorrected() {
		return npCorrected;
	}
	public void setNpCorrected(String npCorrected) {
		this.npCorrected = npCorrected;
	}
	@Column(name="reg_type")
	public String getRegType() {
		return regType;
	}
	public void setRegType(String regType) {
		this.regType = regType;
	}
	@Column(name="pass_status")
	public Integer getPassStatus() {
		return passStatus;
	}
	public void setPassStatus(Integer passStatus) {
		this.passStatus = passStatus;
	}
	@Column(name="custom_type")
	public String getCustomType() {
		return customType;
	}
	public void setCustomType(String customType) {
		this.customType = customType;
	}
	@Column(name="plate_color")
	public Integer getPlateColor() {
		return plateColor;
	}
	public void setPlateColor(Integer plateColor) {
		this.plateColor = plateColor;
	}
	@Column(name="transmitted_flag")
	public Integer getTransmittedFlag() {
		return transmittedFlag;
	}
	public void setTransmittedFlag(Integer transmittedFlag) {
		this.transmittedFlag = transmittedFlag;
	}
	@Column(name="is_match_order")
	public Integer getIsMatchOrder() {
		return isMatchOrder;
	}
	public void setIsMatchOrder(Integer isMatchOrder) {
		this.isMatchOrder = isMatchOrder;
	}
	
	@Column(name="car_body_colors")
	public Integer getCarBodyColor() {
		return carBodyColor;
	}
	public void setCarBodyColor(Integer carBodyColor) {
		this.carBodyColor = carBodyColor;
	}
	@Column(name="parking_type")
	public Integer getParkingType() {
		return parkingType;
	}
	public void setParkingType(Integer parkingType) {
		this.parkingType = parkingType;
	}
	@Column(name="car_type")
	public Integer getCarType() {
		return carType;
	}
	public void setCarType(Integer carType) {
		this.carType = carType;
	}
	@Column(name="berth_car_img_path")
	public String getBerthCarImgPath() {
		return berthCarImgPath;
	}
	public void setBerthCarImgPath(String berthCarImgPath) {
		this.berthCarImgPath = berthCarImgPath;
	}
	@Column(name="berth_num")
	public String getBerthNum() {
		return berthNum;
	}
	public void setBerthNum(String berthNum) {
		this.berthNum = berthNum;
	}
	@Column(name="car_num_img_path")
	public String getCarNumImgPath() {
		return carNumImgPath;
	}
	public void setCarNumImgPath(String carNumImgPath) {
		this.carNumImgPath = carNumImgPath;
	}
	@Column(name="credit_card_time")
	public Timestamp getCreditCardTime() {
		return creditCardTime;
	}
	public void setCreditCardTime(Timestamp creditCardTime) {
		this.creditCardTime = creditCardTime;
	}
	@Column(name="throug_type")
	public Integer getThrougType() {
		return througType;
	}
	public void setThrougType(Integer througType) {
		this.througType = througType;
	}
	@Column(name="discharged_type")
	public Integer getDischargedype() {
		return dischargedype;
	}
	public void setDischargedype(Integer dischargedype) {
		this.dischargedype = dischargedype;
	}
	@Column(name="operate_time")
	public Timestamp getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}
}
