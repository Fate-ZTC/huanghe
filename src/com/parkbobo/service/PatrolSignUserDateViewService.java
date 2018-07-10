package com.parkbobo.service;

import com.parkbobo.dao.PatrolSignUserDateViewDao;
import com.parkbobo.model.PatrolSignUserDateView;
import com.parkbobo.utils.PageBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("patrolSignUserDateViewService")
public class PatrolSignUserDateViewService {
    @Resource(name = "patrolSignUserDateViewDaoImpl")
    private PatrolSignUserDateViewDao patrolSignUserDateViewDao;

    public PageBean<PatrolSignUserDateView> pageQuery(String hql, int pageSize, int page){
        return patrolSignUserDateViewDao.pageQuery(hql, pageSize, page);
    }

    public PatrolSignUserDateViewDao getPatrolSignUserDateViewDao() {
        return patrolSignUserDateViewDao;
    }

    public void setPatrolSignUserDateViewDao(PatrolSignUserDateViewDao patrolSignUserDateViewDao) {
        this.patrolSignUserDateViewDao = patrolSignUserDateViewDao;
    }
}
