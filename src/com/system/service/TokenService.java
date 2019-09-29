package com.system.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.parkbobo.utils.HttpRequest;
import com.system.dao.ResourcesDao;
import com.system.dao.RoleDao;
import com.system.dao.RoleResourcesDao;
import com.system.dao.impl.ResourcesDaoImpl;
import com.system.dao.impl.RoleDaoImpl;
import com.system.model.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @ClassName GetTokenService
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/20 9:37
 * @Version 1.0
 **/
@Component("tokenService")
public class TokenService {
    @Resource(name = "roleDaoImpl")
    private RoleDao roleDao;
    @Resource(name = "resourcesDaoImpl")
    private ResourcesDao resourcesDao;
    @Resource(name = "roleResourcesDaoImpl")
    private RoleResourcesDao roleResourcesDao;
    private  static ResponseResult responseResult = new ResponseResult();

    public static ResponseResult getResponseResult() {
        return responseResult;
    }

    public static void setResponseResult(ResponseResult responseResult) {
        TokenService.responseResult = responseResult;
    }

    //得到token
    public  ResponseResult getToken(String username, String password)   {
            //读取配置文件
            Properties properties = new Properties();
            InputStream inputStream = this.getClass().getResourceAsStream("/server.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = HttpRequest.sendGet(properties.getProperty("oauthUrl") + "/oauth/token",
                    "username=" + username + "&" +
                            "password=" + password + "&" +
                            "client_id=" + properties.getProperty("client_id") + "&" +
                            "client_secret=" + properties.getProperty("client_secret") + "&" +
                            "grant_type=password"
            );
            try {
                String str = new String(result.getBytes("UTF-8"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            responseResult = JSONObject.parseObject(result, ResponseResult.class);
            responseResult.setNowTime(System.currentTimeMillis());
            return responseResult;
    }

    //刷新token
    //得到token
    public  ResponseResult refreshToken() {
        //读取配置文件
        Properties properties = new Properties();
        InputStream inputStream = Object.class.getResourceAsStream("/server.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = HttpRequest.sendGet(properties.getProperty("oauthUrl") + "/oauth/token",
                        "refresh_token=" + responseResult.getRefresh_token() + "&" +
                        "client_id=" + properties.getProperty("client_id") + "&" +
                        "client_secret=" + properties.getProperty("client_secret") + "&" +
                        "grant_type=refresh_token"
        );
        try {
            String str = new String(result.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        responseResult = JSONObject.parseObject(result, ResponseResult.class);
        responseResult.setNowTime(System.currentTimeMillis());
        return responseResult;
    }

    //根据Token获得用户信息
    public Manager getManagerInfo(ResponseResult responseResult) {
        //读取配置文件
        Properties properties = new Properties();
        InputStream inputStream = this.getClass().getResourceAsStream("/server.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String token = responseResult.getAccess_token();
        String type = responseResult.getToken_type();
        String result = HttpRequest.getInfoByToken(properties.getProperty("oauthUrl") + "/center/user/oauth", token, type);
        Result result1 = JSONObject.parseObject(result, Result.class);
        JSONObject jsonObject = (JSONObject) result1.getData();
        String password = jsonObject.getString("passWord");
        String username = jsonObject.getString("userCode");
        JSONArray jsonArray = jsonObject.getJSONArray("rules");
        Integer userId = jsonObject.getInteger("userId");
        Manager manager = new Manager();
        manager.setPassword(password);
        manager.setUserId(userId);
        manager.setUsername(username);
        Set<ManagerRole> managerRoles = new HashSet<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            String content = jsonArray.getJSONObject(i).getString("content");
            //将中控角色信息同步到本地角色表
            if(content.equals("admin")) {
                List<Role> roles = getRoleFormCCR(TokenService.getResponseResult());
                for (Role role1 : roles) {
                    String enname = role1.getEnname();
                    if (!roleDao.existsByProperty("enname", enname)) {
                        roleDao.merge(role1);
                    }
                }
            }
            Role role1 = roleDao.getUniqueByProperty("enname", content);
            if(role1 != null) {
                //得到角色对应的资源
                role1.setRoleResourceses(getRoles(role1).getRoleResourceses());
                ManagerRole managerRole = new ManagerRole();
                managerRole.setManager(manager);
                managerRole.setRole(role1);
                managerRoles.add(managerRole);
            }
        }
        manager.setManagerRoles(managerRoles);
        return manager;
    }

    //同步权限信息
    public Role getRoles(Role role) {
        //得到之前admin的权限
        List<RoleResources> roleResources = roleResourcesDao.getByHQL("from RoleResources as r where r.id.roleId = " + role.getRoleId());
        Set<RoleResources> roleResourcesSet = new HashSet<>();
        for (int i = 0; i < roleResources.size(); i++){
            roleResourcesSet.add(roleResources.get(i));
        }
        role.setRoleResourceses(roleResourcesSet);
        return role;
    }

    //得到中控的角色信息
    public  List<Role> getRoleFormCCR(ResponseResult responseResult) {
        //读取配置文件
        Properties properties = new Properties();
        InputStream inputStream = this.getClass().getResourceAsStream("/server.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String token = responseResult.getAccess_token();
        String type = responseResult.getToken_type();
        String result = HttpRequest.getRolesFormCCR(properties.getProperty("oauthUrl") + "/center/user/rule", token, type, "page=0&pageSize=100");
        Result result1 = JSONObject.parseObject(result, Result.class);
        JSONObject jsonObject = (JSONObject) result1.getData();
        JSONArray array = jsonObject.getJSONArray("content");
        List<Role> roles = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            String name = array.getJSONObject(i).getString("name");
            String content = array.getJSONObject(i).getString("content");
            Role role = new Role();
            role.setName(name);
            role.setEnname(content);
            role.setCreateTime(new Date());
            role.setEnable(1);
            role.setRoleType(1);
            role.setIscore(1);
            roles.add(role);
        }
       return roles;
    }

}
