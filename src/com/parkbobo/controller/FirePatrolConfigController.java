package com.parkbobo.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
/**
 * 配置文件
 * @author zj
 *@version 1.0
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.model.FirePatrolConfig;
import com.parkbobo.service.FirePatrolConfigService;
@Controller
public class FirePatrolConfigController {
	@Resource
	private FirePatrolConfigService firePatrolConfigService;
	
	@RequestMapping("firePatrolConfig_list")
	public ModelAndView list()
	{
		ModelAndView mv = new ModelAndView();
		FirePatrolConfig patrolConfig = this.firePatrolConfigService.getById(1);
		mv.addObject("firePatrolConfig", patrolConfig);
		mv.setViewName("manager/system/firePatrolConfig/patrolConfig-list");
		return mv;
	}
	@RequestMapping("firePatrolConfig_edit")
	public ModelAndView edit(FirePatrolConfig firePatrolConfig){
		ModelAndView mv = new ModelAndView();
		if(firePatrolConfig.getDistance()==null){
			mv.addObject("msg","请输入距离");
			mv.setViewName("redirect:/firePatrolConfig_list");
			return mv;
		}
		this.firePatrolConfigService.updateConfig(firePatrolConfig);
		mv.setViewName("redirect:/firePatrolConfig_list?method=editSuccess");
		return mv;
	}
}
