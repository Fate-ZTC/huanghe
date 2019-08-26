package com.parkbobo.service;

import com.parkbobo.dao.PatrolConfigTickDao;
import com.parkbobo.model.PatrolConfigTick;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2019/1/21/021.
 */
@Service
public class PatrolConfigTickService {
    @Resource
    private PatrolConfigTickDao patrolConfigTickDao;
    /**
     * 根据id查询
     */
    public PatrolConfigTick getById(Integer id){
        return this.patrolConfigTickDao.get(id);
    }

    /**
     * 修改
     */
    public void merge(PatrolConfigTick patrolConfigTick){
        patrolConfigTickDao.merge(patrolConfigTick);
    }
}
