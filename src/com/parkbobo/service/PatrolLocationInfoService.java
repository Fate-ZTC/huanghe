package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolLocationInfoDao;

@Service
public class PatrolLocationInfoService {
	
	@Resource(name="patrolLocationInfoDaoImpl")
	private PatrolLocationInfoDao patrolLocationInfoDao;
}
