package com.parkbobo.dao.impl;

import com.parkbobo.dao.PatrolSignRecordDao;
import com.parkbobo.model.PatrolSignRecord;
import org.springframework.stereotype.Component;

@Component("patrolSignRecordDaoImpl")
public class PatrolSignRecordDaoImpl extends BaseDaoSupport<PatrolSignRecord>
        implements PatrolSignRecordDao{
}
