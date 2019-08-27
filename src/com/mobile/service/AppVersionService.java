package com.mobile.service;

import java.util.List;

import javax.annotation.Resource;

import com.mobile.dao.AppVersionDao;
import com.parkbobo.model.FirePatrolException;
import com.system.utils.PageBean;
import org.springframework.stereotype.Component;


import com.mobile.model.AppVersion;

@Component("appVersionService")
public class AppVersionService 
{
	@Resource(name="appVersionDaoImpl")
	private AppVersionDao appVersionDao;
	
	public void save(AppVersion appVersion)
	{
		this.appVersionDao.merge(appVersion);
	}
	
	public void delete(String versionCode)
	{
		AppVersion appVersion = this.appVersionDao.get(versionCode);
		appVersion.setIsDel(1);
		this.appVersionDao.merge(appVersion);
	}
	
	public PageBean<AppVersion> getPage(String hql, int pageSize, int page)
	{
		return this.appVersionDao.pageQuery(hql, pageSize, page);
	}
	
//	public List<AppVersion> getByHql(String hql)
//	{
//		return this.appVersionDao.getByHQL(hql);
//	}

	public PageBean<AppVersion> getByHql(String hql, int pageSize, int page) {

		return this.appVersionDao.pageQuery(hql, pageSize, page);
	}
	
	public AppVersion getById(String versionCode)
	{
		return this.appVersionDao.get(versionCode);
	}
}