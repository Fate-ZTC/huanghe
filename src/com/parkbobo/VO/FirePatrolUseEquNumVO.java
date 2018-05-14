package com.parkbobo.VO;

/**
 * @author lijunhong
 * @since 18/5/4 下午1:53
 * 用于保存设备数
 */
public class FirePatrolUseEquNumVO extends PatrolStatisticsExceptionCountVO{


    private int totalcheckcount;    //巡查次数
    private String month;           //时间


    public FirePatrolUseEquNumVO() {}

    public int getTotalcheckcount() {
        return totalcheckcount;
    }

    public void setTotalcheckcount(int totalcheckcount) {
        this.totalcheckcount = totalcheckcount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
