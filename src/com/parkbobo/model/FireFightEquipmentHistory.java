package com.parkbobo.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;

@Entity
@Table(name="fire_fight_equipment_history")
@SequenceGenerator(name="generator", sequenceName="fire_fight_equipment_history_id_seq", allocationSize = 1)
public class FireFightEquipmentHistory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8256001129924164070L;
	
	/**
	 * id
	 */
	private Integer id;
	/**
	 * 设备原来的id
	 */
	private Integer oldId;
	/**
	 * 设备状态 1正常 0异常
	 */
	private Short status;
	/**
	 * 设备名
	 */
	private String name;
	/**
	 * 经度
	 */
	private double lon;
	/**
	 * 纬度
	 */
	private double lat;
	/**
	 * 巡查状态 0未检查 1已检查
	 */
	private Short checkStatus;
	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;
	/**
	 * 校区
	 */
	private Integer campusNum;
	/**
	 * 异常信息
	 */
	private Integer fpid;

	/**
	 * 巡查人姓名
     */
	private String userName;
	/**
	 * 巡查人工号
     */
	private String jobNum;

	/**
	 *  楼层id
     */
	private String floorid;


	/**
	 * 大楼id
	 */
	private String buildingCode;

	/**
	 * 巡查次数
	 */
	private int checkCount;

	/**
	 * 设备位置
	 */
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
	@Column(name="status")
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="lon")
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	@Column(name="lat")
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	@Column(name="check_status")
	public Short getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(Short checkStatus) {
		this.checkStatus = checkStatus;
	}
	@Column(name="last_update_time")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	@Column(name="campus_num")
	public Integer getCampusNum() {
		return campusNum;
	}
	public void setCampusNum(Integer campusNum) {
		this.campusNum = campusNum;
	}
	@Column(name="old_id")
	public Integer getOldId() {
		return oldId;
	}
	public void setOldId(Integer oldId) {
		this.oldId = oldId;
	}
	@Transient
	public Integer getFpid() {
		return fpid;
	}
	public void setFpid(Integer fpid) {
		this.fpid = fpid;
	}

	@Column(name = "username")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name = "job_num")
	public String getJobNum() {
		return jobNum;
	}

	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}

	@Column(name = "floorid")
	public String getFloorid() {
		return floorid;
	}

	public void setFloorid(String floorid) {
		this.floorid = floorid;
	}

	@Column(name = "building_code")
	public String getBuildingCode() {
		return buildingCode;
	}

	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}

	@Column(name = "check_count")
	public int getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(int checkCount) {
		this.checkCount = checkCount;
	}

	@Column(name = "location_name")
	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	/**
	 * 用于转换对象
	 * @param map
	 * @return
	 */
	public static FireFightEquipmentHistory toObject(Map<String,Object> map) {
		FireFightEquipmentHistory fireFightEquipmentHistory = new FireFightEquipmentHistory();
		fireFightEquipmentHistory.setId((Integer) map.get("id"));
		fireFightEquipmentHistory.setOldId((Integer) map.get("old_id"));

		String status = map.get("status").toString();
        if(status != null) {
            fireFightEquipmentHistory.setStatus(Short.parseShort(status));
        }
		if(map.get("last_update_time") != null && !"".equals(map.get("last_update_time"))) {
			//这里进行时间转换
			String dateStr = map.get("last_update_time").toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				if(dateStr.contains(".")) {
					//将格式为:2018-08-09 10:00:00.123进行切割
					String[] split = dateStr.split("\\.");
					if(split != null && split.length > 0) {
						dateStr = split[0];
					}
				}
				Date last_update_time = format.parse(dateStr);
				fireFightEquipmentHistory.setLastUpdateTime(last_update_time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(map.get("lon") != null) {
			fireFightEquipmentHistory.setLon((Double) map.get("lon"));
		}
		if(map.get("lat") != null) {
			fireFightEquipmentHistory.setLat((double) map.get("lat"));
		}

		String checkStatus = map.get("check_status").toString();
        if(checkStatus != null && !"".equals(checkStatus)) {
			fireFightEquipmentHistory.setCheckStatus(Short.parseShort(checkStatus));
		}

		fireFightEquipmentHistory.setCampusNum((Integer) map.get("campus_num"));
		if(map.get("username") != null) {
			fireFightEquipmentHistory.setUserName((String) map.get("username"));
		}
		if(map.get("job_num") != null) {
			fireFightEquipmentHistory.setJobNum((String) map.get("job_num"));
		}
		if(map.get("floorid") != null) {
			fireFightEquipmentHistory.setFloorid((String) map.get("floorid"));
		}
		if(map.get("building_code") != null) {
			fireFightEquipmentHistory.setBuildingCode((String) map.get("building_code"));
		}
		if(map.get("location_name") != null) {
			fireFightEquipmentHistory.setLocationName((String) map.get("location_name"));
		}
		if(map.get("name") != null) {
			fireFightEquipmentHistory.setName((String) map.get("name"));
		}
		return fireFightEquipmentHistory;
	}

	/**
	 * 将list中查询出来的数组转化成对象列表
	 * @param list
	 * @return
	 */
	public static List<FireFightEquipmentHistory> toObjectList(List<Map<String,Object>> list) {
		List<FireFightEquipmentHistory> histories = new ArrayList<>();
		if(list != null && list.size() > 0) {
			for(Map<String,Object> map:list) {
				FireFightEquipmentHistory fireFightEquipmentHistory = toObject(map);
				if(fireFightEquipmentHistory != null) {
					histories.add(fireFightEquipmentHistory);
				}
			}

		}
		return histories;
	}



}
