package com.parkbobo.VO;

import java.util.Date;
import java.util.List;

import com.parkbobo.model.FireFightEquipmentHistory;
import com.parkbobo.model.FirePatrolInfo;
import com.parkbobo.utils.PageBean;

/**
 * @author lijunhong
 * @since 18/5/4 下午1:50
 * 主要用于，对巡查使用端进行数据统计
 */
public class FirePatrolUseStatisticsVO {


    private FirePatrolUseBarListVO firePatrolUseBarListVO;          //保存列表
    private FirePatrolUseEquNumVO firePatrolUseEquNumVO;            //保存统计个数
    private PageBean<FireFightEquipmentHistory> pageBean;           //设备数据
    private List<FireFightEquipmentHistory> list;                   //查询的设备
    private List<FirePatrolInfo> firePatrolInfoList;                //巡查记录
    private int buildingType;                                       //当前选中的类型
    private String jobNum;                                          //工号
    private Date date;                                              //时间
    private boolean isThisMonth;                                    //是否是本月
    private String dateStr;                                         //时间月份
    private int page;
    private int pageSize;
    private boolean isNextPage;


    public FirePatrolUseBarListVO getFirePatrolUseBarListVO() {
        return firePatrolUseBarListVO;
    }

    public void setFirePatrolUseBarListVO(FirePatrolUseBarListVO firePatrolUseBarListVO) {
        this.firePatrolUseBarListVO = firePatrolUseBarListVO;
    }

    public FirePatrolUseEquNumVO getFirePatrolUseEquNumVO() {
        return  firePatrolUseEquNumVO;
    }

    public void setFirePatrolUseEquNumVO(FirePatrolUseEquNumVO firePatrolUseEquNumVO) {
        this.firePatrolUseEquNumVO = firePatrolUseEquNumVO;
    }

    public List<FirePatrolInfo> getFirePatrolInfoList() {
        return firePatrolInfoList;
    }

    public void setFirePatrolInfoList(List<FirePatrolInfo> firePatrolInfoList) {
        this.firePatrolInfoList = firePatrolInfoList;
    }

    public int getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(int buildingType) {
        this.buildingType = buildingType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isThisMonth() {
        return isThisMonth;
    }

    public void setThisMonth(boolean thisMonth) {
        isThisMonth = thisMonth;
    }

    public PageBean<FireFightEquipmentHistory> getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean<FireFightEquipmentHistory> pageBean) {
        this.pageBean = pageBean;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isNextPage() {
        return isNextPage;
    }

    public void setNextPage(boolean nextPage) {
        isNextPage = nextPage;
    }

    public List<FireFightEquipmentHistory> getList() {
        return list;
    }

    public void setList(List<FireFightEquipmentHistory> list) {
        this.list = list;
    }
}
