package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FirePatrolImgDao;
import com.parkbobo.model.FirePatrolImg;

@Service
public class FirePatrolImgService {

	@Resource(name="firePatrolImgDaoImpl")
	private FirePatrolImgDao firePatrolImgDao;

	public FirePatrolImg add(FirePatrolImg firePatrolImg) {
		return this.firePatrolImgDao.add(firePatrolImg);
	}
}
