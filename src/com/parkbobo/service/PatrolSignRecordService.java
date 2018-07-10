package com.parkbobo.service;

import com.parkbobo.dao.PatrolSignRecordDao;
import com.parkbobo.model.PatrolSignRecord;
import com.parkbobo.utils.PageBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("patrolSignRecordService")
public class PatrolSignRecordService {
    @Resource(name = "patrolSignRecordDaoImpl")
    private PatrolSignRecordDao patrolSignRecordDao;

    public PageBean<PatrolSignRecord> pageQuery(String hql, int pageSize, int page){
        return patrolSignRecordDao.pageQuery(hql, pageSize, page);
    }

    public List<PatrolSignRecord> getByHql(String hql){
        return patrolSignRecordDao.getByHQL(hql);
    }

    public PatrolSignRecord add(PatrolSignRecord patrolSignRecord){
        return patrolSignRecordDao.add(patrolSignRecord);
    }

    public Integer countEffectiveWithTimeRange(String jobNum, String startTime, String endTime){
        String hql = "from PatrolSignRecord where jobNum = '" + jobNum
                + "' and signType = 1 and signTime > '" + startTime
                + "' and signTime < '" + endTime
                + "'";
        return patrolSignRecordDao.pageQuery(hql, 1, 1).getAllRow();
    }

    public Boolean checkSignedWithTimeRange(String jobNum, String startTime, String endTime, Integer pointId){
        String hql = "from PatrolSignRecord where jobNum = '" + jobNum
                + "' and patrolSignPointInfo.pointId = " + pointId
                + " and signTime > '" + startTime
                + "' and signTime < '" + endTime
                + "'";
        return patrolSignRecordDao.pageQuery(hql, 1, 1).getAllRow() > 0;
    }

    public PatrolSignRecordDao getPatrolSignRecordDao() {
        return patrolSignRecordDao;
    }

    public void setPatrolSignRecordDao(PatrolSignRecordDao patrolSignRecordDao) {
        this.patrolSignRecordDao = patrolSignRecordDao;
    }
}
