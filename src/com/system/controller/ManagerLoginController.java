package com.system.controller;

import java.io.Serializable;

import jsx3.app.Model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ManagerLoginController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8321745690164298156L;
	@RequestMapping("managerLogin")
	public ModelAndView execute(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("manager/login");
		return mv;
	}
}
