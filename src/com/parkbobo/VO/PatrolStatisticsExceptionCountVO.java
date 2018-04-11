package com.parkbobo.VO;

/**
 * Created by lijunhong on 18/4/3.
 */
public class PatrolStatisticsExceptionCountVO {

    private int allCount;       //全部数量
    private int normalCount;    //正常数量
    private int exceptionCount; //异常数量


    public PatrolStatisticsExceptionCountVO() {}


    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getNormalCount() {
        return normalCount;
    }

    public void setNormalCount(int normalCount) {
        this.normalCount = normalCount;
    }

    public int getExceptionCount() {
        return exceptionCount;
    }

    public void setExceptionCount(int exceptionCount) {
        this.exceptionCount = exceptionCount;
    }
}
