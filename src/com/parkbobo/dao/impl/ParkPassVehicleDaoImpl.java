package com.parkbobo.dao.impl;
import org.springframework.stereotype.Component;

import com.parkbobo.dao.ParkPassVehicleDao;
import com.parkbobo.model.ParkPassVehicle;
@Component("parkPassVehicleDaoImpl")
public class ParkPassVehicleDaoImpl extends BaseDaoSupport<ParkPassVehicle> implements ParkPassVehicleDao {
}