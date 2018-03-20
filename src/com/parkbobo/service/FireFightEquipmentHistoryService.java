package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FireFightEquipmentHistoryDao;
import com.parkbobo.model.FireFightEquipmentHistory;

@Service
public class FireFightEquipmentHistoryService {

	@Resource(name="fireFightEquipmentHistoryDaoImpl")
	private FireFightEquipmentHistoryDao fireFightEquipmentHistoryDao;
	
	public FireFightEquipmentHistory add(FireFightEquipmentHistory fireFightEquipmentHistory){
		return this.fireFightEquipmentHistoryDao.add(fireFightEquipmentHistory);
	}
}
