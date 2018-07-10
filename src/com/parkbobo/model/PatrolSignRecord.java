package com.parkbobo.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 巡更签到记录信息
 * @version 1.0
 * @author RY
 * @since 2018-7-9 11:02:59
 */

@Entity
@Table(name="patrol_sign_record")
@SequenceGenerator(name="generator", sequenceName="patrol_sign_record_record_id_seq", allocationSize = 1)
public class PatrolSignRecord implements java.io.Serializable{

    private static final long serialVersionUID = -1059093881736748178L;
    /**
     * 签到记录ID
     */
    private Integer recordId;
    /**
     * 签到点位信息
     */
    private PatrolSignPointInfo patrolSignPointInfo;
    /**
     * 工号
     */
    private String jobNum;
    /**
     * 姓名
     */
    private String username;
    /**
     * 签到时间
     */
    private Date signTime;
    /**
     * 签到类型，1签到，2未签自动生成
     */
    private Integer signType;
    /**
     * 定位类型，1蓝牙，2GPS
     */
    private Integer signMode;
    /**
     * 未签时间范围，签到类型为未签自动生成时有效
     */
    private String noSignRange;

    @Id
    @Column(name = "record_id", unique = true, nullable = false)
    @GeneratedValue(generator = "generator", strategy = GenerationType.AUTO)
    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "point_id")
    public PatrolSignPointInfo getPatrolSignPointInfo() {
        return patrolSignPointInfo;
    }

    public void setPatrolSignPointInfo(PatrolSignPointInfo patrolSignPointInfo) {
        this.patrolSignPointInfo = patrolSignPointInfo;
    }

    @Column(name = "job_num")
    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "sign_time")
    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    @Column(name = "sign_type")
    public Integer getSignType() {
        return signType;
    }

    public void setSignType(Integer signType) {
        this.signType = signType;
    }

    @Column(name = "sign_mode")
    public Integer getSignMode() {
        return signMode;
    }

    public void setSignMode(Integer signMode) {
        this.signMode = signMode;
    }

    @Column(name = "no_sign_range")
    public String getNoSignRange() {
        return noSignRange;
    }

    public void setNoSignRange(String noSignRange) {
        this.noSignRange = noSignRange;
    }
}
