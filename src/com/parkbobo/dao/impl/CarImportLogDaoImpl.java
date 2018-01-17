package com.parkbobo.dao.impl;
import org.springframework.stereotype.Component;

import com.parkbobo.dao.CarImportLogDao;
import com.parkbobo.model.CarImportLog;
@Component("carImportLogDaoImpl")
public class CarImportLogDaoImpl extends BaseDaoSupport<CarImportLog> implements CarImportLogDao {
}