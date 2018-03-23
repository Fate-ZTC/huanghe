package com.parkbobo.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FirePatrolExceptionDao;
import com.parkbobo.model.FirePatrolException;
import com.parkbobo.utils.PageBean;

@Service
public class FirePatrolExceptionService {

	@Resource(name="firePatrolExceptionDaoImpl")
	private FirePatrolExceptionDao firePatrolExceptionDao;
	public List<FirePatrolException> getByHQL(String hql) {
		return firePatrolExceptionDao.getByHQL(hql);
	}
	public List<FirePatrolException> getAllFireExceptions() {
		return this.firePatrolExceptionDao.getAll();
	}
	public List<FirePatrolException> getBySth(String excName,Integer page,Integer pageSize) throws UnsupportedEncodingException{
		String hql = "from FirePatrolException where 1=1";
		if(excName!=null&&!"".equals(excName)){
			hql += " and exceptionName like'% "+URLDecoder.decode(URLEncoder.encode(excName, "ISO8859_1"), "UTF-8")+"'";
		}
		hql +=" order by sort";
		PageBean<FirePatrolException> query = this.firePatrolExceptionDao.pageQuery(hql, pageSize, page);
		if(query!=null){
			return query.getList();
		}
		return null;
	}
	public int countBySth(String excName) throws UnsupportedEncodingException {
		String hql = "from FirePatrolException where 1=1";
		if(excName!=null&&!"".equals(excName)){
			hql += " and exceptionName like'% "+URLDecoder.decode(URLEncoder.encode(excName, "ISO8859_1"), "UTF-8")+"'";
		}
		int count = this.firePatrolExceptionDao.totalCount(hql);
		return count;
	}
	public FirePatrolException addRecord(FirePatrolException firePatrolException){
		return this.firePatrolExceptionDao.add(firePatrolException);
	}
	public FirePatrolException getById(Integer id){
		return this.firePatrolExceptionDao.get(id);
	}
	public void updateRecord(FirePatrolException firePatrolException) {
		this.firePatrolExceptionDao.update(firePatrolException);
	}
	public void deleteById(Integer id) {
		this.firePatrolExceptionDao.delete(id);
	}
	public void bulkDelete(String[] idArr) {
		this.firePatrolExceptionDao.bulkDelete(idArr);
	}

}
