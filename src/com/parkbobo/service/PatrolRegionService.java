package com.parkbobo.service;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolRegionDao;
import com.parkbobo.model.PatrolRegion;
import com.parkbobo.utils.PageBean;

@Service
public class PatrolRegionService {

	@Resource(name="patrolRegionDaoImpl")
	private PatrolRegionDao patrolRegionDao;
	
	public PatrolRegion getById(Integer id){
		return this.patrolRegionDao.get(id);
	}
	public void deleteById(Integer id){
		this.patrolRegionDao.delete(id);
	}
	public void bulkDelete(Integer[] idStr){
		this.patrolRegionDao.bulkDelete(idStr);
	}
	public List<PatrolRegion> getByCampusNum(Integer campusNum){
		return this.patrolRegionDao.getByProperty("campusNum", campusNum);
	}
	public List<PatrolRegion> getAll(){
		return this.patrolRegionDao.getAll();
	}
	public List<PatrolRegion> getByHQL(String hql){
		return this.patrolRegionDao.getByHQL(hql);
	}
	
	public PageBean<PatrolRegion> getBySth(Integer regionId,Integer campusNum,Integer pageSize,Integer page){
		String hql = "from PatrolRegion where isDel=0";
		if(regionId!=null && regionId > 0){
			hql += " and id = "+regionId;
		}
		if(campusNum!=null && campusNum >= 0){
			hql +=" and campusNum = "+campusNum;
		}
		hql +=" order by lastUpdateTime desc";
		PageBean<PatrolRegion> pageList = this.patrolRegionDao.pageQuery(hql,pageSize==null?12:pageSize, page==null?1:page);
		return pageList;
	}
	public void update(PatrolRegion patrolRegion){
		this.patrolRegionDao.update(patrolRegion);
	}
	public void addRecord(PatrolRegion patrolRegion) {
		this.patrolRegionDao.add(patrolRegion);
	}
	public Integer seleceMaxid(){
		 return this.patrolRegionDao.selectMaxid("select max(id) from patrol_region");
	}


	/**
	 * 通过sql进行更新区域对象
	 * @param sql
	 * @return
     */
	public int updateBySql(String sql) {
		if(sql != null && !"".equals(sql)) {
			int result = this.patrolRegionDao.updatePatrolRegionDao(sql);
			return result;
		}
		return 0;
	}




}
