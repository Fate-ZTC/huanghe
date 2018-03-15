package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolExceptionDao;

@Service
public class PatrolExceptionService {
	
	@Resource(name="patrolExceptionDaoImpl")
	private PatrolExceptionDao patrolExceptionDao;
}
