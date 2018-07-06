package com.parkbobo.dao.impl;

import com.parkbobo.dao.PatrolBeaconInfoDao;
import com.parkbobo.dao.PatrolSignPointInfoDao;
import com.parkbobo.model.PatrolBeaconInfo;
import org.springframework.stereotype.Component;

@Component("patrolBeaconInfoDaoImpl")
public class PatrolBeaconInfoDaoImpl extends BaseDaoSupport<PatrolBeaconInfo>
        implements PatrolBeaconInfoDao{
}
