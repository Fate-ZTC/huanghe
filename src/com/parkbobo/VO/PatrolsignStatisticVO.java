package com.parkbobo.VO;

import java.util.Date;

/**
 * 巡更签到用户统计实体
 * @version 1.0
 * @author RY
 * @since 2018-7-10 14:11:28
 */

public class PatrolsignStatisticVO implements java.io.Serializable{
    private static final long serialVersionUID = -4733384127217297641L;
    /**
     * 工号
     */
    private String jobNum;
    /**
     * 姓名
     */
    private String username;
    /**
     * 应签
     */
    private Integer expectedCount;
    /**
     * 实签
     */
    private Integer signedCount;

    /**
     * 用户区域
     */
    private Integer usregId;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 异常次数
     */
    private Integer abnormalCount;

    public Integer getAbnormalCount() {
        return abnormalCount;
    }

    public void setAbnormalCount(Integer abnormalCount) {
        this.abnormalCount = abnormalCount;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getExpectedCount() {
        return expectedCount;
    }

    public void setExpectedCount(Integer expectedCount) {
        this.expectedCount = expectedCount;
    }

    public Integer getSignedCount() {
        return signedCount;
    }

    public void setSignedCount(Integer signedCount) {
        this.signedCount = signedCount;
    }

    public Integer getNoSignCount() {
        if(expectedCount != null && signedCount != null){
            return expectedCount - signedCount;
        }
        return 0;
    }

    public Integer getUsregId() {
        return usregId;
    }

    public void setUsregId(Integer usregId) {
        this.usregId = usregId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
