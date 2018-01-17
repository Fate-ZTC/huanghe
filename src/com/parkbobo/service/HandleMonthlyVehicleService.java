package com.parkbobo.service;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.HandleMonthlyVehicleDao;
import com.parkbobo.dao.ParkPassVehicleMonthDao;
import com.parkbobo.model.HandleMonthlyVehicle;
import com.parkbobo.model.ParkPassVehicleMonth;

@Component("handleMonthlyVehicleService")
public class HandleMonthlyVehicleService {
	@Resource(name="handleMonthlyVehicleDaoImpl")
	private HandleMonthlyVehicleDao handleMonthlyVehicleDao;

	/**
	 * HQL语句查询
	 * @param hql
	 * @return
	 */
	public List<HandleMonthlyVehicle> getByHql(String hql){
		return this.handleMonthlyVehicleDao.getByHQL(hql);
	}
	
	
	public HandleMonthlyVehicle get(String entityid){
		return handleMonthlyVehicleDao.get(entityid);
	}
	
	public List<HandleMonthlyVehicle> getAll() {
		return handleMonthlyVehicleDao.getAll();
	}
	
	public void add(HandleMonthlyVehicle handleMonthlyVehicle) {
		handleMonthlyVehicleDao.add(handleMonthlyVehicle);
	}
	public void merge(HandleMonthlyVehicle handleMonthlyVehicle) {
		handleMonthlyVehicleDao.merge(handleMonthlyVehicle);
	}
	
	public void update(HandleMonthlyVehicle handleMonthlyVehicle){
		handleMonthlyVehicleDao.update(handleMonthlyVehicle);
	}
	
	
}