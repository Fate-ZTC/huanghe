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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.alibaba.fastjson.annotation.JSONField;
import com.vividsolutions.jts.geom.MultiPolygon;

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
	 * 空间几何信息
	 */
	@JSONField(serialize=false)
	private MultiPolygon regionLocation;
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
	
	
}
