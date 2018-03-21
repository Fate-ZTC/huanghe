package com.parkbobo.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FireFightEquipmentHistoryDao;
import com.parkbobo.model.FireFightEquipmentHistory;
import com.parkbobo.utils.PageBean;

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
	public void update(FireFightEquipmentHistory entity){
		this.fireFightEquipmentHistoryDao.update(entity);
	}
	public List<FireFightEquipmentHistory> getByHQL(String hql){
		return this.fireFightEquipmentHistoryDao.getByHQL(hql);
	}
	public List<FireFightEquipmentHistory> getByProperty(String propertyName ,Object value ,String orderBy,boolean isAsc){
		return this.fireFightEquipmentHistoryDao.getByProperty(propertyName, value, orderBy, isAsc);
	}
	public PageBean<FireFightEquipmentHistory> pageQuery(String hql, int pageSize,int page){
		return this.fireFightEquipmentHistoryDao.pageQuery(hql, pageSize, page);
	}
}
