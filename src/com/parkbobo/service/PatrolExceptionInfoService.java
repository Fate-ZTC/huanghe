package com.parkbobo.service;

import javax.annotation.Resource;

import com.parkbobo.model.PatrolExceptionInfo;
import com.parkbobo.utils.PageBean;
import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolExceptionInfoDao;

import java.util.List;

@Service
public class PatrolExceptionInfoService {
	
	@Resource(name="patrolExceptionInfoDaoImpl")
	private PatrolExceptionInfoDao patrolExceptionInfoDao;

	public List<PatrolExceptionInfo> getByHQL(String hql) {
		return patrolExceptionInfoDao.getByHQL(hql);
	}
	public PatrolExceptionInfo getById(Integer id){
		return this.patrolExceptionInfoDao.get(id);
	}
	public List<PatrolExceptionInfo> getByProperty(String propertyName ,Object value) {
		return patrolExceptionInfoDao.getByProperty(propertyName,value);
	}
	public PageBean<PatrolExceptionInfo> pageQuery(String hql, int pageSize, int page){
		return patrolExceptionInfoDao.pageQuery(hql, pageSize, page);
	}
	public Integer expectedCountWithTimeRange(String jobNum, String startTime, String endTime){
		String hql = "from PatrolExceptionInfo where jobNum = '" + jobNum
				+ "' and createTime > '" + startTime
				+ "' and createTime < '" + endTime
				+ "'";
		return patrolExceptionInfoDao.pageQuery(hql, 1, 1).getAllRow();
	}
}
