package com.parkbobo.dao.impl;

import com.parkbobo.dao.PatrolSignPointInfoDao;
import com.parkbobo.model.PatrolSignPointInfo;
import org.springframework.stereotype.Component;

@Component("patrolSignPointInfoDaoImpl")
public class PatrolSignPointInfoDaoImpl extends BaseDaoSupport<PatrolSignPointInfo>
        implements PatrolSignPointInfoDao{
}
