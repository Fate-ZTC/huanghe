package com.parkbobo.service;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.CarExitLogDao;
import com.parkbobo.dao.ParkPassVehicleDao;
import com.parkbobo.model.CarExitLog;
import com.parkbobo.model.ParkPassVehicle;

@Component("parkPassVehicleService")
public class ParkPassVehicleService {
	@Resource(name="parkPassVehicleDaoImpl")
	private ParkPassVehicleDao parkPassVehicleDao;

	/**
	 * HQL语句查询
	 * @param hql
	 * @return
	 */
	public List<ParkPassVehicle> getByHql(String hql){
		return this.parkPassVehicleDao.getByHQL(hql);
	}
	
	
	public ParkPassVehicle get(String entityid){
		return parkPassVehicleDao.get(entityid);
	}
	
	public List<ParkPassVehicle> getAll() {
		return parkPassVehicleDao.getAll();
	}
	
	public void add(ParkPassVehicle parkPassVehicle) {
		parkPassVehicleDao.add(parkPassVehicle);
	}
	
	/**
	 * 根据车牌号获取滞留在停车场的车辆
	 * */
	public ParkPassVehicle retentionCar(String carNumber){
		String hql = "from ParkPassVehicle where carplate='"+carNumber+"' order by passTime desc limit 1 offset 0";
//		System.out.println(hql);
		List<ParkPassVehicle> list = parkPassVehicleDao.getByHQL(hql);
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
	public ParkPassVehicle loadInCar(String carNumber){
		String hql = "from ParkPassVehicle where carplate='"+carNumber+"' and direct = 0 order by passTime desc limit 1 offset 0";
		List<ParkPassVehicle> list = parkPassVehicleDao.getByHQL(hql);
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
	public ParkPassVehicle loadByInunid(String inunid){
		return parkPassVehicleDao.getUniqueByProperty("inUnid", inunid);
	}
}