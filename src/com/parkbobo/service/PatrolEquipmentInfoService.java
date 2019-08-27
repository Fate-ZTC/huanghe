package com.parkbobo.service;


import com.parkbobo.dao.PatrolEquipmentInfoDao;
import com.parkbobo.model.PatrolEquipmentInfo;
import com.parkbobo.utils.PageBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Component("PatrolEquipmentInfoService")
public class PatrolEquipmentInfoService {
    @Resource(name = "PatrolEquipmentInfoDaoImpl")
    private PatrolEquipmentInfoDao patrolEquipmentInfoDao;

    public PageBean<PatrolEquipmentInfo> pageQuery(String hql, int pageSize, int page){
        return patrolEquipmentInfoDao.pageQuery(hql, pageSize, page);
    }

    public List<PatrolEquipmentInfo> getByHql(String hql){
        return patrolEquipmentInfoDao.getByHQL(hql);
    }

    public PatrolEquipmentInfo get(Serializable beaconId){
        return patrolEquipmentInfoDao.get(beaconId);
    }

    public PatrolEquipmentInfo add(PatrolEquipmentInfo patrolEquipmentInfo){
        return patrolEquipmentInfoDao.add(patrolEquipmentInfo);
    }

    public void update(PatrolEquipmentInfo patrolEquipmentInfo){
        patrolEquipmentInfoDao.update(patrolEquipmentInfo);
    }

    public void delete(Serializable beaconId){
        patrolEquipmentInfoDao.delete(beaconId);
    }


    public PatrolEquipmentInfoDao getPatrolEquipmentInfoDao() {
        return patrolEquipmentInfoDao;
    }

    public void setPatrolEquipmentInfoDao(PatrolEquipmentInfoDao patrolEquipmentInfoDao) {
        this.patrolEquipmentInfoDao = patrolEquipmentInfoDao;
    }

    public PatrolEquipmentInfo getUniqueByPropertys(String[] propertyNames, Object[] values){
        return patrolEquipmentInfoDao.getUniqueByPropertys(propertyNames, values);
    }


}
