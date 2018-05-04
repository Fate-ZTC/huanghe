package com.parkbobo.service;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FirePatrolBuildingTypeDao;
import com.parkbobo.model.FirePatrolBuildingType;

/**
 * @author lijunhong
 * @since 18/5/2 下午11:18
 */
@Service("firePatrolBuildingTypeService")
public class FirePatrolBuildingTypeService {

    @Resource(name = "firePatrolBuildingTypeDaoImpl")
    private FirePatrolBuildingTypeDao firePatrolBuildingTypeDao;


    /**
     * 根据hql获取大楼分类信息
     * @param hql
     * @return
     */
    public List<FirePatrolBuildingType> getBuildingType(String hql) {
        return firePatrolBuildingTypeDao.getByHQL(hql);
    }

}
