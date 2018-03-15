package com.parkbobo.dao.impl;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.PatrolLocationInfoDao;
import com.parkbobo.model.PatrolLocationInfo;

@Component("patrolLocationInfoDaoImpl")
public class PatrolLocationInfoDaoImpl extends BaseDaoSupport<PatrolLocationInfo> implements PatrolLocationInfoDao{

}
