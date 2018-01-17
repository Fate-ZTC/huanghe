package com.parkbobo.dao.impl;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.VehicleCostDao;
import com.parkbobo.model.VehicleCost;

@Component("vehicleCostDaoImpl")
public class VehicleCostDaoImpl extends BaseDaoSupport<VehicleCost>
		implements VehicleCostDao{

}
