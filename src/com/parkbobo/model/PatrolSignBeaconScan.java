package com.parkbobo.model;

/**
 * 巡更签到-蓝牙标签扫描信息
 * @version 1.0
 * @author RY
 * @since 2018-7-9 11:53:04
 */

public class PatrolSignBeaconScan implements java.io.Serializable{
    private static final long serialVersionUID = -7891553656592410554L;
    /**
     * 标签major参数
     */
    private Integer major;
    /**
     * 标签minor参数
     */
    private Integer minor;
    /**
     * 距离标签的距离
     */
    private Float distance;

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }
}
