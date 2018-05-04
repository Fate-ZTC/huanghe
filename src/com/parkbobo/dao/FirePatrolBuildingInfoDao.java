package com.parkbobo.dao;

import com.parkbobo.model.FirePatrolBuildingInfo;

/**
 * @author lijunhong
 * @since 18/5/3 下午9:04
 */
public interface FirePatrolBuildingInfoDao extends BaseDao<FirePatrolBuildingInfo> {

    double getDistanceBySql(String sql);
}
