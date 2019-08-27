package com.parkbobo.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.parkbobo.model.FireFightEquipmentHistory;
import org.springframework.stereotype.Service;

import com.parkbobo.dao.FireFightEquipmentDao;
import com.parkbobo.model.FireFightEquipment;

@Service
public class FireFightEquipmentService {

	@Resource(name="fireFightEquipmentDaoImpl")
	private FireFightEquipmentDao fireFightEquipmentDao;

	@Resource
	private FireFightEquipmentService fireFightEquipmentService;
	@Resource
	private FireFightEquipmentHistoryService fireFightEquipmentHistoryService;

	public List<FireFightEquipment> getAll(){
		return fireFightEquipmentDao.getAll();
	}
	public List<FireFightEquipment> getByProperty(String propertyName ,Object value){
		return fireFightEquipmentDao.getByProperty(propertyName, value);
	}
	public List<FireFightEquipment> getByProperty(String propertyName ,Object value ,String orderBy,boolean isAsc){
		return fireFightEquipmentDao.getByProperty(propertyName, value, orderBy, isAsc);
	}
	public List<FireFightEquipment> getByProperty(String[] propertyNames ,Object[] values){
		return fireFightEquipmentDao.getByPropertys(propertyNames, values);
	}
	
	
	public FireFightEquipment getById(Integer id){
		return this.fireFightEquipmentDao.get(id);
	}

	public void update(FireFightEquipment fireFightEquipment) {
		this.fireFightEquipmentDao.update(fireFightEquipment);
	}
	public void updateAll(){
		this.fireFightEquipmentDao.bulkUpdate("update FireFightEquipment set checkStatus = 0", null);
	}



	/**
	 * 根据楼层id和大楼id查询室内消防设备信息
	 * @param buildingCode		大楼id
	 * @param floorId			楼层id
	 * @return
	 */
	public List<FireFightEquipment> getIndoorEquipment(String buildingCode,String floorId) {
		if(buildingCode != null && floorId != null) {
			String hql = "FROM FireFightEquipment WHERE floorId='" + floorId + "' AND buildingCode='" + buildingCode+"'";
			List<FireFightEquipment> fireFightEquipments = this.fireFightEquipmentDao.getByHQL(hql);
			return fireFightEquipments;
		}
		return null;
	}


	/**
	 * 根据楼层id和大楼id查询室内消防设备信息
	 * @param buildingCode		大楼id
	 * @param floorId			楼层id
	 * @return
	 */
	public List<FireFightEquipment> getOutdoorEquipment(String buildingCode,String floorId) {
		if(buildingCode == null && floorId != null) {
			String hql = "FROM FireFightEquipment WHERE floorId='" + floorId + "' AND buildingCode is null";
			List<FireFightEquipment> fireFightEquipments = this.fireFightEquipmentDao.getByHQL(hql);
			return fireFightEquipments;
		}
		return null;
	}


	/**
	 * 重置所有消防设备的巡查状态
	 */

	public void copeToHistoryTask() {
		System.out.println("执行复制");
		List<FireFightEquipment> fireFightEquipments = fireFightEquipmentService.getAll();
		for (FireFightEquipment fireFightEquipment: fireFightEquipments) {
			FireFightEquipmentHistory history = new FireFightEquipmentHistory();
			history.setCampusNum(fireFightEquipment.getCampusNum());
			//这里每个月需要进行对巡查状态进行变更,所有设备为未巡查状态
//			history.setCheckStatus(fireFightEquipment.getCheckStatus());
			history.setCheckStatus((short)0);			//改为为巡查状态
			history.setOldId(fireFightEquipment.getId());
			//这里是每月的时间
			history.setLastUpdateTime(new Date());
			history.setLat(fireFightEquipment.getLat());
			history.setLon(fireFightEquipment.getLon());
			history.setStatus(fireFightEquipment.getStatus());
			history.setName(fireFightEquipment.getName());
			//设置楼层id
			if(null != fireFightEquipment.getFloorid()) {
				history.setFloorid(fireFightEquipment.getFloorid());
			}
			//设置大楼id
			if(null != fireFightEquipment.getBuildingCode()) {
				history.setBuildingCode(fireFightEquipment.getBuildingCode());
			}
			this.fireFightEquipmentHistoryService.add(history);
		}
	}

	public void restCheckStatusTask() {
		System.out.println("执行重置");
		this.fireFightEquipmentService.updateAll();
	}
}
