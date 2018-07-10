package com.parkbobo.service;

import com.parkbobo.dao.PatrolPauseDao;
import com.parkbobo.model.PatrolPause;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("patrolPauseService")
public class PatrolPauseService {
    @Resource(name = "patrolPauseDaoImpl")
    private PatrolPauseDao patrolPauseDao;

    public PatrolPause add(PatrolPause patrolPause){
        return patrolPauseDao.add(patrolPause);
    }

    public void update(PatrolPause patrolPause){
        patrolPauseDao.update(patrolPause);
    }

    public PatrolPause checkPauseStatus(){
        String hql = "from PatrolPause where pauseEnd is not null order by pauseStart desc";
        List<PatrolPause> pauseList = patrolPauseDao.getByHQL(hql);
        if(pauseList.size() > 0){
            return pauseList.get(0);
        } else{
            return null;
        }
    }

    public List<PatrolPause> getByHql(String hql){
        return patrolPauseDao.getByHQL(hql);
    }

    public PatrolPauseDao getPatrolPauseDao() {
        return patrolPauseDao;
    }

    public void setPatrolPauseDao(PatrolPauseDao patrolPauseDao) {
        this.patrolPauseDao = patrolPauseDao;
    }
}
