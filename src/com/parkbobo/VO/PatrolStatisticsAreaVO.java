package com.parkbobo.VO;

import java.util.List;

/**
 * Created by lijunhong on 18/4/3.
 * 用于统计区域中巡查信息安防
 */
public class PatrolStatisticsAreaVO {

    private Integer id;             //用户区域id
    private String username;        //用户名
    private Integer regionId;       //区域id
    private String jobNum;         //工号
    private String startTime;       //开始时间
    private String endTime;         //结束时间
    private Integer status;         //异常状态 1正常2异常
    private String date;            //时间(根据开始时间)
    private Integer campusNum;      //校区id
    private String regionName;      //区域名称
    private List<PatrolExceptionVO> exceptionVOs;   //异常信息

    public PatrolStatisticsAreaVO() {}


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getCampusNum() {
        return campusNum;
    }

    public void setCampusNum(Integer campusNum) {
        this.campusNum = campusNum;
    }

    public List<PatrolExceptionVO> getExceptionVOs() {
        return exceptionVOs;
    }

    public void setExceptionVOs(List<PatrolExceptionVO> exceptionVOs) {
        this.exceptionVOs = exceptionVOs;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
