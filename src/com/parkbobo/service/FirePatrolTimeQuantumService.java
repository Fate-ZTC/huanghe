package com.parkbobo.service;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FirePatrolTimeQuantumDao;
import com.parkbobo.model.FirePatrolTimeQuantum;

/**
 * @author lijunhong
 * @since 18/5/3 下午5:20
 */
@Service("firePatrolTimeQuantumService")
public class FirePatrolTimeQuantumService {

    @Resource
    private FirePatrolTimeQuantumDao firePatrolTimeQuantumDao;


    /**
     * 进行判断是否为开始时间
     * @param jobNum        工号
     * @param campusNum     校区id
     * @return
     */
    public boolean isStartTime(String jobNum,int campusNum) {
        String hql = "FROM FirePatrolTimeQuantum WHERE jobNum='" + jobNum + "' AND " + "campusNum=" + campusNum + " AND isNew=1";
        List<FirePatrolTimeQuantum> firePatrolTimeQuantums = firePatrolTimeQuantumDao.getByHQL(hql);
        //判断扫码的时候是否是开始巡查
        if(firePatrolTimeQuantums == null || firePatrolTimeQuantums.size() == 0) {
            return true;
        }else {
            if(firePatrolTimeQuantums.get(0).getEndTime() != null) {
                return true;
            }
        }
        return false;
    }


    /**
     * 添加巡查时间段
     * @param firePatrolTimeQuantum     巡查时间段
     */
    public void addFirePatrolTimeQuantum(FirePatrolTimeQuantum firePatrolTimeQuantum) {
        this.firePatrolTimeQuantumDao.add(firePatrolTimeQuantum);
    }

    /**
     * 进行更新
     * @param firePatrolTimeQuantum
     */
    public void updateFirePatrolTimeQuantum(FirePatrolTimeQuantum firePatrolTimeQuantum) {
        this.firePatrolTimeQuantumDao.update(firePatrolTimeQuantum);
    }

    /**
     * 获取时间端对象
     * @param hql
     * @return
     */
    public List<FirePatrolTimeQuantum> getByHql(String hql) {
        return this.firePatrolTimeQuantumDao.getByHQL(hql);
    }







}
