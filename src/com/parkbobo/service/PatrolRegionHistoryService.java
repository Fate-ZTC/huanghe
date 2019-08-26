package com.parkbobo.service;

import com.parkbobo.dao.PatrolRegionDao;
import com.parkbobo.dao.PatrolRegionHistoryDao;
import com.parkbobo.model.PatrolRegion;
import com.parkbobo.model.PatrolRegionHistory;
import com.parkbobo.utils.PageBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Component("patrolRegionHistoryService")
public class PatrolRegionHistoryService {

	@Resource(name="patrolRegionHistoryDaoImpl")
	private PatrolRegionHistoryDao patrolRegionHistoryDao;
	
	public PatrolRegionHistory getById(Integer id){
		return this.patrolRegionHistoryDao.get(id);
	}
	public void deleteById(Integer id){
		this.patrolRegionHistoryDao.delete(id);
	}
	public void bulkDelete(Integer[] idStr){
		this.patrolRegionHistoryDao.bulkDelete(idStr);
	}
	public List<PatrolRegionHistory> getByCampusNum(Integer campusNum){
		return this.patrolRegionHistoryDao.getByProperty("campusNum", campusNum);
	}
	public List<PatrolRegionHistory> getAll(){
		return this.patrolRegionHistoryDao.getAll();
	}
	public PatrolRegionHistory getByHQL(String hql){
		List<PatrolRegionHistory> list = this.patrolRegionHistoryDao.getByHQL(hql);
		if(list.size()>0){
			return list.get(0);
		}else{
			return new PatrolRegionHistory();
		}
	}

	public void update(PatrolRegionHistory patrolRegion){
		this.patrolRegionHistoryDao.update(patrolRegion);
	}
	public void addRecord(PatrolRegionHistory patrolRegion) {
		this.patrolRegionHistoryDao.add(patrolRegion);
	}

	public Boolean exsit(String s, Object id) {
		return patrolRegionHistoryDao.existsByProperty(s,id);
	}
}
