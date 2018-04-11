package com.parkbobo.service;

import java.util.Date;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolConfigDao;
import com.parkbobo.model.PatrolConfig;
import com.parkbobo.model.PatrolLocationInfo;
import com.parkbobo.model.PatrolUser;
import com.parkbobo.model.PatrolUserRegion;
import com.parkbobo.utils.GisUtils;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

@Service
public class PatrolConfigService {



	private final long ONE_MINUTE_MSEC = 60 * 1000;		//一分钟毫秒值

	@Resource(name="patrolConfigDaoImpl")
	private PatrolConfigDao patrolConfigDao;

	@Resource
	private PatrolRegionService patrolRegionService;

	@Resource
	private PatrolUserRegionService patrolUserRegionService;

	@Resource
	private PatrolLocationInfoService patrolLocationInfoService;

	@Resource
	private PatrolUserService patrolUserService;

	/**
	 * 获取用于操作的配置信息
	 * @param configId 管理员端选择配置信息
	 * @return 数据库对应配置信息
	 */
	public PatrolConfig getConfig(Integer configId){
		PatrolConfig pc = this.patrolConfigDao.get(configId);
		return pc;
	}
	/**
	 * 更新配置信息
	 * @param patrolConfig
	 */
	public void updateConfig(PatrolConfig patrolConfig){
		this.patrolConfigDao.merge(patrolConfig);
	}
	/**
	 * 根据id查询
	 */
	public PatrolConfig getById(Integer id){
		return this.patrolConfigDao.get(id);
	}
	public PatrolConfig getByP(Integer campusNum){
		return this.patrolConfigDao.getUniqueByProperty("campusNum", campusNum);
	}


	/**
	 * 进行判断位置没有发生变化是否超过指定时间
	 * @param lon						经度
	 * @param lat						纬度
	 * @param patrolUserRegion			用户区域对象
	 * @param locationNotChangeTime		规定时间
     * @return
     */
	public boolean isLocationNotChange(double lon,double lat,PatrolUserRegion patrolUserRegion,Integer locationNotChangeTime) {
		//进行人员是否发生变化判断
		//这里进行查询校区id
		PatrolUser user = patrolUserService.getByJobNum(patrolUserRegion.getJobNum());
		if(user != null && user.getCampusNum() != null) {
			//获取位置信息
			PatrolLocationInfo locationInfo = patrolLocationInfoService.getLocation(patrolUserRegion.getJobNum(),
					patrolUserRegion.getRegionId(),user.getCampusNum());
			//这里进行判断位置是否发生变化
			double lastLon = locationInfo.getLon();
			double lastLat = locationInfo.getLat();
			//TODO 这里进行判断位置(直接使用经纬度进行相等,将来可以改为位置范围内没有移动,比如两个经纬度一米内)
			if(lastLat == lat && lon == lastLon) {
				//说明经纬度相等
				Date notChangeStartTime = patrolUserRegion.getLocationNotChangeStartTime();
				if(notChangeStartTime == null) {
					//第一次相等,设置开始时间
					patrolUserRegion.setLocationNotChangeStartTime(new Date());
					patrolUserRegionService.updateRecord(patrolUserRegion);
				}else {
					//这里进行判断
					long lastNotChangeTimeInterval =
							new Date().getTime() - patrolUserRegion.getLocationNotChangeStartTime().getTime();
					if(lastNotChangeTimeInterval > locationNotChangeTime * ONE_MINUTE_MSEC) {
						//超过指定时间
						return true;
					}
				}
			}else {
				//这里如果不相等,将上次的时间设置为null
				patrolUserRegion.setLocationNotChangeStartTime(null);
				patrolUserRegionService.updateRecord(patrolUserRegion);
			}

		}

		return false;
	}

	/**
	 * 是否离开区域超过规定时间
	 * @param leaveTime					配置表中规定的离开规定时间(分钟)
	 * @param patrolUserRegion			巡查人员和区域相关信息
	 * @param isInRegion 				是否在指定巡查区域
     * @return
     */
	public boolean isLeaveTime(Integer leaveTime, PatrolUserRegion patrolUserRegion,boolean isInRegion) {
		if(patrolUserRegion == null)
			return true;

		if(patrolUserRegion.getLeaveRegionStartTime() == null) {
			//进行添加离开指定区域开始时间
			patrolUserRegion.setLeaveRegionStartTime(new Date());
			patrolUserRegionService.updateRecord(patrolUserRegion);
		}else {
			//进行判断是否超过指定时间
			Date leaveRegionStartTime = patrolUserRegion.getLeaveRegionStartTime();
			if(new Date().getTime() - leaveRegionStartTime.getTime() > leaveTime * ONE_MINUTE_MSEC) {
				//超过
				return true;
			}
		}

		//进入区域内,重置开始离开区域开始时间
		if(isInRegion) {
			patrolUserRegion.setLeaveRegionStartTime(new Date());
			patrolUserRegionService.updateRecord(patrolUserRegion);
		}
		return false;
	}

	/**
	 * 判断是否离开巡更区域超过指定距离
	 * @param lon				经度
	 * @param lat				纬度
	 * @param configDistance	配置距离
	 * @param regionId			区域id
     * @return
     */
	public boolean isLeaveDistance(double lon,double lat,Integer configDistance,Integer regionId) {
		try {
			Polygon polygon = GisUtils.createCircle(lon, lat, configDistance, 50);
			Geometry regionLocation = patrolRegionService.getById(regionId).getRegionLocation();
			if(regionLocation.contains(polygon)) {
				//在巡逻区域内
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}


	/**
	 * 进行计算人员是否超过规定时间没有进行上传经纬度
	 * @param patrolUserRegion		区域用户对象
	 * @param lossTime				丢失时间(分钟)
	 * @return
     */
	public boolean isLoseTime(PatrolUserRegion patrolUserRegion,Integer lossTime) {
		if(patrolUserRegion != null && lossTime != null && lossTime >= 0) {
			//计算最后一次上传的时间
			long leaveTimeInterVal = new Date().getTime() - patrolUserRegion.getLastUpdateTime().getTime();
			if(leaveTimeInterVal > lossTime * ONE_MINUTE_MSEC) {
				//这里在判断时候需要将开始时间进行重置
				patrolUserRegion.setLastUpdateTime(new Date());
				patrolUserRegionService.updateRecord(patrolUserRegion);
				return true;
			}
		}
		return false;
	}


	/**
	 * 判断当前是否为紧急状态
	 * @param emergencyCofig	紧急状态码,1为紧急状态,0为正常状态
	 * @return
     */
	public boolean isEmergency(Integer emergencyCofig) {
		if(emergencyCofig != null) {
			if(emergencyCofig == 1) {
				//紧急状态
				return true;
			}else {
				//非紧急状态
				return false;
			}
		}
		return false;
	}

	/**
	 * 在指定时间内是否到达巡更区域
	 * @param configTime	时间分钟
	 * @param startTime		开始时间
	 * @return
     */
	public boolean isArriveTimeOn(int configTime, Date startTime) {
		//进行转化
		if(startTime != null) {
			long duringTime = new Date().getTime() - startTime.getTime();
			long cofigTimeSec = 0l;
			if(configTime >= 0){
				cofigTimeSec = configTime * 60 * 1000;
				if(duringTime <= cofigTimeSec) {
					//在指定时间内到达区域
					return true;
				}
			}

		}
		return false;
	}



	/**
	 * 是否能进行推送(在推送完成之后需要进行重置推送时间)
	 * @param patrolUserRegion	区域用户对象
	 * @param frequencyTime		推送频率
	 * @return
     */
	public boolean isArrivePushTime(PatrolUserRegion patrolUserRegion,Integer frequencyTime) {

		if(patrolUserRegion != null && frequencyTime != null) {
			long pushTimeInterval = new Date().getTime() -  patrolUserRegion.getExceptionPushTime().getTime();
			if(pushTimeInterval > frequencyTime * ONE_MINUTE_MSEC) {
				//这里推送时间超过频率时间,能进行推送
				return true;
			}
		}
		return false;
	}


	//TODO 这里进行判断是否到达指定区域(第一次上传)
	public boolean isArrive(double lon,double lat) {

		return false;
	}


}
