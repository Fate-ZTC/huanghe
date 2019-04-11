package com.parkbobo.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolLocationInfoDao;
import com.parkbobo.model.PatrolLocationInfo;
import com.parkbobo.utils.PageBean;

@Service
public class PatrolLocationInfoService {

	@Resource(name="patrolLocationInfoDaoImpl")
	private PatrolLocationInfoDao patrolLocationInfoDao;

	public boolean addRecord(PatrolLocationInfo patrolLocationInfo){
		try {
			this.patrolLocationInfoDao.add(patrolLocationInfo);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 获取当前位置信息
	 * @param jobNum
	 * @param usregId
	 * @param campusNum
	 * @return
	 */
	public PatrolLocationInfo getLocation(String jobNum,Integer usregId,Integer campusNum){
		String hql = "from PatrolLocationInfo where jobNum = '"+jobNum+"' and usregId="+usregId+
				" and campusNum = "+campusNum +" order by timestamp desc";
		List<PatrolLocationInfo> list = this.patrolLocationInfoDao.getPage(hql,0,1);
		if(list.size()>0&&list!=null) {
			return list.get(0);
		}
		return null;
	}

	public List<PatrolLocationInfo> getByProperty(String propertyName,Object value){
		return this.patrolLocationInfoDao.getByProperty(propertyName, value);
	}
	public List<PatrolLocationInfo> getByProperty(String propertyName ,Object value ,String orderBy,boolean isAsc){
		return this.patrolLocationInfoDao.getByProperty(propertyName, value, orderBy, isAsc);
	}
	public PatrolLocationInfo add(PatrolLocationInfo patrolLocationInfo){
		return patrolLocationInfoDao.add(patrolLocationInfo);
	}
	public List<Integer> getExceptionTypes(String jobNum,Date startTime,Date endTime){
		String sql = "select distinct exception_type from patrol_location_info where job_num = '"+jobNum+"' and timestamp >"+startTime +"and timestamp <"+ endTime;
		List<Integer>  list= this.patrolLocationInfoDao.getBySql(sql);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	public PageBean<PatrolLocationInfo> getAbnormal(Integer pageSize, Integer page) {
		String hql = "from PatrolLocationInfo where status!=1";
		return this.patrolLocationInfoDao.pageQuery(hql, pageSize==null?20:pageSize, page==null?1:page);
	}

	public List<PatrolLocationInfo> getByHql(String hql) {
		return patrolLocationInfoDao.getByHQL(hql);
	}

	public void merge(PatrolLocationInfo patrolLocationInfo) {
		patrolLocationInfoDao.merge(patrolLocationInfo);
	}
}
