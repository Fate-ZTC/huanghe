package com.parkbobo.dao.impl;
import org.springframework.stereotype.Component;

import com.parkbobo.dao.CarExitLogDao;
import com.parkbobo.model.CarExitLog;
@Component("carExitLogDaoImpl")
public class CarExitLogDaoImpl extends BaseDaoSupport<CarExitLog> implements CarExitLogDao {
}