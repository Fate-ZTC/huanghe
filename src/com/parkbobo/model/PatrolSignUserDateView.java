package com.parkbobo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 安防巡更签到-用户/签到日期视图
 * @version 1.0
 * @author RY
 * @since 2018-7-9 19:16:08
 */

@Entity
@Table(name = "view_patrol_sign_user_date")
public class PatrolSignUserDateView implements java.io.Serializable{

    private static final long serialVersionUID = 1768374312538986797L;
    /**
     * 主键ID
     */
    private Integer kid;
    /**
     * 工号
     */
    private String jobNum;
    /**
     * 签到日期
     */
    private String signDate;

    @Id
    @Column(name = "kid")
    public Integer getKid() {
        return kid;
    }

    public void setKid(Integer kid) {
        this.kid = kid;
    }

    @Column(name = "job_num")
    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    @Column(name = "sign_date")
    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }
}
