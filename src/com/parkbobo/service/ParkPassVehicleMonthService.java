package com.parkbobo.service;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.ParkPassVehicleMonthDao;
import com.parkbobo.model.ParkPassVehicleMonth;

@Component("parkPassVehicleMonthService")
public class ParkPassVehicleMonthService {
	@Resource(name="parkPassVehicleMonthDaoImpl")
	private ParkPassVehicleMonthDao passVehicleMonthDao;

	/**
	 * HQL语句查询
	 * @param hql
	 * @return
	 */
	public List<ParkPassVehicleMonth> getByHql(String hql){
		return this.passVehicleMonthDao.getByHQL(hql);
	}
	
	
	public ParkPassVehicleMonth get(String entityid){
		return passVehicleMonthDao.get(entityid);
	}
	
	public List<ParkPassVehicleMonth> getAll() {
		return passVehicleMonthDao.getAll();
	}
	
	public void add(ParkPassVehicleMonth parkPassVehicle) {
		passVehicleMonthDao.add(parkPassVehicle);
	}
	
	/**
	 * 根据车牌号获取滞留在停车场的车辆
	 * */
	public ParkPassVehicleMonth retentionCar(String carNumber){
		String hql = "from ParkPassVehicleMonth where carplate='"+carNumber+"' order by passTime desc limit 1 offset 0";
		List<ParkPassVehicleMonth> list = passVehicleMonthDao.getByHQL(hql);
		if(list!=null && list.size()>0){
			if(list.get(0).getDirect().equals(0)){
				return list.get(0);
			}
		}
		return null;
		
	}
	
	/**
	 * 根据车牌号获取进场记录
	 * */
	public ParkPassVehicleMonth loadInCar(String carNumber){
		String hql = "from ParkPassVehicleMonth where carplate='"+carNumber+"' where direct = 0 order by passTime desc limit 1 offset 0";
		List<ParkPassVehicleMonth> list = passVehicleMonthDao.getByHQL(hql);
		if(list!=null && list.size()>0){
				return list.get(0);
		}
		return null;
		
	}
	
	/**
	 * 根据入场ID获取记录
	 * @param inunid
	 * @return
	 */
	public ParkPassVehicleMonth loadByInunid(String inunid){
		return passVehicleMonthDao.getUniqueByProperty("inUnid", inunid);
	}
}