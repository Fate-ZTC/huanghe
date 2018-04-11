package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FirePatrolConfigDao;
import com.parkbobo.model.FirePatrolConfig;

@Service
public class FirePatrolConfigService {

	@Resource(name="firePatrolConfigDaoImpl")
	private FirePatrolConfigDao firePatrolConfigDao;

	public FirePatrolConfig getConfigBy(Integer campusNum) {
		return this.firePatrolConfigDao.getUniqueByProperty("campusNum", campusNum);
	}

	public void updateConfig(FirePatrolConfig config) {
		this.firePatrolConfigDao.merge(config);
	}

	public FirePatrolConfig getById(Integer configId) {
		return this.firePatrolConfigDao.get(configId);
	}
}
