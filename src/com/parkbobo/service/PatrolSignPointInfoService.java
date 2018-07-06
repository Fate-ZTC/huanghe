package com.parkbobo.service;

import com.parkbobo.dao.PatrolSignPointInfoDao;
import com.parkbobo.model.PatrolSignPointInfo;
import com.parkbobo.utils.PageBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Component("patrolSignPointInfoService")
public class PatrolSignPointInfoService {
    @Resource(name = "patrolSignPointInfoDaoImpl")
    private PatrolSignPointInfoDao patrolSignPointInfoDao;

    public PageBean<PatrolSignPointInfo> pageQuery(String hql, int pageSize, int page){
        return patrolSignPointInfoDao.pageQuery(hql, pageSize, page);
    }

    public List<PatrolSignPointInfo> getByHql(String hql){
        return patrolSignPointInfoDao.getByHQL(hql);
    }

    public PatrolSignPointInfo get(Serializable pointId){
        return patrolSignPointInfoDao.get(pointId);
    }

    public PatrolSignPointInfo add(PatrolSignPointInfo patrolSignPointInfo){
        return patrolSignPointInfoDao.add(patrolSignPointInfo);
    }

    public void update(PatrolSignPointInfo patrolSignPointInfo){
        patrolSignPointInfoDao.update(patrolSignPointInfo);
    }

    public void delete(Serializable pointId){
        patrolSignPointInfoDao.delete(pointId);
    }

    public PatrolSignPointInfoDao getPatrolSignPointInfoDao() {
        return patrolSignPointInfoDao;
    }

    public void setPatrolSignPointInfoDao(PatrolSignPointInfoDao patrolSignPointInfoDao) {
        this.patrolSignPointInfoDao = patrolSignPointInfoDao;
    }
}
