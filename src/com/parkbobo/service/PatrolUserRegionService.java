package com.parkbobo.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolUserRegionDao;
import com.parkbobo.model.PatrolUserRegion;

@Service
public class PatrolUserRegionService {

	@Resource(name="patrolUserRegionDaoImpl")
	private PatrolUserRegionDao patrolUserRegionDao;

	/**
	 * 增加用户区域信息
	 * @param patrolUserRegion
	 * @return
	 */
	public PatrolUserRegion addRecord(PatrolUserRegion patrolUserRegion){
		return this.patrolUserRegionDao.add(patrolUserRegion);
	}
	/**
	 * 修改用户区域信息
	 */
	public PatrolUserRegion updateRecord(PatrolUserRegion patrolUserRegion){
		this.patrolUserRegionDao.update(patrolUserRegion);
		return patrolUserRegion;
	}
	/**
	 * 根据id获取信息
	 */
	public PatrolUserRegion getById(Integer id){
		return this.patrolUserRegionDao.get(id);
	}

	public PatrolUserRegion getUniqueByProperty(String propertyName,Object value){
		return this.patrolUserRegionDao.getUniqueByProperty(propertyName, value);
	}
	public List<PatrolUserRegion> getByProperty(String propertyName,Object value){
		return this.patrolUserRegionDao.getByProperty(propertyName, value);
	}
	public List<PatrolUserRegion> getByProperty(String propertyName ,Object value ,String orderBy,boolean isAsc){
		return this.patrolUserRegionDao.getByProperty(propertyName, value, orderBy, isAsc);
	}
	public List<PatrolUserRegion> getByHQL(String hql){
		return this.patrolUserRegionDao.getByHQL(hql);
	}
	public List<Object[]> getBySql(String sql){
		return this.patrolUserRegionDao.getBySql(sql);
	}

	public PatrolUserRegion getCountTime(String jobNum){
		String hql = "from PatrolUserRegion where endTime is null and  jobNum = '"+jobNum+"' order by startTime desc limit 1";
		List<PatrolUserRegion> list = this.patrolUserRegionDao.getByHQL(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 判断是否偷懒
	 * 
	 */
	public boolean isLazy(PatrolUserRegion patrolUserRegion,Integer lazyTime){
		if(new Date().getTime()-patrolUserRegion.getLastUpdateTime().getTime()>=1000*60*lazyTime){
			return true;
		}
		return false;
	}
	public void merge(PatrolUserRegion patrolUserRegion) {
		this.patrolUserRegionDao.merge(patrolUserRegion);
	}
	public List<PatrolUserRegion> getAbnormal() {
		String hql = "from PatrolUserRegion where endTime is null and status = 2";
		return this.patrolUserRegionDao.getByHQL(hql);
	}
	public List<PatrolUserRegion> getPatrolUserBySth(String username,Integer regionId,Integer exceptionType,Date startTime,Date endTime,Integer page,Integer pageSize) throws UnsupportedEncodingException{
		String hql = "from PatrolUserRegion where 1=1";
		if(StringUtils.isNotBlank(username)){
			hql += " and username like '%"+URLDecoder.decode(URLEncoder.encode(username, "ISO8859_1"), "UTF-8")+"%'";
		}
		if(regionId!=null&&regionId!=0){
			hql += " and regionId =" + regionId;
		}
		if(exceptionType!=null){
			if(exceptionType==1){
				hql += " and exceptionType is null";
			}else{
				hql += " and exceptionType is not null";
			}
		}
		if(startTime!=null){
			hql += " and startTime > '"+ startTime + "'";
		}
		if(endTime!=null){
			hql +=" and startTime < '" + endTime + "'"; 
		}
		hql +=" order by startTime desc";		
		return this.patrolUserRegionDao.pageQuery(hql,pageSize,page).getList();
	}
	public PatrolUserRegion getByJobNum(String jobNum) {
		String hql = "from PatrolUserRegion where jobNum ='"+ jobNum +"' and endTime is null order by createTime desc limit 1";
		List<PatrolUserRegion> list = this.patrolUserRegionDao.getByHQL(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
