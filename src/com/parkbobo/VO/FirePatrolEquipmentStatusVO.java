package com.parkbobo.VO;

/**
 * Created by lijunhong on 18/4/18.
 * 消防设备状态同步
 */
public class FirePatrolEquipmentStatusVO {

    private String deviceId;        //设备id
    private String patrolUser;      //巡查人员name
    private String deviceStatus;    //设备状态

    public FirePatrolEquipmentStatusVO() {}

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPatrolUser() {
        return patrolUser;
    }

    public void setPatrolUser(String patrolUser) {
        this.patrolUser = patrolUser;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    @Override
    public String toString() {
        return "FirePatrolEquipmentStatusVO{" +
                "deviceId='" + deviceId + '\'' +
                ", patrolUser='" + patrolUser + '\'' +
                ", deviceStatus='" + deviceStatus + '\'' +
                '}';
    }
}
