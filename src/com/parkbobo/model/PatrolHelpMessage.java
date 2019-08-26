package com.parkbobo.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 安防巡更求救信息实体
 * @version 1.0
 * @author ZQ
 * @since 2019-02-20 10:24:18
 */

@Entity
@Table(name="patrol_help_message")
@SequenceGenerator(name="generator", sequenceName="patrol_help_message_id_seq", allocationSize = 1)
public class PatrolHelpMessage {

    private Integer id           ;//        主键id
    private String userCode    ;//        求救人账号
    private String userName    ;//        求救人姓名
    private Double lat          ;//        经度
    private Double lon          ;//        纬度
    private Date helpTime    ;//        求救时间
    private Integer isRead      ;//        是否已读,0未读，1已读
    private Integer campusNum; //校区id

    @Id
    @Column(name="id",unique=true,nullable=false)
    @GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "user_code")
    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "lat")
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Column(name = "lon")
    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Column(name = "help_time")
    public Date getHelpTime() {
        return helpTime;
    }

    public void setHelpTime(Date helpTime) {
        this.helpTime = helpTime;
    }

    @Column(name = "is_read")
    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    @Column(name = "campus_num")
    public Integer getCampusNum() {
        return campusNum;
    }

    public void setCampusNum(Integer campusNum) {
        this.campusNum = campusNum;
    }
}
