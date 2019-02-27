package com.parkbobo.model;

import java.util.Date;
import javax.persistence.*;

/**
 * Created by lijunhong on 18/4/18.
 */
@Entity
@Table(name="patrol_exception_push_info")
@SequenceGenerator(name="generator", sequenceName="patrol_exeception_push_info_id_seq", allocationSize = 1)
public class PatrolExceptionPushInfo {

    private Integer id;             //推送记录id
    private Integer campusNum;      //校区id
    private String jobNum;          //工号
    private double lat;             //最后一次经度
    private double lon;             //最后一次纬度
    private String exceptionName;   //异常名称
    private Integer exceptionType;   //异常类型
    private Integer exceptionId;     //异常id
    private Integer status;         //状态 是否异常   1正常  2异常
    private String userName;        //巡查员姓名
    private Integer usregId;        //区域和用户绑定id
    private Date pushDate;          //推送时间
    private Date exceptionDate;     //异常发生时间
    private String usregName;        //异常区域名称



    public PatrolExceptionPushInfo() {}


    @Id
    @Column(name="id",unique=true,nullable=false)
    @GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "campus_num")
    public Integer getCampusNum() {
        return campusNum;
    }

    public void setCampusNum(Integer campusNum) {
        this.campusNum = campusNum;
    }

    @Column(name = "job_num")
    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    @Column(name = "lat")
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Column(name = "lon")
    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Column(name = "exception_name")
    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    @Column(name = "exception_type")
    public Integer getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(Integer exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Column(name = "exception_id")
    public Integer getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(Integer exceptionId) {
        this.exceptionId = exceptionId;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "usregid")
    public Integer getUsregId() {
        return usregId;
    }

    public void setUsregId(Integer usregId) {
        this.usregId = usregId;
    }

    @Column(name = "push_date")
    public Date getPushDate() {
        return pushDate;
    }

    public void setPushDate(Date pushDate) {
        this.pushDate = pushDate;
    }

    @Column(name = "exception_date")
    public Date getExceptionDate() {
        return exceptionDate;
    }

    public void setExceptionDate(Date exceptionDate) {
        this.exceptionDate = exceptionDate;
    }

    @Column(name = "usregname")
    public String getUsregName() {
        return usregName;
    }

    public void setUsregName(String usregName) {
        this.usregName = usregName;
    }

//    @Override
//    public String toString() {
//        return "PatrolExceptionPushInfo{" +
//                "id=" + id +
//                ", campusNum=" + campusNum +
//                ", jobNum='" + jobNum + '\'' +
//                ", lat=" + lat +
//                ", lon=" + lon +
//                ", exceptionName=" + exceptionName +
//                ", exceptionType=" + exceptionType +
//                ", exceptionId=" + exceptionId +
//                ", status=" + status +
//                ", userName='" + userName + '\'' +
//                ", usregId=" + usregId +
//                '}';
//    }


    @Override
    public String toString() {
        return "PatrolExceptionPushInfo{" +
                "id=" + id +
                ", campusNum=" + campusNum +
                ", jobNum='" + jobNum + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", exceptionName='" + exceptionName + '\'' +
                ", exceptionType=" + exceptionType +
                ", exceptionId=" + exceptionId +
                ", status=" + status +
                ", userName='" + userName + '\'' +
                ", usregId=" + usregId +
                ", pushDate=" + pushDate +
                ", exceptionDate=" + exceptionDate +
                ", usregName='" + usregName + '\'' +
                '}';
    }
}
