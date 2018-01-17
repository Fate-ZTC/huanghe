package com.parkbobo.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="lq_car_import_log")
@SequenceGenerator(name = "generator", sequenceName = "lq_car_import_log_import_logid_seq", allocationSize = 1)
public class CarImportLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 进口记录id
	 * */
	private Long importLogid;
	/**
	 * 出口记录ID
	 * */
	private CarExitLog carExitLog;
	/**
	 * 停车场id
	 * */
	private Long carparkid;
	/**
	 * 进出口标志 1：进口 0 出口
	 * */
	private Integer isImport;
	/**
	 * 进出入口门编号，不能为空
	 * */
	private Integer gateId;
	/**
	 * 通道号码，对于每一门的出入口进行编号
	 * */
	private Integer channelId;
	/**
	 * 车辆记录ID号码，不能玩为空，唯一标记一条
	 * */
	private Long dzLogid;
	/**
	 * 进入时间
	 * */
	private Timestamp enterTime;
	/**
	 * 车牌号码
	 * */
	private String plateNo;
	/**
	 * 车牌置信度
	 * */
	private Integer npCredit;
	/**
	 * 车辆如果刷卡进入则记录卡号
	 * */
	private String cardNo;
	/**
	 * 通道身份认证类型
	 * */
	private String identityType;
	/**
	 * 操作员代码
	 * */
	private String usercode;
	/**
	 * 车辆进入时候的场景图片
	 * */
	private String capImage;
	/**
	 * 备注
	 * */
	private String remarks;
	/**
	 * 状态
	 * */
	private Integer status;
	/**
	 * 是否校正通行
	 * */
	private String npCorrected;
	/**
	 * 是否注册车
	 * */
	private String regType;
	/**
	 * 出口是否匹配
	 * */
	private String matchedLeave;
	/**
	 * 通关状态(是否验证通过并放行)
	 * */
	private Integer passStatus;
	/**
	 * 用户类型，主要包括月租或者临停用户
	 * */
	private String customType;
	/**
	 * 车牌颜色
	 * */
	private Integer plateColor;
	/**
	 * 传输标志
	 * */
	private Integer transmittedFlag;
	/**
	 * 是否确认入场
	 * */
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
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generator")
    @Column(name="import_logid", unique=true, nullable=false)
	public Long getImportLogid() {
		return importLogid;
	}
	public void setImportLogid(Long importLogid) {
		this.importLogid = importLogid;
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
	 @Column(name="gate_id")
	public Integer getGateId() {
		return gateId;
	}
	public void setGateId(Integer gateId) {
		this.gateId = gateId;
	}
	 @Column(name="channel_id")
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	 @Column(name="dz_logid")
	public Long getDzLogid() {
		return dzLogid;
	}
	public void setDzLogid(Long dzLogid) {
		this.dzLogid = dzLogid;
	}
	 @Column(name="enter_time")
	public Timestamp getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(Timestamp enterTime) {
		this.enterTime = enterTime;
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
	 @Column(name="usercode")
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	 @Column(name="cap_image")
	public String getCapImage() {
		return capImage;
	}
	public void setCapImage(String capImage) {
		this.capImage = capImage;
	}
	 @Column(name="remarks")
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	 @Column(name="matched_leave")
	public String getMatchedLeave() {
		return matchedLeave;
	}
	public void setMatchedLeave(String matchedLeave) {
		this.matchedLeave = matchedLeave;
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
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="exit_logid")
	public CarExitLog getCarExitLog() {
		return carExitLog;
	}
	public void setCarExitLog(CarExitLog carExitLog) {
		this.carExitLog = carExitLog;
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
