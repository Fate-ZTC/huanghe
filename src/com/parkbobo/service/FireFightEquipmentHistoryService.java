package com.parkbobo.service;

import java.util.List;

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
	public List<Object[]> getBySql(String sql){
		return this.fireFightEquipmentHistoryDao.getBySql(sql);
	}
	public List<FireFightEquipmentHistory> getByHQL(String hql){
		return this.fireFightEquipmentHistoryDao.getByHQL(hql);
	}
}
