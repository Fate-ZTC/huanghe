package com.parkbobo.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.model.PatrolConfig;
import com.parkbobo.service.PatrolConfigService;

/**
 * 巡更参数配置
 * @author zj
 *@version 1.0
 */
@Controller
public class PatrolConfigController {
	
	@Resource
	private PatrolConfigService patrolConfigService;
	
	@RequestMapping("patrolConfig_list")
	public ModelAndView list()
	{
		ModelAndView mv = new ModelAndView();
		PatrolConfig patrolConfig = this.patrolConfigService.getById(1);
		mv.addObject("patrolConfig", patrolConfig);
		mv.setViewName("manager/system/patrolConfig/patrolConfig-list");
		return mv;
	}
}
