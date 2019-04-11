package com.parkbobo.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * 轨迹定位点辅助类
 * @vesion:wys
 * @author:2.1
 * @date:2019/4/9
 */
public class PatrolLocationInfoVo {
    /**
     * 点经纬度
     */
    private String geom;
    /**
     * 点上传时间
     */
    private String time;
    /**
     * 点状态
     */
    private String status;
    /**
     * 是否到达巡更区域，0为未到达，1为到达
     */
    private String isArrive;

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsArrive() {
        return isArrive;
    }

    public void setIsArrive(String isArrive) {
        this.isArrive = isArrive;
    }
}
