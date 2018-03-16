package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolExceptionDao;
import com.parkbobo.model.PatrolException;

@Service
public class PatrolExceptionService {
	
	@Resource(name="patrolExceptionDaoImpl")
	private PatrolExceptionDao patrolExceptionDao;
	
	
	public PatrolException get(Integer entityid) {
		return patrolExceptionDao.get(entityid);
	}
}
