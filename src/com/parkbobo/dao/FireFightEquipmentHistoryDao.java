package com.parkbobo.dao;

import java.util.List;

import com.parkbobo.model.FireFightEquipmentHistory;

public interface FireFightEquipmentHistoryDao extends BaseDao<FireFightEquipmentHistory>{
	public abstract List<Object[]> getBySql(String sql);
}
