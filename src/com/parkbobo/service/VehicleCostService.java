package com.parkbobo.service;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.VehicleCostDao;
import com.parkbobo.model.VehicleCost;

@Component("vehicleCostService")
public class VehicleCostService {
	@Resource(name="vehicleCostDaoImpl")
	private VehicleCostDao vehicleCostDao;
	
	public VehicleCost add(VehicleCost vc){
		return vehicleCostDao.add(vc);
	}
	
	public VehicleCost get(Serializable kid){
		return vehicleCostDao.get(kid);
	}
	
	public VehicleCost loadByInunid(String inunid){
		return vehicleCostDao.getUniqueByProperty("inUnid", inunid);
	}

	public VehicleCostDao getVehicleCostDao() {
		return vehicleCostDao;
	}

	public void setVehicleCostDao(VehicleCostDao vehicleCostDao) {
		this.vehicleCostDao = vehicleCostDao;
	}
	public VehicleCost carout2Ve(String carPlate,Date outTime){
		return vehicleCostDao.getUniqueByPropertys(new String[]{"carPlate","outTime"}, new Object[]{carPlate,outTime});
	}
}
