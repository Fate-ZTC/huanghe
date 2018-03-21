package com.parkbobo.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolUserRegionDao;
import com.parkbobo.model.PatrolUserRegion;

@Service
public class PatrolUserRegionService {

	@Resource(name="patrolUserRegionDaoImpl")
	private PatrolUserRegionDao patrolUserRegionDao;

	/**
	 * 增加用户区域信息
	 * @param patrolUserRegion
	 * @return
	 */
	public PatrolUserRegion addRecord(PatrolUserRegion patrolUserRegion){
		return this.patrolUserRegionDao.add(patrolUserRegion);
	}
	/**
	 * 修改用户区域信息
	 */
	public PatrolUserRegion updateRecord(PatrolUserRegion patrolUserRegion){
		this.patrolUserRegionDao.update(patrolUserRegion);
		return patrolUserRegion;
	}
	/**
	 * 根据id获取信息
	 */
	public PatrolUserRegion getById(Integer id){
		return this.patrolUserRegionDao.get(id);
	}
	
	public PatrolUserRegion getUniqueByProperty(String propertyName,Object value){
		return this.patrolUserRegionDao.getUniqueByProperty(propertyName, value);
	}
	public List<PatrolUserRegion> getByProperty(String propertyName,Object value){
		return this.patrolUserRegionDao.getByProperty(propertyName, value);
	}
	public List<PatrolUserRegion> getByProperty(String propertyName ,Object value ,String orderBy,boolean isAsc){
		return this.patrolUserRegionDao.getByProperty(propertyName, value, orderBy, isAsc);
	}
	public List<PatrolUserRegion> getByHQL(String hql){
		return this.patrolUserRegionDao.getByHQL(hql);
	}
	public List<Object[]> getBySql(String sql){
		return this.patrolUserRegionDao.getBySql(sql);
	}

	public Long getCountTime(String jobNum){
		String hql = "from PatrolUserRegion where jobNum = '"+jobNum+"' order by startTime desc limit 1";
		List<PatrolUserRegion> list = this.patrolUserRegionDao.getByHQL(hql);
		if(list!=null&&list.size()>0){
			PatrolUserRegion patrolUserRegion = list.get(0);
			return new Date().getTime()-patrolUserRegion.getStartTime().getTime();
		}
		return null;
	}
	/**
	 * 判断是否偷懒
	 * 
	 */
	public boolean isLazy(PatrolUserRegion patrolUserRegion){
		if(new Date().getTime()-patrolUserRegion.getLastUpdateTime().getTime()>=1000*300){
			return true;
		}
		return false;
	}
	public void merge(PatrolUserRegion patrolUserRegion) {
		this.patrolUserRegionDao.merge(patrolUserRegion);
	}
	public List<PatrolUserRegion> getAbnormal() {
		String hql = "from PatrolUserRegion where endTime is null and status = 2";
		return this.patrolUserRegionDao.getByHQL(hql);
	}
}
