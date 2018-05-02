package com.parkbobo.dao;

import com.parkbobo.model.PatrolRegion;

public interface PatrolRegionDao extends BaseDao<PatrolRegion>{
    int updatePatrolRegionDao(String sql);
}
