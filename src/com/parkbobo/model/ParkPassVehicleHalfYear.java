package com.parkbobo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="lq_park_pass_vehicle_half_year")
public class ParkPassVehicleHalfYear implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6791817881891089184L;
	private String id;
	private Long carparkid;//停车点编号：carparkid
	private Integer gateid;//出入口内部编号：gateid
	private Integer direct;//出入方向：direct ，0 入场，1 出场
	private Date passTime;//过车时间：pass_time
	private String carplate;//车牌号：carplate
	private Integer platecolor;//车牌颜色
	private Integer platetype;//车牌类型：platetype
	private Integer cartype;//车辆类型：cartype
	private String cardno;//卡号：cardno
	private Integer parktype;//停车类型：parktype 1：临时车，2：包白天，3：包晚上，4：包全天
	private String imageurl;
	@Id 
	@Column(name="id", unique=true, nullable=false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="carparkid")
	public Long getCarparkid() {
		return carparkid;
	}
	public void setCarparkid(Long carparkid) {
		this.carparkid = carparkid;
	}
	@Column(name="gateid")
	public Integer getGateid() {
		return gateid;
	}
	public void setGateid(Integer gateid) {
		this.gateid = gateid;
	}
	@Column(name="direct")
	public Integer getDirect() {
		return direct;
	}
	public void setDirect(Integer direct) {
		this.direct = direct;
	}
	@Column(name="pass_time")
	public Date getPassTime() {
		return passTime;
	}
	public void setPassTime(Date passTime) {
		this.passTime = passTime;
	}
	@Column(name="carplate")
	public String getCarplate() {
		return carplate;
	}
	public void setCarplate(String carplate) {
		this.carplate = carplate;
	}
	@Column(name="platecolor")
	public Integer getPlatecolor() {
		return platecolor;
	}
	public void setPlatecolor(Integer platecolor) {
		this.platecolor = platecolor;
	}
	@Column(name="platetype")
	public Integer getPlatetype() {
		return platetype;
	}
	public void setPlatetype(Integer platetype) {
		this.platetype = platetype;
	}
	@Column(name="cartype")
	public Integer getCartype() {
		return cartype;
	}
	public void setCartype(Integer cartype) {
		this.cartype = cartype;
	}
	@Column(name="cardno")
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	@Column(name="parktype")
	public Integer getParktype() {
		return parktype;
	}
	public void setParktype(Integer parktype) {
		this.parktype = parktype;
	}
	@Column(name="imageurl")
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	

}
