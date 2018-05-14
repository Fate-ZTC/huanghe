package com.parkbobo.VO;

import java.util.List;

import com.parkbobo.model.FirePatrolBuildingType;

/**
 * @author lijunhong
 * @since 18/5/4 下午1:51
 * 用于保存巡查端列表头部导航栏数据
 */
public class FirePatrolUseBarListVO {


    private List<FirePatrolBuildingType> firePatrolBuildingTypes;

    public FirePatrolUseBarListVO() {}

    public List<FirePatrolBuildingType> getFirePatrolBuildingTypes() {
        return firePatrolBuildingTypes;
    }

    public void setFirePatrolBuildingTypes(List<FirePatrolBuildingType> firePatrolBuildingTypes) {
        this.firePatrolBuildingTypes = firePatrolBuildingTypes;
    }
}
