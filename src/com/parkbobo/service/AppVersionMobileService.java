package com.parkbobo.service;

import java.util.List;

import javax.annotation.Resource;


import com.system.dao.AppVersionDao;
import com.system.model.AppVersion;
import org.springframework.stereotype.Service;

@Service("appVersionMobileService")
public class AppVersionMobileService
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

	public List<AppVersion> getByHql(String hql)
	{
		return this.appVersionDao.getByHQL(hql);
	}
	
	public AppVersion getById(String versionCode)
	{
		return this.appVersionDao.get(versionCode);
	}
}