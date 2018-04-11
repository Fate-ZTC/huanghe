package com.parkbobo.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.alibaba.fastjson.annotation.JSONField;
import com.vividsolutions.jts.geom.Geometry;
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
	 * 颜色16进制值
     */
	private String color;


	/**
	 * 空间几何信息
	 */
	@JSONField(serialize=false)
	private Geometry regionLocation;


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
	public Geometry getRegionLocation() {
		return regionLocation;
	}

	public void setRegionLocation(Geometry regionLocation) {
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

	@Column(name = "color")
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		if(color == null) {
			//这只默认颜色
			this.color = "#02a6cf";
		}else {
			this.color = color;
		}
	}
}
