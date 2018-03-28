package com.parkbobo.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.parkbobo.dao.FirePatrolInfoDao;
import com.parkbobo.model.FirePatrolInfo;
import com.parkbobo.utils.PageBean;

@Service
public class FirePatrolInfoService {

	@Resource(name="firePatrolInfoDaoImpl")
	private FirePatrolInfoDao firePatrolInfoDao;

	public List<FirePatrolInfo> getByProperty(String propertyName ,Object value ,String orderBy,boolean isAsc){
		return firePatrolInfoDao.getByProperty(propertyName, value, orderBy, isAsc);
	}
	public List<FirePatrolInfo> getByProperty(String propertyName ,Object value){
		return firePatrolInfoDao.getByProperty(propertyName, value);
	}
	public List<FirePatrolInfo> getAll(){
		return firePatrolInfoDao.getAll();
	}
	public FirePatrolInfo get(Integer entityid){
		return firePatrolInfoDao.get(entityid);
	}


	public FirePatrolInfo add(FirePatrolInfo firePatrolInfo) {
		return this.firePatrolInfoDao.add(firePatrolInfo);
	}

	public FirePatrolInfo getNewest(Integer equipmentId){
		String hql = " from FirePatrolInfo where fireFightEquipment.id = "+equipmentId+" order by timestamp desc limit 1";
		List<FirePatrolInfo> list = this.firePatrolInfoDao.getByHQL(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	public void update(FirePatrolInfo firePatrolInfo){
		this.firePatrolInfoDao.update(firePatrolInfo);
	}
	public PageBean<FirePatrolInfo> getUsers(String equipName, String username, Integer status, Date startTime,
			Date endTime, Integer page, Integer pageSize) throws UnsupportedEncodingException {
		String hql = "from FirePatrolInfo where 1=1";
		if(StringUtils.isNotBlank(equipName)){
			hql += " and fireFightEquipment.name like '%"+URLDecoder.decode(URLEncoder.encode(equipName, "ISO8859_1"), "UTF-8")+"%'";
		}
		if(StringUtils.isNotBlank(username)){
			hql += " and firePatrolUser.username like '%" + URLDecoder.decode(URLEncoder.encode(username, "ISO8859_1"), "UTF-8")+"%'";
		}
		if(status!=null){
			hql += " and patrolStatus ="+status;
		}
		if(startTime!=null){
			hql += " and timestamp > '"+startTime+"'";
		}
		if(endTime!=null){
			hql += " and timestamp < '"+endTime+"'";
		}
		return this.firePatrolInfoDao.pageQuery(hql, pageSize, page);
	}
	public int countInfo(String equipName, String username, Integer status, Date startTime, Date endTime) throws UnsupportedEncodingException {
		String hql = "from FirePatrolInfo where 1=1";
		if(StringUtils.isNotBlank(equipName)){
			hql += " and fireFightEquipment.name like '%"+URLDecoder.decode(URLEncoder.encode(equipName, "ISO8859_1"), "UTF-8")+"%'";
		}
		if(StringUtils.isNotBlank(username)){
			hql += " and firePatrolUser.username like '%" + URLDecoder.decode(URLEncoder.encode(username, "ISO8859_1"), "UTF-8")+"%'";
		}
		if(status!=null){
			hql += " and patrolStatus ="+status;
		}
		if(startTime!=null){
			hql += " and timestamp > '"+startTime+"'";
		}
		if(endTime!=null){
			hql += " and timestamp < '"+endTime+"'";
		}
		return this.firePatrolInfoDao.totalCount(hql);
	}
	public void deleteById(Integer id) {
		this.firePatrolInfoDao.delete(id);
	}
	public PageBean<FirePatrolInfo> getByHql(String hql, int pageSize, int page) {
		return this.firePatrolInfoDao.pageQuery(hql, pageSize, page);
	}
	public void bulkDelete(String ids) {
		if(ids.length() > 0){
			String[] strs = ids.split(",");
			Integer[] idArr = new Integer[strs.length];
			for (int i=0; i< strs.length; i++) {
				idArr[i] = Integer.parseInt(strs[i]);
			}
			this.firePatrolInfoDao.bulkDelete(idArr);
		}
	}
	public List<FirePatrolInfo> getBySth(FirePatrolInfo firePatrolInfo, Date startTime, Date endTime) throws UnsupportedEncodingException {
		String hql = "from FirePatrolInfo where 1=1";
		if(StringUtils.isNotBlank(firePatrolInfo.getFireFightEquipment().getName())){
			hql += " and fireFightEquipment.name like '%"+URLDecoder.decode(URLEncoder.encode(firePatrolInfo.getFireFightEquipment().getName(), "ISO8859_1"), "UTF-8")+"%'";
		}
		if(StringUtils.isNotBlank(firePatrolInfo.getFirePatrolUser().getUsername())){
			hql += " and firePatrolUser.username like '%" + URLDecoder.decode(URLEncoder.encode(firePatrolInfo.getFirePatrolUser().getUsername(), "ISO8859_1"), "UTF-8")+"%'";
		}
		if(firePatrolInfo.getPatrolStatus()!=-1){
			hql += " and patrolStatus ="+firePatrolInfo.getPatrolStatus();
		}
		if(startTime!=null){
			hql += " and timestamp > '"+startTime+"'";
		}
		if(endTime!=null){
			hql += " and timestamp < '"+endTime+"'";
		}
		hql +=" order by timestamp desc";
		return this.firePatrolInfoDao.getByHQL(hql);
	}
	public List<FirePatrolInfo> getByHql(String jobNum) {
		String hql = " from FirePatrolInfo where firePatrolUser.jobNum = '" +jobNum+"' order by timestamp desc";
		return this.firePatrolInfoDao.getByHQL(hql);
	}
}
