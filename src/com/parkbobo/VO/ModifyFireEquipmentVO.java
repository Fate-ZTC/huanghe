package com.parkbobo.VO;

/**
 * Created by lijunhong on 18/4/9.
 * 消防设备相关请求类
 */
public class ModifyFireEquipmentVO {

    private Integer type;                        //操作类型:1、增加,2、删除,3、修改
    private Integer pointid;                     //pointid
    private FirePatrolEquipmentVO equipmentVO;  //实体对象


    public ModifyFireEquipmentVO() {}

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPointid() {
        return pointid;
    }

    public void setPointid(Integer pointid) {
        this.pointid = pointid;
    }

    public FirePatrolEquipmentVO getEquipmentVO() {
        return equipmentVO;
    }

    public void setEquipmentVO(FirePatrolEquipmentVO equipmentVO) {
        this.equipmentVO = equipmentVO;
    }
}
