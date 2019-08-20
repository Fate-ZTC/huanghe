package com.system.service;

import com.system.dao.ManagerDao;
import com.system.dao.ManagerRoleDao;
import com.system.dao.impl.ManagerRoleDaoImpl;
import com.system.model.ManagerRole;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName ManagerRoleService
 * @Description TODO
 * @Author Administrator
 * @Date 2019/8/20 16:10
 * @Version 1.0
 **/
@Component("managerRoleService")
public class ManagerRoleService {
    @Resource(name = "managerRoleDaoImpl")
    private ManagerRoleDao managerRoleDao;
    //添加
    public void add(ManagerRole managerRole) {
        managerRole = managerRoleDao.add(managerRole);
    }
}
