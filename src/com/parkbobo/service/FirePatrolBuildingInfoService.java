package com.parkbobo.service;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FirePatrolBuildingInfoDao;

/**
 * @author lijunhong
 * @since 18/5/3 下午9:10
 */
@Service("firePatrolBuildingInfoService")
public class FirePatrolBuildingInfoService {

    private FirePatrolBuildingInfoDao firePatrolBuildingInfoDao;


    /**
     * 根据sql计算点到面的距离
     * @param sql
     * @return
     */
    public double getDistance(String sql) {
        return this.firePatrolBuildingInfoDao.getDistanceBySql(sql);
    }


}
