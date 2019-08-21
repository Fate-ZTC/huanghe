package com.parkbobo.service;

import com.system.dao.ManagerDao;
import com.system.dao.RoleDao;
import com.system.model.Manager;
import com.system.model.ManagerRole;
import com.system.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName LoginService
 * @Description TODO
 * @Author Administrator
 * @Date 2019/8/21 11:08
 * @Version 1.0
 **/
@Service
public class LoginService {
    @Autowired
    ManagerDao managerDao;
    @Autowired
    RoleDao roleDao;
    public List<Role> managerLogin(Manager manager) {
        String username = manager.getUsername();
        String password = manager.getPassword();
        //将密码加密进行比较
        ShaPasswordEncoder sp = new ShaPasswordEncoder();
        String password1=sp.encodePassword(password, username);
        List<Manager> list=managerDao.getByHQL("from Manager as m where m.username='"+username+"'and m.password='"+password1+"'");
        Manager manager1=list.get(0);
        List<Role> roleList = new ArrayList<>();
        //得到角色ID
        Set<Integer> roleIdSet= new HashSet<>();
        Set<ManagerRole> managerRoles=manager1.getManagerRoles();
        for (ManagerRole managerRole: managerRoles) {
            roleIdSet.add(managerRole.getId().getRoleId());
        }
        Iterator<Integer> iterator = roleIdSet.iterator();
        while(iterator.hasNext()){
            roleList.add(roleDao.get(iterator.next()));
        }
        return roleList;
    }
}
