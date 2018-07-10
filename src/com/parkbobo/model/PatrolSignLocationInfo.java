package com.parkbobo.model;

import java.util.List;

/**
 * 巡更签到-APP上传的用户位置信息实体
 * @version 1.0
 * @author RY
 * @since 2018-7-9 11:50:00
 */

public class PatrolSignLocationInfo implements java.io.Serializable{
    private static final long serialVersionUID = 8826945653791594613L;
    /**
     * GPS定位经纬度
     */
    private String lngLat;
    /**
     * 用户巡更区域选择ID
     */
    private Integer usregId;
    /**
     * 定位时间，延时签到时有效，格式：yyyy-MM-dd HH:mm:ss
     */
    private String locationTime;
    /**
     * 扫描到的蓝牙标签
     */
    private List<PatrolSignBeaconScan> beaconList;

    public String getLngLat() {
        return lngLat;
    }

    public void setLngLat(String lngLat) {
        this.lngLat = lngLat;
    }

    public Integer getUsregId() {
        return usregId;
    }

    public void setUsregId(Integer usregId) {
        this.usregId = usregId;
    }

    public String getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(String locationTime) {
        this.locationTime = locationTime;
    }

    public List<PatrolSignBeaconScan> getBeaconList() {
        return beaconList;
    }

    public void setBeaconList(List<PatrolSignBeaconScan> beaconList) {
        this.beaconList = beaconList;
    }
}
