package com.parkbobo.dao;

import com.parkbobo.model.PatrolSignPointInfo;

public interface PatrolSignPointInfoDao extends BaseDao<PatrolSignPointInfo>{
    Boolean isInRegion(Integer regionId, Double lng, Double lat);

    int updatePatrolRegionDao(String sql);
}
