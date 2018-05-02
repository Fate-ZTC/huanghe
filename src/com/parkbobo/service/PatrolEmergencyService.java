package com.parkbobo.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolEmergencyDao;
import com.parkbobo.model.PatrolEmergency;
import com.parkbobo.utils.PageBean;

@Service
public class PatrolEmergencyService {

	@Resource(name="patrolEmergencyDaoImpl")
	private PatrolEmergencyDao patrolEmergencyDao;

	public PatrolEmergency add(PatrolEmergency patrolEmergency) {
		return this.patrolEmergencyDao.add(patrolEmergency);		
	}
	public void update(PatrolEmergency patrolEmergency){
		this.patrolEmergencyDao.update(patrolEmergency);
	}
	public PatrolEmergency getNewest(Integer campusNum){
		String hql = "from PatrolEmergency where campusNum =" +campusNum +" and endTime is null order by startTime desc limit 1";
		List<PatrolEmergency> list = this.patrolEmergencyDao.getByHQL(hql);
		PatrolEmergency p = null;
		if(list!=null&&list.size()>0){
			p = list.get(0);
		}
		return p;
	}
	public List<PatrolEmergency> getByPage(Integer campusNum,Integer page,Integer pageSize,Date startTime,Date endTime){
		String hql = "from PatrolEmergency where campusNum = "+campusNum;
		if(startTime!=null){
			hql += " and startTime > '"+startTime+"'";
		}
		if(endTime!=null){
			hql += " and startTime< '"+ endTime + "'";
		}
		PageBean<PatrolEmergency> query = this.patrolEmergencyDao.pageQuery(hql, pageSize, page);
		if(query!=null){
			return query.getList();
		}
		return null;
	}
	public int countEmergencies(Integer campusNum, Date startTime, Date endTime) {
		String hql = "from PatrolEmergency where campusNum = "+campusNum;
		if(startTime!=null){
			hql += " and startTime > '"+startTime+"'";
		}
		if(endTime!=null){
			hql += " and startTime< '"+ endTime + "'";
		}
		int count = this.patrolEmergencyDao.totalCount(hql);
		return count;
	}
	public void deleteById(Integer id){
		this.patrolEmergencyDao.delete(id);
	}
	public void bulkDelete(String ids){
		if(ids.length() > 0){
			String[] strs = ids.split(",");
			Integer[] idArr = new Integer[strs.length];
			for (int i=0; i< strs.length; i++) {
				idArr[i] = Integer.parseInt(strs[i]);
			}
			this.patrolEmergencyDao.bulkDelete(idArr);
		}
	}
	public List<PatrolEmergency> getBySth(Integer campusNum,Date startTime, Date endTime) {
		String hql = "from PatrolEmergency where campusNum = "+campusNum;
		if(startTime!=null){
			hql += " and startTime > '"+startTime+"'";
		}
		if(endTime!=null){
			hql += " and startTime< '"+ endTime + "'";
		}
		return this.patrolEmergencyDao.getByHQL(hql);
	}
	public PageBean<PatrolEmergency> getByHql(String hql, int pageSize, int page) {
		return this.patrolEmergencyDao.pageQuery(hql, pageSize, page);
	}
}
