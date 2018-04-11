package com.parkbobo.service;

import java.util.List;

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

	public List<FirePatrolImg> getByProperty(String propertyName ,Object value) {
		return this.firePatrolImgDao.getByProperty(propertyName, value);
	}


	public List<FirePatrolImg> getByHql(String hql) {
		return firePatrolImgDao.getByHQL(hql);
	}
}
