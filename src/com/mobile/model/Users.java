package com.mobile.model;


import com.system.utils.Configuration;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="lq_users")
public class Users implements java.io.Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = -1687120445237355770L;
	//用户
	private String usercode;
	//微信通讯录ID，学号、职工号
	private String userid;
	//用户类型，1学生，2教师
	private Integer userType;
	//用户密码
	private String password;
	//用户昵称
	private String nickname;
	//用户头像
	private String avatar;
	//
	private String resident;
	//注册时间
	private Timestamp residentTime;
	
	private String signature;
	//性别
	private Integer gender;
	//生日
	private Date birth;
	//手机
	private String mobile;
	//qq号码
	private String qq;
	//邮箱
	private String email;
	//是否公开信息
	private Integer isOpen;
	//积分
	private Integer points;
	//1=已关注，2=已禁用，4=未关注
	private Integer status;
	private String openid;
	//微信号
	private String weixinid;
	//职位信息
	private String position;
	//备注
	private String remark;
	//常用设备标识符
	private String commonDevice;
	//新设备标识符
	private String newDevice;
	//新设备登录次数
	private Integer newCount;
	//发送的验证码
	private String captcha;
	//是否已绑定
	private Integer isAuth;
	// 最后定位时的经度
	private double longitude;
	//最后定位时的纬度
	private double latitude;
	//最后定位时间
	private Date locationTime;
	//用户审批ID审批
	private String auditUser_id;
	private String orgCode;
	@Id
	@GeneratedValue(generator = "myIdGenerator")
	@GenericGenerator(name = "myIdGenerator", strategy = "assigned")
	@Column(name="usercode", unique=true, nullable=false, length=100)
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	@Column(name="user_type")
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	@Column(name="password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="nickname")
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	@Column(name="resident")
	public String getResident() {
		return resident;
	}
	public void setResident(String resident) {
		this.resident = resident;
	}
	@Column(name="resident_time")
	public Timestamp getResidentTime() {
		return residentTime;
	}
	public void setResidentTime(Timestamp residentTime) {
		this.residentTime = residentTime;
	}
	@Column(name="signature")
	public String getSignature() {
		return signature;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	@Column(name="birth")
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	@Column(name="mobile")
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Column(name="qq")
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	@Column(name="is_open")
	public Integer getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}
	@Column(name="points")
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	@Column(name="userid")
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	@Column(name="avatar")
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	@Column(name="gender")
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	@Column(name="email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name="openid")
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	@Column(name="weixinid")
	public String getWeixinid() {
		return weixinid;
	}
	public void setWeixinid(String weixinid) {
		this.weixinid = weixinid;
	}
	@Column(name="position")
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "common_device")
	public String getCommonDevice() {
		return commonDevice;
	}
	public void setCommonDevice(String commonDevice) {
		this.commonDevice = commonDevice;
	}
	@Column(name = "new_device")
	public String getNewDevice() {
		return newDevice;
	}
	public void setNewDevice(String newDevice) {
		this.newDevice = newDevice;
	}
	@Column(name = "new_count")
	public Integer getNewCount() {
		return newCount;
	}
	public void setNewCount(Integer newCount) {
		this.newCount = newCount;
	}
	@Column(name = "captcha")
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	@Column(name = "is_auth")
	public Integer getIsAuth() {
		return isAuth;
	}
	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}
	@Column(name = "longitude")
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	@Column(name = "latitude")
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	@Column(name = "location_time")
	public Date getLocationTime() {
		return locationTime;
	}

	@Column(name = "org_code")
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public void setLocationTime(Date locationTime) {
		this.locationTime = locationTime;
	}
	@Transient
	public String getAuditUser_id() {
		return auditUser_id;
	}
	public void setAuditUser_id(String auditUserId) {
		auditUser_id = auditUserId;
	}
	@Transient
	public String getPhotoPath() {
		if(checkExistPhoto(this.userid)){
			return "photo/" + this.userid + ".jpg";
		}
		else if(this.gender == 2){
			return "sign/img/female.jpg";
		}
		else{
			return "sign/img/male.jpg";
		}
	}

	/**
	 * 检查是否有照片
	 * @param usercode
	 * @return
	 */
	public boolean checkExistPhoto(String usercode){
		boolean isExist = true;
		String filePath = Configuration.getInstance().getValue("photoPath") + usercode + ".jpg";
		File photo = new File(filePath);
		if(!photo.exists()){
			isExist = false;
		}
		return isExist;
	}
	
	
}