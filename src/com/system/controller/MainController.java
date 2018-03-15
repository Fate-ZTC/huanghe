package com.system.controller;

import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.system.model.Sysconfig;
/**
 * 登录主页面
 * @author LH
 * @since 1.0
 */
@Controller("mainAction")
@Scope("session")
public class MainController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -183944415128545745L;
	
	
	
	@RequestMapping("main_index")
	public ModelAndView index()
	{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("manager/main");
		return mv;
	}
	/**
	 * 工作台
	 * @return
	 */
	@RequestMapping("main_desktop")
	public ModelAndView desktop(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("manager/desktop");
		return mv;
	}
}
