package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolExceptionPushInfoDao;
import com.parkbobo.model.PatrolExceptionPushInfo;
import com.parkbobo.utils.PageBean;

/**
 * Created by lijunhong on 18/4/18.
 */
@Service("patrolExceptionPushInfoService")
public class PatrolExceptionPushInfoService {

    @Resource
    private PatrolExceptionPushInfoDao patrolExceptionPushInfoDao;


    /**
     * 保存推送记录
     * @param pushInfo
     */
    public void addExceptionPushInfo(PatrolExceptionPushInfo pushInfo) {
        patrolExceptionPushInfoDao.add(pushInfo);
    }


    /**
     * 根据分页获取推送异常激烈
     * @param hql           hql
     * @param pageSize      每页大小
     * @param page          当前页
     * @return
     */
    public PageBean<PatrolExceptionPushInfo> getExceptionPushInfo(String hql,int pageSize,int page) {
        return this.patrolExceptionPushInfoDao.pageQuery(hql,pageSize,page);
    }

}
