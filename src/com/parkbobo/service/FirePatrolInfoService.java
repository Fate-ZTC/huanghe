package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FirePatrolInfoDao;

@Service
public class FirePatrolInfoService {
	
	@Resource(name="firePatrolInfoDaoImpl")
	private FirePatrolInfoDao firePatrolInfoDao;
}
