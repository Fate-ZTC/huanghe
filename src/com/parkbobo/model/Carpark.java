package com.parkbobo.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@Entity
@Table(name = "lq_carpark")
@SequenceGenerator(name="generator", sequenceName="lq_carpark_carparkid_seq", allocationSize = 1)
public class Carpark
  implements Serializable
{
  private static final long serialVersionUID = -2489073582608914038L;
  private Long carparkid;
  private String name;
  private String picarr;
  private Integer totalBerth;
  private Integer enableBerth;
  private String province;
  private String city;
  private String county;
  private String address;
  private Date opentime;
  private Date closetime;
  private String feeRates;
  private Integer beforeMins;
  private Double beforePrice;
  private Integer afterMins;
  private Double afterPrice;
  private Short maptype;
  private Short parktype;
  private Integer enstopnum;
  private Short isAllowed;
  private Integer showLevel;
  private Double longitude;
  private Double latitude;
  private String rightTopLon;
  private String rightTopLat;
  private String leftBottomLon;
  private String leftBottomLat;
  private Short isAuthentication;
  private Long posttime;
  private String username;
  private Integer isPartner;
  private String password;
  private String managerPassword;
  private Integer ownerPercent;
  private Integer propertyPercent;
  private Integer isBarrierGate;
  private Short isSystemPrice;
  private Integer systemBeforeMins;
  private Double systemBeforePrice;
  private Integer systemAfterMins;
  private Double systemAfterPrice;
  private Double systemViolatePrice;
  private String thirdId;
  private Integer appointmentMins;
  private String memo;
  private Short amapCarparkType;
  private String zoneid;
  
  @Id
  @Column(name="carparkid", unique = true, nullable = false)
  @GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
  public Long getCarparkid()
  {
    return this.carparkid;
  }

  public void setCarparkid(Long carparkid) {
    this.carparkid = carparkid;
  }
  @Column(name="name")
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name="picarr")
  public String getPicarr()
  {
    return this.picarr;
  }

  public void setPicarr(String picarr) {
    this.picarr = picarr;
  }

  @Column(name="total_berth")
  public Integer getTotalBerth()
  {
    return this.totalBerth;
  }

  public void setTotalBerth(Integer totalBerth) {
    this.totalBerth = totalBerth;
  }

  @Column(name="enable_berth")
  public Integer getEnableBerth()
  {
    return this.enableBerth;
  }

  public void setEnableBerth(Integer enableBerth) {
    this.enableBerth = enableBerth;
  }

  @Column(name="province", length=50)
  public String getProvince()
  {
    return this.province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  @Column(name="city", length=50)
  public String getCity()
  {
    return this.city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Column(name="county", length=50)
  public String getCounty()
  {
    return this.county;
  }

  public void setCounty(String county) {
    this.county = county;
  }

  @Column(name="address")
  public String getAddress()
  {
    return this.address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Column(name="opentime", length=29)
  public Date getOpentime()
  {
    return this.opentime;
  }

  public void setOpentime(Date opentime) {
    this.opentime = opentime;
  }

  @Column(name="closetime", length=29)
  public Date getClosetime()
  {
    return this.closetime;
  }

  public void setClosetime(Date closetime) {
    this.closetime = closetime;
  }

  @Column(name="fee_rates")
  public String getFeeRates()
  {
    return this.feeRates;
  }

  public void setFeeRates(String feeRates) {
    this.feeRates = feeRates;
  }

  @Column(name="before_mins")
  public Integer getBeforeMins()
  {
    return this.beforeMins;
  }

  public void setBeforeMins(Integer beforeMins) {
    this.beforeMins = beforeMins;
  }

  @Column(name="before_price", precision=12)
  public Double getBeforePrice()
  {
    return this.beforePrice;
  }

  public void setBeforePrice(Double beforePrice) {
    this.beforePrice = beforePrice;
  }

  @Column(name="after_mins")
  public Integer getAfterMins()
  {
    return this.afterMins;
  }

  public void setAfterMins(Integer afterMins) {
    this.afterMins = afterMins;
  }

  @Column(name="after_price", precision=12)
  public Double getAfterPrice()
  {
    return this.afterPrice;
  }

  public void setAfterPrice(Double afterPrice) {
    this.afterPrice = afterPrice;
  }

  @Column(name="maptype")
  public Short getMaptype()
  {
    return this.maptype;
  }

  public void setMaptype(Short maptype) {
    this.maptype = maptype;
  }

  @Column(name="parktype")
  public Short getParktype()
  {
    return this.parktype;
  }

  public void setParktype(Short parktype) {
    this.parktype = parktype;
  }

  @Column(name="enstopnum")
  public Integer getEnstopnum()
  {
    return this.enstopnum;
  }

  public void setEnstopnum(Integer enstopnum) {
    this.enstopnum = enstopnum;
  }

  @Column(name="is_allowed")
  public Short getIsAllowed()
  {
    return this.isAllowed;
  }

  public void setIsAllowed(Short isAllowed) {
    this.isAllowed = isAllowed;
  }

  @Column(name="show_level")
  public Integer getShowLevel()
  {
    return this.showLevel;
  }

  public void setShowLevel(Integer showLevel) {
    this.showLevel = showLevel;
  }

  @Column(name="longitude", precision=30, scale=20)
  public Double getLongitude()
  {
    return this.longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  @Column(name="latitude", precision=30, scale=20)
  public Double getLatitude()
  {
    return this.latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  @Column(name="right_top_lon", length=200)
  public String getRightTopLon()
  {
    return this.rightTopLon;
  }

  public void setRightTopLon(String rightTopLon) {
    this.rightTopLon = rightTopLon;
  }

  @Column(name="right_top_lat", length=200)
  public String getRightTopLat()
  {
    return this.rightTopLat;
  }

  public void setRightTopLat(String rightTopLat) {
    this.rightTopLat = rightTopLat;
  }

  @Column(name="left_bottom_lon", length=200)
  public String getLeftBottomLon()
  {
    return this.leftBottomLon;
  }

  public void setLeftBottomLon(String leftBottomLon) {
    this.leftBottomLon = leftBottomLon;
  }

  @Column(name="left_bottom_lat", length=200)
  public String getLeftBottomLat()
  {
    return this.leftBottomLat;
  }

  public void setLeftBottomLat(String leftBottomLat) {
    this.leftBottomLat = leftBottomLat;
  }

  @Column(name="is_authentication")
  public Short getIsAuthentication()
  {
    return this.isAuthentication;
  }

  public void setIsAuthentication(Short isAuthentication) {
    this.isAuthentication = isAuthentication;
  }

  @Column(name="memo")
  public String getMemo()
  {
    return this.memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  @Column(name="amap_carpark_type")
  public Short getAmapCarparkType() {
    return this.amapCarparkType;
  }

  public void setAmapCarparkType(Short amapCarparkType) {
    this.amapCarparkType = amapCarparkType;
  }

  @Column(name="posttime")
  public Long getPosttime()
  {
    return this.posttime;
  }

  public void setPosttime(Long posttime) {
    this.posttime = posttime;
  }
  @Column(name="username")
  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Column(name="is_partner")
  public Integer getIsPartner() {
    return this.isPartner;
  }

  public void setIsPartner(Integer isPartner) {
    this.isPartner = isPartner;
  }
  @Column(name="password")
  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
  @Column(name="manager_password")
  public String getManagerPassword() {
    return this.managerPassword;
  }

  public void setManagerPassword(String managerPassword) {
    this.managerPassword = managerPassword;
  }
  @Column(name="owner_percent")
  public Integer getOwnerPercent() {
    return this.ownerPercent;
  }

  public void setOwnerPercent(Integer ownerPercent) {
    this.ownerPercent = ownerPercent;
  }
  @Column(name="property_percent")
  public Integer getPropertyPercent() {
    return this.propertyPercent;
  }

  public void setPropertyPercent(Integer propertyPercent) {
    this.propertyPercent = propertyPercent;
  }

  @Column(name="is_barrier_gate")
  public Integer getIsBarrierGate() {
    return this.isBarrierGate;
  }

  public void setIsBarrierGate(Integer isBarrierGate) {
    this.isBarrierGate = isBarrierGate;
  }
  @Column(name="is_system_price")
  public Short getIsSystemPrice() {
    return this.isSystemPrice;
  }

  public void setIsSystemPrice(Short isSystemPrice) {
    this.isSystemPrice = isSystemPrice;
  }
  @Column(name="system_before_mins")
  public Integer getSystemBeforeMins() {
    return this.systemBeforeMins;
  }

  public void setSystemBeforeMins(Integer systemBeforeMins) {
    this.systemBeforeMins = systemBeforeMins;
  }
  @Column(name="system_before_price")
  public Double getSystemBeforePrice() {
    return this.systemBeforePrice;
  }

  public void setSystemBeforePrice(Double systemBeforePrice) {
    this.systemBeforePrice = systemBeforePrice;
  }
  @Column(name="system_after_mins")
  public Integer getSystemAfterMins() {
    return this.systemAfterMins;
  }

  public void setSystemAfterMins(Integer systemAfterMins) {
    this.systemAfterMins = systemAfterMins;
  }
  @Column(name="system_after_price")
  public Double getSystemAfterPrice() {
    return this.systemAfterPrice;
  }

  public void setSystemAfterPrice(Double systemAfterPrice) {
    this.systemAfterPrice = systemAfterPrice;
  }
  @Column(name="system_violate_price")
  public Double getSystemViolatePrice() {
    return this.systemViolatePrice;
  }

  public void setSystemViolatePrice(Double systemViolatePrice) {
    this.systemViolatePrice = systemViolatePrice;
  }

  @Column(name="third_id")
  public String getThirdId() {
	  return thirdId;
  }
  
  public void setThirdId(String thirdId) {
	  this.thirdId = thirdId;
  }
 

  @Column(name="appointment_mins")
  public Integer getAppointmentMins() {
    return this.appointmentMins;
  }


  public void setAppointmentMins(Integer appointmentMins) {
    this.appointmentMins = appointmentMins;
  }

	public String getZoneid() {
		return zoneid;
	}
	
	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}
}