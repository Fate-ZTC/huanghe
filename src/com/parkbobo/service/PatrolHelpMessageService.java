package com.parkbobo.service;

import com.parkbobo.dao.PatrolHelpMessageDao;
import com.parkbobo.model.PatrolHelpMessage;
import com.parkbobo.utils.PageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ZQ on 2019/2/20/020.
 */
@Service
public class PatrolHelpMessageService {
    @Resource(name = "patrolHelpMessageImpl")
    private PatrolHelpMessageDao patrolHelpMessageDao;


    public void add(PatrolHelpMessage patrolHelpMessage) {
        this.patrolHelpMessageDao.add(patrolHelpMessage);
    }

    public PageBean<PatrolHelpMessage> getPatrolHelpMessage(String hql, int pageSize, int page) {
        return this.patrolHelpMessageDao.pageQuery(hql,pageSize,page);
    }

    public PatrolHelpMessage getById(Integer id){
        return this.patrolHelpMessageDao.get(id);
    }

    public void merge(PatrolHelpMessage patrolHelpMessage) {
        this.patrolHelpMessageDao.merge(patrolHelpMessage);
    }

    public void update(PatrolHelpMessage patrolHelpMessage) {
        this.patrolHelpMessageDao.update(patrolHelpMessage);
    }

    public PageBean<PatrolHelpMessage> pageQuery(String hql, int pageSize, int page){
        return patrolHelpMessageDao.pageQuery(hql, pageSize, page);
    }

    public void bulkDelete(String ids) {
        if(ids.length() > 0) {
            String[] strs = ids.split(",");
            Integer[] idArr = new Integer[strs.length];
            for (int i=0; i< strs.length; i++) {
                idArr[i] = Integer.parseInt(strs[i]);
            }
            this.patrolHelpMessageDao.bulkDelete(idArr);
        }
    }

    public PatrolHelpMessage get(Integer id) {
        return this.patrolHelpMessageDao.get(id);
    }

    public List<PatrolHelpMessage> getByHql(String hql){
        return patrolHelpMessageDao.getByHQL(hql);
    }
}
