package com.system.dao.impl;

import com.parkbobo.dao.impl.BaseDaoSupport;
import com.system.dao.ManagerDao;
import com.system.dao.ManagerRoleDao;
import com.system.model.Manager;
import com.system.model.ManagerRole;
import org.springframework.stereotype.Component;

/**
 * @ClassName ManagerRoleDaoImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2019/8/20 16:14
 * @Version 1.0
 **/
@Component("managerRoleDaoImpl")
public class ManagerRoleDaoImpl extends BaseDaoSupport<ManagerRole>
        implements ManagerRoleDao {
}
