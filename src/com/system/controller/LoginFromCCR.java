package com.system.controller;

import com.system.model.Manager;
import com.system.model.ResponseResult;
import com.system.service.TokenService;
import com.system.utils.HttpUtil;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LoginFromCCR
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/25 15:25
 * @Version 1.0
 **/
@Controller
public class LoginFromCCR {
    @Autowired
    private TokenService tokenService;

    @ResponseBody
    @RequestMapping("loginToSicnupatrol")
    public String loginfromCcr(@RequestParam(value = "token") String token){
        String url = "http://localhost:8080/j_spring_security_check";
        ResponseResult responseResult = new ResponseResult();
        responseResult.setAccess_token(token);
        responseResult.setToken_type("bearer");
        Manager managerInfo = tokenService.getManagerInfo(responseResult);
        String username = managerInfo.getUsername();
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("loginkey", "123456");
        String s = HttpUtil.doPost(url, map);
        if(s != null) {
            return s;
        }else {
            return "跳转失败!";
        }
       /* ResponseResult responseResult = new ResponseResult();
        responseResult.setAccess_token(token);
        responseResult.setToken_type("bearer");
        Manager managerInfo = tokenService.getManagerInfo(responseResult);
        String username = managerInfo.getUsername();
        String loginkey = managerInfo.getPassword();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("username", username);
        modelAndView.addObject("loginkey", loginkey);
        modelAndView.setViewName("redirect:/main_index");
        return  modelAndView;*/
    }
}
