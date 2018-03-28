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
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.alibaba.fastjson.annotation.JSONField;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.WKTWriter;

@Entity
@Table(name="patrol_region")
@SequenceGenerator(name="generator", sequenceName="patrol_region_id_seq", allocationSize = 1)
public class PatrolRegion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5738933554294542612L;
	/**
	 * 区域id
	 */
	private Integer id;
	/**
	 * 区域名称
	 */
	private String regionName;
	/**
	 * 校区编号
	 */
	private Integer campusNum;
	/**
	 * 创建时间
	 */
	private Date createtime;

	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;

	/**
	 * 空间几何信息
	 */
	@JSONField(serialize=false)
	private MultiPolygon regionLocation;


	/**
	 * 字符串类型的空间经纬度信息
	 */
	private String geometry;
	/**
	 * 面图元几何中心点
	 */
	private String geometryCentroid;
	/**
	 * 是否删除
	 */
	private Short isDel;
	@Id
	@Column(name="id",unique=true,nullable=false)
	@GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="region_name")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	@Column(name="campus_num")
	public Integer getCampusNum() {
		return campusNum;
	}

	public void setCampusNum(Integer campusNum) {
		this.campusNum = campusNum;
	}
	@Column(name="createtime")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	@Column(name="region_location")
	@Type(type="org.hibernatespatial.GeometryUserType",parameters ={
			@Parameter(name="dialect",value="postgis")
	})
	public MultiPolygon getRegionLocation() {
		return regionLocation;
	}

	public void setRegionLocation(MultiPolygon regionLocation) {
		this.regionLocation = regionLocation;
	}
	@Column(name="is_del")
	public Short getIsDel() {
		return isDel;
	}

	public void setIsDel(Short isDel) {
		this.isDel = isDel;
	}

	@Transient
	public String getGeometry() {
		WKTWriter wr = new WKTWriter();
		if(regionLocation!=null){
			geometry =  wr.write(this.regionLocation);
		}
		if(geometry != null) {
			if (geometry.length() > 0) {
				geometry = geometry.replaceAll(", ", ",");
			}
			return geometry;
		}
		return null;
	}

	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}

	@Transient
	public String getGeometryCentroid() {
		WKTWriter wr = new WKTWriter();
		if(regionLocation!=null){
			geometryCentroid = wr.write(this.regionLocation.getCentroid());
			geometryCentroid = geometryCentroid.substring(geometryCentroid.indexOf("(")+1, geometryCentroid.indexOf(")")).replace(" ", ",");
			return geometryCentroid;
		}
		return null;
	}
	@Column(name="last_update_time")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}



}
