package com.parkbobo.VO;

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

}
