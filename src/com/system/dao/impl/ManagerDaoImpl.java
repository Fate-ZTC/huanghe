package com.system.dao.impl;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.impl.BaseDaoSupport;
import com.system.dao.ManagerDao;
import com.system.model.Manager;

@Component("managerDaoImpl")
public class ManagerDaoImpl extends BaseDaoSupport<Manager>
		implements ManagerDao{

}
