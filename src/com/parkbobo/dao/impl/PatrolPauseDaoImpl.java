package com.parkbobo.dao.impl;

import com.parkbobo.dao.PatrolPauseDao;
import com.parkbobo.model.PatrolPause;
import org.springframework.stereotype.Component;

@Component("patrolPauseDaoImpl")
public class PatrolPauseDaoImpl extends BaseDaoSupport<PatrolPause>
        implements PatrolPauseDao{
}
