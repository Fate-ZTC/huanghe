package com.parkbobo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 巡更签到-点位信息
 * @version 1.0
 * @author RY
 * @since 2018-7-6 17:45:37
 */

@Entity
@Table(name="patrol_sign_point_info")
@SequenceGenerator(name="generator", sequenceName="patrol_sign_point_info_point_id_seq", allocationSize = 1)
public class PatrolSignPointInfo implements Serializable {


	private static final long serialVersionUID = 189970556253691819L;
	/**
	 * 点位ID
	 */
	private Integer pointId;
	/**
	 * 巡更区域
	 */
	private PatrolRegion patrolRegion;
	/**
	 * 点位名称
	 */
	private String pointName;
	/**
	 * 点位经度
	 */
	private Double lng;
	/**
	 * 点位纬度
	 */
	private Double lat;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	@Id
	@Column(name = "point_id", unique = true, nullable = false)
	@GeneratedValue(generator = "generator", strategy = GenerationType.AUTO)
	public Integer getPointId() {
		return pointId;
	}

	public void setPointId(Integer pointId) {
		this.pointId = pointId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "id" )
	public PatrolRegion getPatrolRegion() {
		return patrolRegion;
	}

	public void setPatrolRegion(PatrolRegion patrolRegion) {
		this.patrolRegion = patrolRegion;
	}

	@Column(name = "point_name")
	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	@Column(name = "lng")
	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	@Column(name = "lat")
	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	@Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
