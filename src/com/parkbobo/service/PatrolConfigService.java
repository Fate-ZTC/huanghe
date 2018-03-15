package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolConfigDao;
import com.parkbobo.model.PatrolConfig;

@Service
public class PatrolConfigService {

	@Resource(name="patrolConfigDaoImpl")
	private PatrolConfigDao patrolConfigDao;
	/**
	 * 获取用于操作的配置信息
	 * @param patrolConfig 管理员端选择配置信息
	 * @return 数据库对应配置信息
	 */
	public PatrolConfig getConfig(PatrolConfig patrolConfig){
		PatrolConfig pc = this.patrolConfigDao.getUniqueByPropertys(new String[]{"uploadTime","leaveRegionDistance","startPatroltime","campusNum"},
				new Object[]{patrolConfig.getUploadTime(),patrolConfig.getLeaveRegionDistance(),patrolConfig.getStartPatrolTime(),patrolConfig.getCampusNum()});
		if(pc == null){
			pc = this.patrolConfigDao.add(patrolConfig);
		}
		return pc;
	}
	/**
	 * 更新配置信息
	 * @param patrolConfig
	 * @return
	 */
	public PatrolConfig updateConfig(PatrolConfig patrolConfig){
		this.patrolConfigDao.update(patrolConfig);
		return patrolConfig;
	}
	/**
	 * 根据id查询
	 */
	public PatrolConfig getById(Integer id){
		return this.patrolConfigDao.getUniqueByProperty("id", id);
	}
}
