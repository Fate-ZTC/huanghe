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
		//TODO 这里需要对设备历史记录表进行同步
		this.fireFightEquipmentHistoryDao.merge(entity);
	}
	public List<FireFightEquipmentHistory> getByHQL(String hql) {
		return this.fireFightEquipmentHistoryDao.getByHQL(hql);
	}
	public List<FireFightEquipmentHistory> getByProperty(String propertyName ,Object value ,String orderBy,boolean isAsc) {
		return this.fireFightEquipmentHistoryDao.getByProperty(propertyName, value, orderBy, isAsc);
	}
	public PageBean<FireFightEquipmentHistory> pageQuery(String hql, int pageSize,int page){
		return this.fireFightEquipmentHistoryDao.pageQuery(hql, pageSize, page);
	}


	/**
	 * 批量添加设备历史表
	 * @param fireFightEquipmentHistories
     */
	public void batchAdd(List<FireFightEquipmentHistory> fireFightEquipmentHistories) {
		this.fireFightEquipmentHistoryDao.bulksave(fireFightEquipmentHistories);
	}
}
