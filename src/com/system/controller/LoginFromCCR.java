package com.system.controller;

import com.system.model.Manager;
import com.system.model.ResponseResult;
import com.system.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

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
    @RequestMapping("loginToSicnupatrol")
    public ModelAndView loginfromCcr(@RequestParam("token") String token){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setAccess_token(token);
        Manager managerInfo = tokenService.getManagerInfo(TokenService.getResponseResult());
        String username = managerInfo.getUsername();
        String loginkey = managerInfo.getPassword();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("username", username);
        modelAndView.addObject("loginkey", loginkey);
        modelAndView.setViewName("forward:/j_spring_security_check");
        return modelAndView;
    }
}
