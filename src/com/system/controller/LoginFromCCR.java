package com.system.controller;

import com.system.model.Manager;
import com.system.model.ResponseResult;
import com.system.service.TokenService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
    public ModelAndView loginfromCcr(@RequestParam(value = "token") String token){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setAccess_token(token);
        responseResult.setToken_type("bearer");
        Manager managerInfo = tokenService.getManagerInfo(responseResult);
        String username = managerInfo.getUsername();
        String loginkey = managerInfo.getPassword();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("username", username);
        modelAndView.addObject("loginkey", loginkey);
        modelAndView.setViewName("redirect:/main_index");
        return  modelAndView;
    }
}
