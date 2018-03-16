package com.parkbobo.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolLocationInfoDao;
import com.parkbobo.model.PatrolLocationInfo;
import com.parkbobo.model.PatrolUserRegion;

@Service
public class PatrolLocationInfoService {

	@Resource(name="patrolLocationInfoDaoImpl")
	private PatrolLocationInfoDao patrolLocationInfoDao;

	public boolean addRecord(PatrolLocationInfo patrolLocationInfo){
		try {
			this.patrolLocationInfoDao.add(patrolLocationInfo);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 判断是否在原地五分钟未动
	 * @param uploadTime 上传频率  单位秒 5既 5秒一次
	 * @param userId  用户id
	 * @param lon 经度
	 * @param lat 纬度
	 * @param usregId 区域id
	 * @return  true 未动   false 动了
	 */
	public boolean isLazy(Integer uploadTime,Integer userId,double lon,double lat,Integer usregId){
		Calendar oldtime = Calendar.getInstance();
		oldtime.add(Calendar.MINUTE, -5);
		String starttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(oldtime.getTime());
		Calendar newtime = Calendar.getInstance();
		String endtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newtime.getTime());
		String hql = "from PatrolLocationInfo where userId ="+userId+" and lon = "+lon+" and lat="+lat
				+" and usregId="+usregId+"and timestamp between '"+starttime+"' and '"+endtime+"'";
		List<PatrolLocationInfo> list = this.patrolLocationInfoDao.getByHQL(hql);
		if(list.size()>=(300/uploadTime)){
			return true;
		}
		return false;
	}
	public List<PatrolLocationInfo> getByProperty(String propertyName,Object value){
		return this.patrolLocationInfoDao.getByProperty(propertyName, value);
	}
	public List<PatrolLocationInfo> getByProperty(String propertyName ,Object value ,String orderBy,boolean isAsc){
		return this.patrolLocationInfoDao.getByProperty(propertyName, value, orderBy, isAsc);
	}
}
