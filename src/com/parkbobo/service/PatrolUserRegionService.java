package com.parkbobo.service;

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
		return this.patrolUserRegionDao.getUniqueByProperty("id", id);
	}
}
