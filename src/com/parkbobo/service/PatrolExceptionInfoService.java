package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolExceptionInfoDao;

@Service
public class PatrolExceptionInfoService {
	
	@Resource(name="patrolExceptionInfoDaoImpl")
	private PatrolExceptionInfoDao patrolExceptionInfoDao;
}
