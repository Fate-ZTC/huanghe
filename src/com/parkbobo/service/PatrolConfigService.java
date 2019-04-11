package com.parkbobo.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolConfigDao;
import com.parkbobo.dao.PatrolLocationInfoDao;
import com.parkbobo.dao.PatrolUserDao;
import com.parkbobo.model.*;

@Service
public class PatrolConfigService {



	private final long ONE_MINUTE_MSEC = 60 * 1000;		//一分钟毫秒值
	private final int SPEND_VALUE = 111000;				//在计算面和点的距离时候需要转化成m

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

	@Resource
	private PatrolExceptionService patrolExceptionService;

	@Resource
	private PatrolUserDao patrolUserDao;

	@Resource
	private PatrolConfigService patrolConfigService;

	@Resource
	private ExceptionPushService exceptionPushService;

	@Resource
	private PatrolLocationInfoDao patrolLocationInfoDao;


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
					patrolUserRegion.getId(),user.getCampusNum());
			//这里进行判断位置是否发生变化
			if(locationInfo != null) {
				double lastLon = locationInfo.getLon();
				double lastLat = locationInfo.getLat();
				//TODO 这里进行判断位置(直接使用经纬度进行相等,将来可以改为位置范围内没有移动,比如两个经纬度一米内)
				if (lastLat == lat && lon == lastLon) {
					//说明经纬度相等
					Date notChangeStartTime = patrolUserRegion.getLocationNotChangeStartTime();
					if (notChangeStartTime == null) {
						//第一次相等,设置开始时间
						patrolUserRegion.setLocationNotChangeStartTime(new Date());
						patrolUserRegionService.updateRecord(patrolUserRegion);
					} else {
						//这里进行判断
						long lastNotChangeTimeInterval =
								new Date().getTime() - patrolUserRegion.getLocationNotChangeStartTime().getTime();
						if (lastNotChangeTimeInterval > locationNotChangeTime * ONE_MINUTE_MSEC) {
							//超过指定时间
							return true;
						}
					}
				} else {
					//这里如果不相等,将上次的时间设置为null
					patrolUserRegion.setLocationNotChangeStartTime(null);
					patrolUserRegionService.updateRecord(patrolUserRegion);
				}
			}

		}

		return false;
	}

	/**
	 * 是否离开区域超过规定时间(在这里需要对距离进行判定)
	 * @param leaveTime					配置表中规定的离开规定时间(分钟)
	 * @param patrolUserRegion			巡查人员和区域相关信息
	 * @param isInRegion 				是否在指定巡查区域
     * @return
     */
	public boolean isLeaveTime(Double lon,Double lat,Integer regionId,Integer leaveTime,
							   PatrolUserRegion patrolUserRegion,boolean isInRegion, Integer leaveDistance) {
		if(patrolUserRegion == null)
			return true;

		//这里还需要进行距离判断，超过距离才进行时间判断
		boolean isLeave = isLeaveDistance(lon,lat,leaveDistance,regionId);
		//离开
		if(isLeave) {
			if (patrolUserRegion.getLeaveRegionStartTime() == null) {
				//进行添加离开指定区域开始时间
				patrolUserRegion.setLeaveRegionStartTime(new Date());
				patrolUserRegionService.updateRecord(patrolUserRegion);
			} else {
				//进行判断是否超过指定时间
				Date leaveRegionStartTime = patrolUserRegion.getLeaveRegionStartTime();
				System.out.println("new Date:" + new Date().getTime());
				System.out.println("leaveStartTime:" + leaveRegionStartTime.getTime());
				System.out.println("config Start:" + leaveTime * ONE_MINUTE_MSEC);
				if (new Date().getTime() - leaveRegionStartTime.getTime() > leaveTime * ONE_MINUTE_MSEC) {
					//超过
					System.out.println(new Date().getTime() - leaveRegionStartTime.getTime() > leaveTime * ONE_MINUTE_MSEC);
					return true;
				}
			}
		}

		//进入区域内,重置开始离开区域开始时间
		if(isInRegion && patrolUserRegion.getLeaveRegionStartTime() != null) {
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
			//进行计算是否超过指定距离
			String sql = "SELECT st_distance((SELECT region_location FROM patrol_region WHERE id="+regionId+"), st_geometryfromtext('POINT("+lon+" "+lat+")')) AS distance";
			double distance = patrolUserRegionService.getDistanceBySql(sql);
			if(distance >= 0.0) {
				distance = distance * 111000;
				if(distance < configDistance) {
					//没有离开指定范围
					return false;
				}
			}else {
				return true;
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
			if(patrolUserRegion.getLastUpdateTime() != null) {
				long leaveTimeInterVal = new Date().getTime() - patrolUserRegion.getLastUpdateTime().getTime();
				if (leaveTimeInterVal > lossTime * ONE_MINUTE_MSEC) {
					//这里在判断时候需要将开始时间进行重置
					patrolUserRegion.setLastUpdateTime(new Date());
					patrolUserRegionService.updateRecord(patrolUserRegion);
					return true;
				}
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
	public boolean isArrivePushTime(PatrolUserRegion patrolUserRegion,Integer frequencyTime,Integer startPatrolTime) {

		if(patrolUserRegion != null && frequencyTime != null) {
			if(patrolUserRegion.getExceptionPushTime() == null) {
				//第一次进行异常推送
				System.out.println("xichang time is null");
				return true;
			}else {
				//判断当前时间是否在指定到达时间内，在则不进行推送，不在则进行推送
				if(patrolUserRegion.getStartTime() != null) {
					long startTime = patrolUserRegion.getStartTime().getTime();
					long nowTime = new Date().getTime();
					long endTime = startTime + (startPatrolTime * ONE_MINUTE_MSEC);
					if (startTime <= nowTime && nowTime <= endTime) {
						return false;
					}
				}

				long pushTimeInterval = new Date().getTime() - patrolUserRegion.getExceptionPushTime().getTime();
				if (pushTimeInterval > frequencyTime * ONE_MINUTE_MSEC) {
					//这里推送时间超过频率时间,能进行推送
					System.out.println("now time ="+new Date().getTime()+",xichang time="+patrolUserRegion.getExceptionPushTime().getTime()+",fasong time ="+frequencyTime * ONE_MINUTE_MSEC);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否能进行推送(在推送完成之后需要进行重置推送时间,这里没有加限制，有几分钟到达的时间的限制)
	 * @param patrolUserRegion	区域用户对象
	 * @param frequencyTime		推送频率
	 * @return
	 */
	public boolean isArrivePushTimeNot(PatrolUserRegion patrolUserRegion,Integer frequencyTime) {

		if(patrolUserRegion != null && frequencyTime != null) {
			if(patrolUserRegion.getExceptionPushTime() == null) {
				//第一次进行异常推送
				return true;
			}else {
				long pushTimeInterval = new Date().getTime() - patrolUserRegion.getExceptionPushTime().getTime();
				if (pushTimeInterval > frequencyTime * ONE_MINUTE_MSEC) {
					//这里推送时间超过频率时间,能进行推送
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 这里根据异常类型获取异常信息
	 * @param type	异常类型
     */
	public PatrolException exceptionAssembly(int type) {
		String hql = "FROM PatrolException WHERE type=" + type;
		List<PatrolException> patrolExceptions = patrolExceptionService.getByHQL(hql);
		if(patrolExceptions != null && patrolExceptions.size() > 0) {
			return patrolExceptions.get(0);
		}
		return null;
	}





	/**
	 * 所有状态完成之后进行更新状态，将最新的数据进行更新
	 * @param patrolLocationInfo	经纬度locationInfo
	 * @param patrolUserRegion 		用户region
	 */
	public void updateTimeAndStatus(PatrolUserRegion patrolUserRegion,PatrolLocationInfo patrolLocationInfo) {

		Date date = new Date();
		if(patrolUserRegion.getLastUpdateTime() != null) {
			patrolUserRegion.setLastUpdateTime(date);
		}
		if(patrolUserRegion.getLeaveRegionStartTime() != null) {
			patrolUserRegion.setLeaveRegionStartTime(null);
		}
		if(patrolUserRegion.getLocationNotChangeStartTime() != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT * FROM patrol_location_info WHERE job_num='")
					.append(patrolUserRegion.getJobNum())
					.append("' AND usreg_id=").append(patrolUserRegion.getId())
					.append(" AND ORDER BY timestamp DESC LIMIT 1");

			List<Map<String,Object>> list = this.patrolLocationInfoDao.findForJdbc(sb.toString());
			patrolUserRegion.setLocationNotChangeStartTime(date);
		}
		//进行设置状态(将状态设置为正常，并且清空异常记录)
		patrolUserRegion.setStatus(1);
		patrolUserRegion.setPatrolException(null);

		//设置经纬度info状态信息和异常信息
		patrolLocationInfo.setStatus(1);
		patrolLocationInfo.setPatrolException(null);

		//获取配置信息
		PatrolConfig patrolConfig = this.patrolConfigService.getById(1);
		patrolUserRegionService.updateRecord(patrolUserRegion);
		//查询最新的一条定位数据信息
		PatrolLocationInfo locationInfo= patrolLocationInfoService.getLocation(patrolLocationInfo.getJobNum(),patrolLocationInfo.getUsregId(),patrolLocationInfo.getCampusNum());
		//获取的定位数据时间与最新的一条定位数据时间的差值
		Long xcDate=null;
		if(locationInfo!=null){
			xcDate=patrolLocationInfo.getTimestamp().getTime()-locationInfo.getTimestamp().getTime();
		}

		//如果配置的定位数据上传周期为空，则直接上传，不为空则需要判断
		if(patrolConfig.getSignRange()==null){
			patrolLocationInfoService.add(patrolLocationInfo);
		}
		else{
			if(locationInfo==null){
				patrolLocationInfoService.add(patrolLocationInfo);
			}
			else if(xcDate>=patrolConfig.getSignRange()*1000){
				patrolLocationInfoService.add(patrolLocationInfo);
			}
		}



	}




	/**
	 * 每隔
	 */
	public void patrolStatusTaskInspect() {


	}


}
