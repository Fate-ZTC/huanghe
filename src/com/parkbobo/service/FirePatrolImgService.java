package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FirePatrolImgDao;

@Service
public class FirePatrolImgService {

	@Resource(name="firePatrolImgDaoImpl")
	private FirePatrolImgDao firePatrolImgDao;
}
