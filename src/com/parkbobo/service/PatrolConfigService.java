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
	public PatrolConfig getConfig(Integer configId){
		PatrolConfig pc = this.patrolConfigDao.get(configId);
		return pc;
	}
	/**
	 * 更新配置信息
	 * @param patrolConfig
	 */
	public void updateConfig(PatrolConfig patrolConfig){
		this.patrolConfigDao.update(patrolConfig);
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
}
