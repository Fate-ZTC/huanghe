package com.parkbobo.dao;

import java.util.List;

import com.parkbobo.model.PatrolLocationInfo;

public interface PatrolLocationInfoDao extends BaseDao<PatrolLocationInfo>{
	public abstract List<Integer> getBySql(String sql);
}
