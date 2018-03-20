package com.parkbobo.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FirePatrolInfoDao;
import com.parkbobo.model.FirePatrolInfo;

@Service
public class FirePatrolInfoService {
	
	@Resource(name="firePatrolInfoDaoImpl")
	private FirePatrolInfoDao firePatrolInfoDao;
	
	public List<FirePatrolInfo> getByProperty(String propertyName ,Object value ,String orderBy,boolean isAsc){
		return firePatrolInfoDao.getByProperty(propertyName, value, orderBy, isAsc);
	}
	public List<FirePatrolInfo> getByProperty(String propertyName ,Object value){
		return firePatrolInfoDao.getByProperty(propertyName, value);
	}
	public FirePatrolInfo get(Integer entityid){
		return firePatrolInfoDao.get(entityid);
	}
	

	public FirePatrolInfo add(FirePatrolInfo firePatrolInfo) {
		return this.firePatrolInfoDao.add(firePatrolInfo);
	}
}
