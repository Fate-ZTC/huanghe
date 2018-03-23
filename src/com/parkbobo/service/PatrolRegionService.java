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
	public void bulkDelete(String[] idStr){
		this.patrolRegionDao.bulkDelete(idStr);
	}
	public List<PatrolRegion> getByCampusNum(Integer campusNum){
		return this.patrolRegionDao.getByProperty("campusNum", campusNum);
	}
	
	public List<PatrolRegion> getBySth(String regionName,Integer campusNum,Integer page,Integer pageSize) throws UnsupportedEncodingException{
		String hql = "from PatrolRegion where 1= 1";
		if(StringUtils.isNotBlank(regionName)){
			hql += " and regionName like '%"+URLDecoder.decode(URLEncoder.encode(regionName, "ISO8859_1"), "UTF-8")+"%'";
		}
		if(campusNum!=null){
			hql +=" and campusNum = "+campusNum;
		}
		PageBean<PatrolRegion> query = this.patrolRegionDao.pageQuery(hql, pageSize, page);
		List<PatrolRegion> list = null;
		if(query!=null){
			list = query.getList();
		}
		return list;
	}
	public void update(PatrolRegion patrolRegion){
		this.patrolRegionDao.update(patrolRegion);
	}
	public void addRecord(PatrolRegion patrolRegion) {
		this.patrolRegionDao.add(patrolRegion);
	}
}
