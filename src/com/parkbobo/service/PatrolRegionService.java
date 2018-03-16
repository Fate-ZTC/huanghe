package com.parkbobo.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolRegionDao;
import com.parkbobo.model.PatrolRegion;

@Service
public class PatrolRegionService {

	@Resource(name="patrolRegionDaoImpl")
	private PatrolRegionDao patrolRegionDao;
	
	public PatrolRegion getById(Integer id){
		return this.patrolRegionDao.getUniqueByProperty("id", id);
	}
	
	public List<PatrolRegion> getByCampusNum(Integer campusNum){
		return this.patrolRegionDao.getByProperty("campusNum", campusNum);
	}
}
