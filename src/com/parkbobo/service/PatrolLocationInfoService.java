package com.parkbobo.service;

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
	 * 获取当前位置信息
	 * @param userid
	 * @param usregId
	 * @param campusNum
	 * @return
	 */
	public PatrolLocationInfo getLocation(String jobNum,Integer usregId,Integer campusNum){
		String hql = "from PatrolLocationInfo where jobNum = '"+jobNum+"' and usregId="+usregId+
				" and campusNum = "+campusNum +" order by timestamp desc limit 1";
		List<PatrolLocationInfo> list = this.patrolLocationInfoDao.getByHQL(hql);
		if(list.size()>0&&list!=null){
			return list.get(0);
		}
		return null;
	}
	public List<PatrolLocationInfo> getByProperty(String propertyName,Object value){
		return this.patrolLocationInfoDao.getByProperty(propertyName, value);
	}
	public List<PatrolLocationInfo> getByProperty(String propertyName ,Object value ,String orderBy,boolean isAsc){
		return this.patrolLocationInfoDao.getByProperty(propertyName, value, orderBy, isAsc);
	}
	public PatrolLocationInfo add(PatrolLocationInfo patrolLocationInfo){
		return patrolLocationInfoDao.add(patrolLocationInfo);
	}
}
