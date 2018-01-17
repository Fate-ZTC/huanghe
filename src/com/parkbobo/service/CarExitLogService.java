package com.parkbobo.service;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.CarExitLogDao;
import com.parkbobo.model.CarExitLog;

@Component("carExitLogService")
public class CarExitLogService {
	@Resource(name="carExitLogDaoImpl")
	private CarExitLogDao carExitLogDao;

	/**
	 * HQL语句查询
	 * @param hql
	 * @return
	 */
	public List<CarExitLog> getByHql(String hql){
		return this.carExitLogDao.getByHQL(hql);
	}
	
	
	public CarExitLog get(String entityid){
		return carExitLogDao.get(entityid);
	}
	
	public List<CarExitLog> getAll() {
		return carExitLogDao.getAll();
	}
	
	public void add(CarExitLog carExitLog) {
		carExitLogDao.add(carExitLog);
	}
}