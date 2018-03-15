package com.system.dao.impl;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.impl.BaseDaoSupport;
import com.system.dao.OptLogsDao;
import com.system.model.OptLogs;

@Component("optLogsDaoImpl")
public class OptLogsDaoImpl extends BaseDaoSupport<OptLogs>
		implements OptLogsDao{

}
