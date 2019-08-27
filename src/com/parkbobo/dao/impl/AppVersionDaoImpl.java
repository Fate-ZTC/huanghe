package com.parkbobo.dao.impl;

import com.mobile.dao.impl.GisBaseDAOSupport;
import com.parkbobo.dao.AppVersionDao;
import org.springframework.stereotype.Component;


import com.mobile.model.AppVersion;
@Component("appVersionDaoImpl")
public class AppVersionDaoImpl extends GisBaseDAOSupport<AppVersion> implements
		AppVersionDao {

}