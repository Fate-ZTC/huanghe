package com.parkbobo.service;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolExceptionDao;
import com.parkbobo.dao.PatrolExceptionInfoDao;
import com.parkbobo.model.PatrolException;
import com.parkbobo.model.PatrolExceptionInfo;
import com.parkbobo.utils.PageBean;

@Service
public class PatrolExceptionService {
	
	@Resource(name="patrolExceptionDaoImpl")
	private PatrolExceptionDao patrolExceptionDao;
	@Resource
	private PatrolExceptionInfoDao patrolExceptionInfoDao;


	
	public PatrolException get(Integer entityid) {
		return patrolExceptionDao.get(entityid);
	}
	
	public List<PatrolException> getAll(){
		return patrolExceptionDao.getAll();
	}
	public List<PatrolException> getByHQL(String hql) {
		return patrolExceptionDao.getByHQL(hql);
	}
	
	public PageBean<PatrolException> pageQuery(String hql, int pageSize,int page) {
		return patrolExceptionDao.pageQuery(hql, pageSize, page);
	}

	/**
	 * 推送异常的时候进进行保存异常到异常信息表中,为位统计用
	 * @param type			类型
	 * @param usregid		区域用户id
	 * @param username		用户名
     * @param job_num		工号
     */
	public void addExceptionInfo(Integer type,int usregid,String username,String job_num) {
		String hql = "FROM PatrolException WHERE type=" + type;
		List<PatrolException> patrolExceptions = this.patrolExceptionDao.getByHQL(hql);
		//这里进行添加相关信息
		PatrolExceptionInfo exceptionInfo = new PatrolExceptionInfo();
		if(patrolExceptions != null && patrolExceptions.size() > 0) {
			PatrolException patrolException =  patrolExceptions.get(0);
			exceptionInfo.setExceptionName(patrolException.getExceptionName());
			exceptionInfo.setExceptionType(patrolException.getType());
			exceptionInfo.setUsregId(usregid);
			exceptionInfo.setJobNum(job_num);
			exceptionInfo.setUsername(username);
			exceptionInfo.setCreateTime(new Date());
			patrolExceptionInfoDao.add(exceptionInfo);
		}

	}

}
