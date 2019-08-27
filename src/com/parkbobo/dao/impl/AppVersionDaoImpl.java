package com.parkbobo.dao.impl;

import com.parkbobo.dao.AppVersionDao;
import com.parkbobo.model.AppVersion;
import org.springframework.stereotype.Component;

@Component("appVersionDaoImpl")
public class AppVersionDaoImpl  extends BaseDaoSupport<AppVersion>
		implements AppVersionDao {

}