package com.parkbobo.service;

import java.util.Date;

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

	/**
	 * 异常正常状态转换
	 * @param patrolUserRegionId 区域表id
	 * @return
	 */
	public PatrolUserRegion switchUpload(Integer patrolUserRegionId){
		
		PatrolUserRegion region = this.patrolUserRegionDao.getUniqueByProperty("id", patrolUserRegionId);
		if(region.getStatus()==1){
			this.patrolUserRegionDao.localUpdateOneFields(patrolUserRegionId, new String[]{"status","lastUpdateTime"}, new Object[]{2,new Date()});
			region.setStatus(2);
		}else{
			this.patrolUserRegionDao.localUpdateOneFields(patrolUserRegionId, new String[]{"status","lastUpdateTime"}, new Object[]{1,new Date()});
			region.setStatus(1);
		}
		return region;
	}
}
