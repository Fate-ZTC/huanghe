package com.mobile.dao.impl;

import com.mobile.dao.AppVersionDao;
import org.springframework.stereotype.Component;


import com.mobile.model.AppVersion;
@Component("appVersionDaoImpl")
public class AppVersionDaoImpl extends GisBaseDAOSupport<AppVersion> implements
		AppVersionDao {

}