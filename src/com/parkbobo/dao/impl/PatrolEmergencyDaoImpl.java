package com.parkbobo.dao.impl;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.PatrolEmergencyDao;
import com.parkbobo.model.PatrolEmergency;

@Component("patrolEmergencyDaoImpl")
public class PatrolEmergencyDaoImpl extends BaseDaoSupport<PatrolEmergency> implements PatrolEmergencyDao{

}
