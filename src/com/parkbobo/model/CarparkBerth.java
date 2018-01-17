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

/**
 * @version 1.1
 * @author RY
 * @since 2017-7-11 14:31:48
 * 增加楼层字段
 *
 */

@Entity
@Table(name="lq_carpark_berth")
@SequenceGenerator(name = "generator", sequenceName = "lq_carpark_berth_berth_id_seq", allocationSize = 1)
public class CarparkBerth implements Serializable{

	/**
	 * 车位数据表
	 */
	private static final long serialVersionUID = 763532430278849764L;
	private Long berthId;//车位ID
	private Long carparkid;//停车场ID
	private String spotId;//泊位ID
	private Integer direct;//出入方向
	private Date passTime;//过车时间
	private String carPlate;//车牌号
	private Integer plateColor;//车牌颜色
	private Integer plateType;//车牌类型
	private String carType;//车辆类型
	private Integer parkType;//停车类型
	private String imageUrl;//泊车图片
	private String floorCode;//楼层编码
	@Id 
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generator")
    @Column(name="berth_id", unique=true, nullable=false)
	public Long getBerthId() {
		return berthId;
	}
	public void setBerthId(Long berthId) {
		this.berthId = berthId;
	}
	@Column(name="carparkid")
	public Long getCarparkid() {
		return carparkid;
	}
	public void setCarparkid(Long carparkid) {
		this.carparkid = carparkid;
	}
	@Column(name="spot_id")
	public String getSpotId() {
		return spotId;
	}
	public void setSpotId(String spotId) {
		this.spotId = spotId;
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
	@Column(name="car_plate")
	public String getCarPlate() {
		return carPlate;
	}
	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}
	@Column(name="plate_color")
	public Integer getPlateColor() {
		return plateColor;
	}
	public void setPlateColor(Integer plateColor) {
		this.plateColor = plateColor;
	}
	@Column(name="plate_type")
	public Integer getPlateType() {
		return plateType;
	}
	public void setPlateType(Integer plateType) {
		this.plateType = plateType;
	}
	@Column(name="car_type")
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	@Column(name="park_type")
	public Integer getParkType() {
		return parkType;
	}
	public void setParkType(Integer parkType) {
		this.parkType = parkType;
	}
	@Column(name="image_url")
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@Column(name="floor_code")
	public String getFloorCode() {
		return floorCode;
	}
	public void setFloorCode(String floorCode) {
		this.floorCode = floorCode;
	}
}
