package com.system.dao.impl;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.impl.BaseDaoSupport;
import com.system.dao.DepartmentDao;
import com.system.model.Department;
@Component("departmentDaoImpl")
public class DepartmentDaoImpl extends BaseDaoSupport<Department> implements
		DepartmentDao {

}
