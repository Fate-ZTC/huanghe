package com.parkbobo.dao;

import java.util.List;

import com.parkbobo.model.PatrolUserRegion;

public interface PatrolUserRegionDao extends BaseDao<PatrolUserRegion>{
	public abstract List<Object[]> getBySql(String sql);
}
