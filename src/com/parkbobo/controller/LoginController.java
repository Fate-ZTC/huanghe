package com.parkbobo.controller;

import com.parkbobo.service.LoginService;
import com.system.model.Manager;
import com.system.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author Administrator
 * @Date 2019/8/21 11:07
 * @Version 1.0
 **/
@Controller
public class LoginController {

    @Autowired
    LoginService loginService;
    /**
     * @Author ztc
     * @Description 登录接口
     * @Date 9:18 2019/8/21
     * @Param
     * @return
     **/
    @RequestMapping("user_login")
    @ResponseBody
    public List<Role> managerLogin(@RequestParam("username") String username, @RequestParam("password") String passsword){
        Manager manager=new Manager();
        manager.setUsername(username);
        manager.setPassword(passsword);
        List<Role> roleList = loginService.managerLogin(manager);
        List<Role> roleList1=new ArrayList<>();
        for (Role role: roleList) {
            Role role1=new Role();
            role1.setRoleId(role.getRoleId());
            role1.setName(role.getName());
            role1.setCreateTime(role.getCreateTime());
            role1.setEnable(role.getEnable());
            role1.setEnname(role.getEnname());
            role1.setRoleType(role.getRoleType());
            role1.setIscore(role.getIscore());
            roleList1.add(role1);
        }
        return roleList1;
    }
}
