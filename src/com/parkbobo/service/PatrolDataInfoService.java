package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolDataInfoDao;

@Service
public class PatrolDataInfoService {

	@Resource(name="patrolDataInfoDaoImpl")
	private PatrolDataInfoDao patrolDataInfoDao;
}
