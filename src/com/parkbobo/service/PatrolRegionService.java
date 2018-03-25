package com.parkbobo.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
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
		String hql = "from PatrolRegion where 1= 1";
		if(regionId!=null){
			hql += " and id = "+regionId;
		}
		if(campusNum!=null){
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
}
