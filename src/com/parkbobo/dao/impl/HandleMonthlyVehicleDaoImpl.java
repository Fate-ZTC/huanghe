package com.parkbobo.dao.impl;
import org.springframework.stereotype.Component;

import com.parkbobo.dao.CarExitLogDao;
import com.parkbobo.dao.HandleMonthlyVehicleDao;
import com.parkbobo.model.CarExitLog;
import com.parkbobo.model.HandleMonthlyVehicle;
@Component("handleMonthlyVehicleDaoImpl")
public class HandleMonthlyVehicleDaoImpl extends BaseDaoSupport<HandleMonthlyVehicle> implements HandleMonthlyVehicleDao {
}