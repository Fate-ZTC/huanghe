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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String, Object> managerLogin(@RequestParam("username") String username, @RequestParam("password") String passsword){
        Manager manager=new Manager();
        manager.setUsername(username);
        manager.setPassword(passsword);
        List<Role> roleList = null;
        Map<String, Object> map=new HashMap<String,Object>();
        try {
            roleList = loginService.managerLogin(manager);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status",401);
            map.put("message","用户名或密码错误");
            return map;
        }


        Department department = loginService.findDepartment(manager);
        if(department != null){
            map.put("name",manager.getUsername());
            map.put("departName",department.getName());
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Role role: roleList) {
            Map<String, Object> map1=new HashMap<String,Object>();
            map1.put("roleId",role.getRoleId());
            map1.put("roleName",role.getName());
            map1.put("enable",role.getEnable());
            map1.put("enName",role.getEnname());
            map1.put("roleType",role.getRoleType());
            map1.put("iscore",role.getIscore());
            list.add(map1);
        }
        map.put("roles",list);

        /*StringBuilder s = new StringBuilder();
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
        return s.toString();*/

        return map;
    }
}
