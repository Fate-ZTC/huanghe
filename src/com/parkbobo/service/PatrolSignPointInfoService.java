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

    public Integer countWithRegionId(Integer regionId){
        String hql = "from PatrolSignPointInfo where patrolRegion.id = " + regionId;
        return patrolSignPointInfoDao.pageQuery(hql, 1, 1).getAllRow();
    }

    public Boolean isInRegion(Integer regionId, Double lng, Double lat){
        return patrolSignPointInfoDao.isInRegion(regionId, lng, lat);
    }

    public PatrolSignPointInfoDao getPatrolSignPointInfoDao() {
        return patrolSignPointInfoDao;
    }

    public void setPatrolSignPointInfoDao(PatrolSignPointInfoDao patrolSignPointInfoDao) {
        this.patrolSignPointInfoDao = patrolSignPointInfoDao;
    }
    public void merge(PatrolSignPointInfo patrolSignPointInfo){
        patrolSignPointInfoDao.merge(patrolSignPointInfo);
    }

    public void addRecord(PatrolSignPointInfo patrolSignPointInfo) {
        this.patrolSignPointInfoDao.add(patrolSignPointInfo);
    }
    public int updateBySql(String sql) {
        if(sql != null && !"".equals(sql)) {
            int result = this.patrolSignPointInfoDao.updatePatrolRegionDao(sql);
            return result;
        }
        return 0;
    }


}
