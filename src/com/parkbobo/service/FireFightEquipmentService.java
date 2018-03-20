package com.parkbobo.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FireFightEquipmentDao;
import com.parkbobo.model.FireFightEquipment;

@Service
public class FireFightEquipmentService {

	@Resource(name="fireFightEquipmentDaoImpl")
	private FireFightEquipmentDao fireFightEquipmentDao;
	
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
}
