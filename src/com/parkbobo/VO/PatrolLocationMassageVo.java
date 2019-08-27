package com.parkbobo.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * 轨迹信息
 * @vesion:wys
 * @author:2.1
 * @date:2019/4/9
 */
public class PatrolLocationMassageVo {
    /**
     * 是否异常，0为否，1为是
     */
    private Integer isAbnormal;
    /**
     * 异常类型
     */
    private Integer type;
    /**
     * 提示信息
     */
    private String massage;
    /**
     * 提示显示时间
     */
    private String time;

    private List<PatrolLocationInfoVo> patrolLocationInfoVos = new ArrayList<>();

    public Integer getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(Integer isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public List<PatrolLocationInfoVo> getPatrolLocationInfoVos() {
        return patrolLocationInfoVos;
    }

    public void setPatrolLocationInfoVos(List<PatrolLocationInfoVo> patrolLocationInfoVos) {
        this.patrolLocationInfoVos = patrolLocationInfoVos;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
