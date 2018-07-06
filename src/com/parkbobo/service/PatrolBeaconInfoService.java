package com.parkbobo.service;

import com.parkbobo.dao.PatrolBeaconInfoDao;
import com.parkbobo.model.PatrolBeaconInfo;
import com.parkbobo.utils.PageBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Component("patrolBeaconInfoService")
public class PatrolBeaconInfoService {
    @Resource(name = "patrolBeaconInfoDaoImpl")
    private PatrolBeaconInfoDao patrolBeaconInfoDao;

    public PageBean<PatrolBeaconInfo> pageQuery(String hql, int pageSize, int page){
        return patrolBeaconInfoDao.pageQuery(hql, pageSize, page);
    }

    public List<PatrolBeaconInfo> getByHql(String hql){
        return patrolBeaconInfoDao.getByHQL(hql);
    }

    public PatrolBeaconInfo get(Serializable beaconId){
        return patrolBeaconInfoDao.get(beaconId);
    }

    public PatrolBeaconInfo add(PatrolBeaconInfo patrolBeaconInfo){
        return patrolBeaconInfoDao.add(patrolBeaconInfo);
    }

    public void update(PatrolBeaconInfo patrolBeaconInfo){
        patrolBeaconInfoDao.update(patrolBeaconInfo);
    }

    public void delete(Serializable beaconId){
        patrolBeaconInfoDao.delete(beaconId);
    }

    public PatrolBeaconInfoDao getPatrolBeaconInfoDao() {
        return patrolBeaconInfoDao;
    }

    public void setPatrolBeaconInfoDao(PatrolBeaconInfoDao patrolBeaconInfoDao) {
        this.patrolBeaconInfoDao = patrolBeaconInfoDao;
    }

    public PatrolBeaconInfo getUniqueByPropertys(String[] propertyNames, Object[] values){
        return patrolBeaconInfoDao.getUniqueByPropertys(propertyNames, values);
    }
}
