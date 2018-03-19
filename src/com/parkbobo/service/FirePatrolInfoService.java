package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FirePatrolInfoDao;
import com.parkbobo.model.FirePatrolInfo;

@Service
public class FirePatrolInfoService {
	
	@Resource(name="firePatrolInfoDaoImpl")
	private FirePatrolInfoDao firePatrolInfoDao;

	public FirePatrolInfo add(FirePatrolInfo firePatrolInfo) {
		return this.firePatrolInfoDao.add(firePatrolInfo);
	}
}
