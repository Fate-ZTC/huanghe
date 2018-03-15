package com.parkbobo.dao.impl;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.PatrolExceptionDao;
import com.parkbobo.model.PatrolException;

@Component("patrolExceptionDaoImpl")
public class PatrolExceptionDaoImpl extends BaseDaoSupport<PatrolException> implements PatrolExceptionDao{

}
