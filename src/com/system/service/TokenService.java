package com.system.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.parkbobo.utils.HttpRequest;
import com.system.dao.RoleDao;
import com.system.dao.impl.RoleDaoImpl;
import com.system.model.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

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
    private RoleDaoImpl roleDao;
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
        String result = HttpRequest.sendGet(properties.getProperty("oauthUrl"),
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
        String result = HttpRequest.sendGet(properties.getProperty("oauthUrl"),
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
        String token = responseResult.getAccess_token();
        String type = responseResult.getToken_type();
        String result = HttpRequest.getInfoByToken("https://testgis.you07.com/cmccr-server/center/user/oauth", token, type);
        Result result1 = JSONObject.parseObject(result, Result.class);
        JSONObject jsonObject = (JSONObject) result1.getData();
        String username = jsonObject.getString("userCode");
        JSONArray jsonArray = jsonObject.getJSONArray("rules");
        Integer userId = jsonObject.getInteger("userId");
        Manager manager = new Manager();
        manager.setUserId(65);
        manager.setUsername(username);
        Set<ManagerRole> managerRoles = new HashSet<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            String content = jsonArray.getJSONObject(i).getString("content");
            String name = jsonArray.getJSONObject(i).getString("name");
            Integer ruleId = jsonArray.getJSONObject(i).getInteger("ruleId");
            Role role = new Role();
            role.setRoleId(ruleId);
            role.setName(name);
            role.setEnname(content);
            role.setCreateTime(new Date());
            role.setEnable(1);
            role.setRoleType(1);
            role.setIscore(1);
            ManagerRole managerRole = new ManagerRole();
            managerRole.setRole(role);
            managerRole.setManager(manager);
            managerRoles.add(managerRole);
            //将中控角色信息同步到本地角色表
            roleDao.add(role);
        }
        manager.setManagerRoles(managerRoles);
        return manager;
    }


}
