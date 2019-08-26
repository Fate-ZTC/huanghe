package com.parkbobo.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * cofig是否勾选
 * @version 1.0
 * @author ZQ
 * @since 2018-12-28 14:30:18
 */
@Entity
@Table(name="patrol_config_tick")
@SequenceGenerator(name="generator", sequenceName="patrol_config_tick_tick_id_seq", allocationSize = 1)
public class PatrolConfigTick implements Serializable{

    private Integer tickId                 ;
    private Integer isSignRange           ;
    private Integer isRefreshTime         ;
    private Integer isLazyTime            ;
    private Integer isLeaveRegionDistance    ;
    private Integer isLeaveRegionTime      ;
    private Integer isManufacturerId      ;

    @Id
    @Column(name = "tick_id", unique = true, nullable = false)
    @GeneratedValue(generator = "generator", strategy =  GenerationType.AUTO)
    public Integer getTickId() {
        return tickId;
    }

    public void setTickId(Integer tickId) {
        this.tickId = tickId;
    }

    @Column(name = "is_sign_range")
    public Integer getIsSignRange() {
        return isSignRange;
    }

    public void setIsSignRange(Integer isSignRange) {
        this.isSignRange = isSignRange;
    }

    @Column(name = "is_refresh_time")
    public Integer getIsRefreshTime() {
        return isRefreshTime;
    }

    public void setIsRefreshTime(Integer isRefreshTime) {
        this.isRefreshTime = isRefreshTime;
    }

    @Column(name = "is_lazy_time")
    public Integer getIsLazyTime() {
        return isLazyTime;
    }

    public void setIsLazyTime(Integer isLazyTime) {
        this.isLazyTime = isLazyTime;
    }

    @Column(name = "is_leave_region_distance")
    public Integer getIsLeaveRegionDistance() {
        return isLeaveRegionDistance;
    }

    public void setIsLeaveRegionDistance(Integer isLeaveRegionDistance) {
        this.isLeaveRegionDistance = isLeaveRegionDistance;
    }

    @Column(name = "is_leave_region_time")
    public Integer getIsLeaveRegionTime() {
        return isLeaveRegionTime;
    }

    public void setIsLeaveRegionTime(Integer isLeaveRegionTime) {
        this.isLeaveRegionTime = isLeaveRegionTime;
    }

    @Column(name = "is_manufacturer_id")
    public Integer getIsManufacturerId() {
        return isManufacturerId;
    }

    public void setIsManufacturerId(Integer isManufacturerId) {
        this.isManufacturerId = isManufacturerId;
    }
}
