package com.parkbobo.dao.impl;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.PatrolUserDao;
import com.parkbobo.model.PatrolUser;

@Component("patrolUserDaoImpl")
public class PatrolUserDaoImpl extends BaseDaoSupport<PatrolUser> implements PatrolUserDao{

}
