package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FireFightEquipmentDao;
import com.parkbobo.model.FireFightEquipment;

@Service
public class FireFightEquipmentService {

	@Resource(name="fireFightEquipmentDaoImpl")
	private FireFightEquipmentDao fireFightEquipmentDao;
	
	public FireFightEquipment getById(Integer id){
		return this.fireFightEquipmentDao.get(id);
	}

	public void update(FireFightEquipment fireFightEquipment) {
		this.fireFightEquipmentDao.update(fireFightEquipment);
	}

}
