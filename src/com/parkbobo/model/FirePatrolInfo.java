package com.parkbobo.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="fire_patrol_info")
@SequenceGenerator(name="generator", sequenceName="fire_patrol_info_id_seq", allocationSize = 1)
public class FirePatrolInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7623324663655191769L;
	/**
	 * 异常信息id
	 */
	private Integer id;
	/**
	 * 用户区域
	 */
	private FirePatrolUser firePatrolUser;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 校区
	 */
	private Integer campusNum;
	/**
	 * 时间戳
	 */
	private Date timestamp;
	/**
	 * 异常状态  1正常 0异常
	 */
	private Integer patrolStatus;
	/**
	 * 异常码 格式(1,2,3,)
	 */
	private String exceptionTypes;
	/**
	 * 设备id
	 */
	private FireFightEquipment fireFightEquipment;
	/**
	 * 异常描述
	 */
	private String description;
	/**
	 * 是否是最新的一条 0 不是  1 是
	 */
	private Short isNewest;

	/**
	 * 楼层id
     */
	private String floorid;

	/**
	 * 经度
     */
	private double lon;

	/**
	 * 纬度
     */
	private double lat;

	/**
	 * 巡查人员工号
     */
	private String jobNum;

	/***
	 * 设备名称
	 **/
	private String equipmentName;

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	/***
	 * 位置
	 **/
	private String locationName;


	@Id
	@Column(name="id",nullable=false,unique=true)
	@GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@OneToOne
	@JoinColumn(name="user_id")
	public FirePatrolUser getFirePatrolUser() {
		return firePatrolUser;
	}
	public void setFirePatrolUser(FirePatrolUser firePatrolUser) {
		this.firePatrolUser = firePatrolUser;
	}
	@Column(name="username")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name="campus_num")
	public Integer getCampusNum() {
		return campusNum;
	}
	public void setCampusNum(Integer campusNum) {
		this.campusNum = campusNum;
	}
	@Column(name="timestamp")
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	@Column(name="patrol_status")
	public Integer getPatrolStatus() {
		return patrolStatus;
	}
	public void setPatrolStatus(Integer patrolStatus) {
		this.patrolStatus = patrolStatus;
	}
	@Column(name="exception_types")
	public String getExceptionTypes() {
		return exceptionTypes;
	}
	public void setExceptionTypes(String exceptionTypes) {
		this.exceptionTypes = exceptionTypes;
	}
	@OneToOne
	@JoinColumn(name="equipment_id")
	public FireFightEquipment getFireFightEquipment() {
		return fireFightEquipment;
	}
	public void setFireFightEquipment(FireFightEquipment fireFightEquipment) {
		this.fireFightEquipment = fireFightEquipment;
	}
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name="is_newest")
	public Short getIsNewest() {
		return isNewest;
	}
	public void setIsNewest(Short isNewest) {
		this.isNewest = isNewest;
	}


	@Column(name = "floorid")
	public String getFloorid() {
		return floorid;
	}

	public void setFloorid(String floorid) {
		this.floorid = floorid;
	}

	@Column(name = "lon")
	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	@Column(name = "lat")
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	@Column(name = "job_num")
	public String getJobNum() {
		return jobNum;
	}

	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}


	/**
	 * 用于转换对象
	 * @param map
	 * @return
	 */
	public static FirePatrolInfo toObject(Map<String,Object> map) {
		FirePatrolInfo firePatrolInfo = new FirePatrolInfo();
		firePatrolInfo.setId((Integer) map.get("id"));
		String username = map.get("username").toString();
		if(username != null) {
			firePatrolInfo.setUserName(username);
		}
		if(map.get("timestamp") != null && !"".equals(map.get("timestamp"))) {
			//这里进行时间转换
			String dateStr = map.get("timestamp").toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				if(dateStr.contains(".")) {
					//将格式为:2018-08-09 10:00:00.123进行切割
					String[] split = dateStr.split("\\.");
					if(split != null && split.length > 0) {
						dateStr = split[0];
					}
				}
				Date timestamp = format.parse(dateStr);
				firePatrolInfo.setTimestamp(timestamp);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(map.get("lon") != null) {
			firePatrolInfo.setLon((Double) map.get("lon"));
		}
		if(map.get("lat") != null) {
			firePatrolInfo.setLat((double) map.get("lat"));
		}

		Integer equipmentId = Integer.valueOf(map.get("equipment_id").toString());
		if(equipmentId != null && !"".equals(equipmentId)) {
			FireFightEquipment fireFightEquipment = new FireFightEquipment();
			fireFightEquipment.setName(map.get("name").toString());
			fireFightEquipment.setId(equipmentId);
			firePatrolInfo.setFireFightEquipment(fireFightEquipment);
		}
		firePatrolInfo.setCampusNum((Integer) map.get("campus_num"));
		if(map.get("username") != null) {
			firePatrolInfo.setUserName((String) map.get("username"));
		}
		if(map.get("job_num") != null) {
			firePatrolInfo.setJobNum((String) map.get("job_num"));
		}
		if(map.get("floorid") != null) {
			firePatrolInfo.setFloorid((String) map.get("floorid"));
		}
		if(map.get("patrol_status") !=null) {
			firePatrolInfo.setPatrolStatus((Integer) map.get("patrol_status"));
		}
		firePatrolInfo.setLocationName(map.get("location_name").toString());
		return firePatrolInfo;
	}


	/**
	 * 将list中查询出来的数组转化成对象列表
	 * @param list
	 * @return
	 */
	public static List<FirePatrolInfo> toObjectList(List<Map<String,Object>> list) {
		List<FirePatrolInfo> firePatrolInfos = new ArrayList<>();
		if(list != null && list.size() > 0) {
			for(Map<String,Object> map:list) {
				FirePatrolInfo firePatrolInfo = toObject(map);
				if(firePatrolInfo != null) {
					firePatrolInfos.add(firePatrolInfo);
				}
			}

		}
		return firePatrolInfos;
	}
}
