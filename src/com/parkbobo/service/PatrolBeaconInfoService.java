package com.parkbobo.service;

import com.parkbobo.dao.PatrolBeaconInfoDao;
import com.parkbobo.dao.PatrolSignPointInfoDao;
import com.parkbobo.model.PatrolBeaconInfo;
import com.parkbobo.model.PatrolSignPointInfo;
import com.parkbobo.utils.PageBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Component("patrolBeaconInfoService")
public class PatrolBeaconInfoService {
    @Resource(name = "patrolBeaconInfoDaoImpl")
    private PatrolBeaconInfoDao patrolBeaconInfoDao;
    @Resource(name = "patrolSignPointInfoDaoImpl")
    private PatrolSignPointInfoDao patrolSignPointInfoDao;

    /**
     * 根据标签ID、点位ID
     * 更新点位绑定的标签
     * 先取消其他标签与该点位的绑定
     * 再将该标签与点位绑定
     * @param beaconId
     * @param pointId
     */
    public void updatePointInfo(Integer beaconId, Integer pointId){
        String unbindHql = "update PatrolBeaconInfo set patrolSignPointInfo = null where patrolSignPointInfo.pointId = ?";
        Object[] values = {pointId};
        patrolBeaconInfoDao.bulkUpdate(unbindHql, values);

        PatrolBeaconInfo beaconInfo = patrolBeaconInfoDao.get(beaconId);
        PatrolSignPointInfo pointInfo = patrolSignPointInfoDao.get(pointId);
        beaconInfo.setPatrolSignPointInfo(pointInfo);
        patrolBeaconInfoDao.update(beaconInfo);
    }

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

    public PatrolSignPointInfoDao getPatrolSignPointInfoDao() {
        return patrolSignPointInfoDao;
    }

    public void setPatrolSignPointInfoDao(PatrolSignPointInfoDao patrolSignPointInfoDao) {
        this.patrolSignPointInfoDao = patrolSignPointInfoDao;
    }
}
