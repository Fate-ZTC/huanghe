package com.parkbobo.dao;

import java.util.List;

import com.parkbobo.model.PatrolUserRegion;

public interface PatrolUserRegionDao extends BaseDao<PatrolUserRegion>{
	List<Object[]> getBySql(String sql);


	double getDistanceBySql(String sql);
}
