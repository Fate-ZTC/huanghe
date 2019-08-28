package com.parkbobo.controller;

import com.alibaba.fastjson.JSONObject;
import com.parkbobo.service.LoginService;
import com.system.model.Department;
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
    public String managerLogin(@RequestParam("username") String username, @RequestParam("password") String passsword){
        Manager manager=new Manager();
        manager.setUsername(username);
        manager.setPassword(passsword);
        List<Role> roleList = loginService.managerLogin(manager);
        StringBuilder s = new StringBuilder();
        s.append("{");
        Department department = loginService.findDepartment(manager);
        if(department != null){
            s.append("\"name\"" +":" + "\""+manager.getUsername()+"\"" +",");
            s.append("\"departName\"" +":" +"\""+department.getName()+"\"" +",");
        }
        int i = 0;
        s.append("\"roles\""+":");
        s.append("[");
        for (Role role: roleList) {
            s.append("{");
            s.append("\"roleId\""+":" +role.getRoleId() + ",");
            s.append("\"roleName\"" +":"+ "\""+role.getName()+"\"" +",");
            s.append("\"enable\"" +":" + role.getEnable() + ",");
            s.append("\"enName\"" +":" +"\""+ role.getEnname()+"\"" + ",");
            s.append("\"roleType\""+":"+ role.getRoleType() + ",");
            s.append("\"iscore\""+":" + role.getIscore() + "");
            s.append("}");
            i++;
            if(i != roleList.size()){
                s.append(",");
            }
        }
        s.append("]");
        s.append("}");
        return s.toString();
    }
}
