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
	@RequestMapping("patrolConfig_edit")
	public ModelAndView edit(PatrolConfig patrolConfig){
		ModelAndView mv = new ModelAndView();
		PatrolConfig config = this.patrolConfigService.getById(1);
		mv.addObject("patrolConfig",patrolConfig);
		if(patrolConfig.getRefreshTime()<1){
			mv.addObject("msg","更新间隔不能小于1秒");
			mv.setViewName("redirect:/patrolConfig_list");
			return mv;
		}
		if(patrolConfig.getUploadTime()<1){
			mv.addObject("msg","上传间隔不能小于1秒");
			mv.setViewName("redirect:/patrolConfig_list");
			return mv;
		}
		if(patrolConfig.getStartPatrolTime()<0){
			mv.addObject("msg","允许到达巡更区域时长不能小于0");
			mv.setViewName("redirect:/patrolConfig_list");
			return mv;
		}
		if(patrolConfig.getLeaveRegionTime()<0){
			mv.addObject("msg","允许巡更人员离开巡更范围时长不能小于0");
			mv.setViewName("redirect:/patrolConfig_list");
			return mv;
		}
		patrolConfig.setCampusNum(config.getCampusNum());
		patrolConfig.setIsEmergency(config.getIsEmergency());
		this.patrolConfigService.updateConfig(patrolConfig);
		mv.setViewName("redirect:/patrolConfig_list?method=editSuccess");
		return mv;
	}
}
