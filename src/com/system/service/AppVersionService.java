package com.system.service;

import com.parkbobo.utils.PageBean;
import com.system.dao.AppVersionDao;
import com.system.model.AppVersion;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
	public void update(AppVersion appVersion) {
		this.appVersionDao.merge(appVersion);
	}

	public PageBean<AppVersion> loadPage(String hql, int pageSize, int page){
		return appVersionDao.pageQuery(hql, pageSize, page);
	}
}