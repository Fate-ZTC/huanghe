package com.system.dao.impl;

import com.parkbobo.dao.impl.BaseDaoSupport;
import com.system.dao.AppVersionDao;
import com.system.model.AppVersion;
import org.springframework.stereotype.Component;

@Component("appVersionDaoImpl")
public class AppVersionDaoImpl extends BaseDaoSupport<AppVersion> implements
		AppVersionDao {

}