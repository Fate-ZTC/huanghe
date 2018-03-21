package com.parkbobo.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolExceptionDao;
import com.parkbobo.model.PatrolException;
import com.parkbobo.utils.PageBean;

@Service
public class PatrolExceptionService {
	
	@Resource(name="patrolExceptionDaoImpl")
	private PatrolExceptionDao patrolExceptionDao;
	
	
	public PatrolException get(Integer entityid) {
		return patrolExceptionDao.get(entityid);
	}
	
	public List<PatrolException> getByHQL(String hql) {
		return patrolExceptionDao.getByHQL(hql);
	}
	
	public PageBean<PatrolException> pageQuery(String hql, int pageSize,int page) {
		return patrolExceptionDao.pageQuery(hql, pageSize, page);
	}


	public List<PatrolException> getAllFireExceptions() {
		String hql = "from PatrolException where id >=3 ";
		return this.patrolExceptionDao.getByHQL(hql);
	}
}
