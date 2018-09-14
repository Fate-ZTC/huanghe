package com.parkbobo.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 安防巡更暂停信息实体
 * @version 1.0
 * @author RY
 * @since 2018-7-10 13:40:17
 */

@Entity
@Table(name = "patrol_pause")
@SequenceGenerator(name = "generator", sequenceName = "patrol_pause_pause_id_seq", allocationSize = 1)
public class PatrolPause implements java.io.Serializable{

    private static final long serialVersionUID = -8713082120722733294L;
    /**
     * 暂停信息ID
     */
    private Integer pauseId;
    /**
     * 暂停开始时间
     */
    private Date pauseStart;
    /**
     * 暂停结束时间
     */
    private Date pauseEnd;
    /**
     * 暂停原因
     */
    private String cause;
    /**
     * 暂停发起人账号
     */
    private String usercode;
    /**
     * 暂停发起人姓名
     */
    private String username;

    @Id
    @Column(name = "pause_id", unique = true, nullable = false)
    @GeneratedValue(generator = "generator", strategy = GenerationType.AUTO)
    public Integer getPauseId() {
        return pauseId;
    }

    public void setPauseId(Integer pauseId) {
        this.pauseId = pauseId;
    }

    @Column(name = "pause_start")
    public Date getPauseStart() {
        return pauseStart;
    }

    public void setPauseStart(Date pauseStart) {
        this.pauseStart = pauseStart;
    }

    @Column(name = "pause_end")
    public Date getPauseEnd() {
        return pauseEnd;
    }

    public void setPauseEnd(Date pauseEnd) {
        this.pauseEnd = pauseEnd;
    }

    @Column(name = "cause")
    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    @Column(name = "usercode")
    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Transient
    public String getCheckDuration(){
        String dm = null;
        if(pauseEnd != null && pauseStart != null) {
            long second = 0l;
            second = (pauseEnd.getTime()-pauseStart.getTime())/1000;
            long hours = second / 3600;            //转换小时
            second = second % 3600;                //剩余秒数
            long minutes = second /60;            //转换分钟
            second = second % 60;                //剩余秒数
            dm=hours+":"+minutes+":"+second;
        }
        return dm;
    }
}
