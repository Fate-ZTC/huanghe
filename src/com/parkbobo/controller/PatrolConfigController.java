package com.parkbobo.controller;

import javax.annotation.Resource;

import com.parkbobo.model.PatrolConfigTick;
import com.parkbobo.model.PatrolEquipmentManufacturer;
import com.parkbobo.model.PemModel;
import com.parkbobo.service.PatrolConfigTickService;
import com.parkbobo.service.PatrolEquipmentManufacturerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.model.PatrolConfig;
import com.parkbobo.service.PatrolConfigService;

import java.util.List;

/**
 * 巡更参数配置
 * @author zj
 *@version 1.0
 */
@Controller
public class PatrolConfigController {
	
	@Resource
	private PatrolConfigService patrolConfigService;
    @Resource
    private PatrolEquipmentManufacturerService patrolEquipmentManufacturerService;

	@Resource
	private PatrolConfigTickService patrolConfigTickService;




    /**
	 * 签到
	 */
	@RequestMapping("patrolConfig_list")
	public ModelAndView list()
	{
		ModelAndView mv = new ModelAndView();
		PatrolConfig patrolConfig = this.patrolConfigService.getById(1);
        List<PatrolEquipmentManufacturer> pList=patrolEquipmentManufacturerService.getAll();
		PatrolConfigTick patrolConfigTick=this.patrolConfigTickService.getById(1);
		mv.addObject("patrolConfig", patrolConfig);
        mv.addObject("pList",pList);
		mv.addObject("patrolConfigTick",patrolConfigTick);
		mv.setViewName("manager/system/patrolConfig/patrolConfig-list");
		return mv;
	}

	/**
	 * 进行的是后台配置
	 * @param patrolConfig
	 * @return
	 */
	@RequestMapping("patrolConfig_edit")
	public ModelAndView edit(PatrolConfig patrolConfig,PemModel pemModel,PatrolConfigTick patrolConfigTick){
		ModelAndView mv = new ModelAndView();
		PatrolConfig config = this.patrolConfigService.getById(1);
        PatrolConfigTick configTick=this.patrolConfigTickService.getById(1);
		mv.addObject("patrolConfig",patrolConfig);
		mv.addObject("patrolConfigTick",patrolConfigTick);
       List<PatrolEquipmentManufacturer> pList=pemModel.getpList();
		mv.addObject("pList",pList);
//		if(patrolConfig.getRefreshTime()<1){
//			mv.addObject("msg","更新间隔不能小于1秒");
//			mv.setViewName("redirect:/patrolConfig_list");
//			return mv;
//		}
//		if(patrolConfig.getUploadTime()<1){
//			mv.addObject("msg","上传间隔不能小于1秒");
//			mv.setViewName("redirect:/patrolConfig_list");
//			return mv;
//		}
//		if(patrolConfig.getStartPatrolTime()<0){
//			mv.addObject("msg","允许到达巡更区域时长不能小于0");
//			mv.setViewName("redirect:/patrolConfig_list");
//			return mv;
//		}
//		if(patrolConfig.getLeaveRegionTime()<0){
//			mv.addObject("msg","允许巡更人员离开巡更范围时长不能小于0");
//			mv.setViewName("redirect:/patrolConfig_list");
//			return mv;
//		}
		String ids="";
		for(PatrolEquipmentManufacturer p :pList){
            patrolEquipmentManufacturerService.merge(p);
            ids+=p.getManufacturerId();
            ids+=",";
        }
		patrolConfig.setManufacturerId(ids);
		if(patrolConfigTick.getIsLazyTime()==null){
			patrolConfig.setLazyTime(null);
		}
		if(patrolConfigTick.getIsLeaveRegionDistance()==null){
			patrolConfig.setLeaveRegionDistance(null);
		}
		if(patrolConfigTick.getIsLeaveRegionTime()==null){
			patrolConfig.setLeaveRegionTime(null);
		}
		if(patrolConfigTick.getIsManufacturerId()==null){
			patrolConfig.setManufacturerId(null);
		}
		if(patrolConfigTick.getIsRefreshTime()==null){
			patrolConfig.setRefreshTime(null);
		}
		if(patrolConfigTick.getIsSignRange()==null){
			patrolConfig.setSignRange(null);
		}
//		patrolConfig.setCampusNum(config.getCampusNum());
		patrolConfig.setOvertimeDeal(0);
		patrolConfig.setIsEmergency(config.getIsEmergency());
		this.patrolConfigService.updateConfig(patrolConfig);
		this.patrolConfigTickService.merge(patrolConfigTick);
		mv.setViewName("redirect:/patrolConfig_list?method=editSuccess");
		return mv;
	}


	/**
	 * 定位
	 */
	@RequestMapping("patrolConfig_list1")
	public ModelAndView list1()
	{
		ModelAndView mv = new ModelAndView();
		PatrolConfig patrolConfig = this.patrolConfigService.getById(2);
		mv.addObject("patrolConfig", patrolConfig);
		mv.setViewName("manager/system/patrolConfig/patrolConfig-list1");
		return mv;
	}

	/**
	 * 进行的是后台配置
	 * @param patrolConfig
	 * @return
	 */
	@RequestMapping("patrolConfig_edit1")
	public ModelAndView edit1(PatrolConfig patrolConfig){
		ModelAndView mv = new ModelAndView();
		PatrolConfig config = this.patrolConfigService.getById(2);
		mv.addObject("patrolConfig",patrolConfig);
		if(patrolConfig.getRefreshTime()<1){
			mv.addObject("msg","更新间隔不能小于1秒");
			mv.setViewName("redirect:/patrolConfig_list1");
			return mv;
		}
		if(patrolConfig.getUploadTime()<1){
			mv.addObject("msg","上传间隔不能小于1秒");
			mv.setViewName("redirect:/patrolConfig_list1");
			return mv;
		}
		if(patrolConfig.getStartPatrolTime()<0){
			mv.addObject("msg","允许到达巡更区域时长不能小于0");
			mv.setViewName("redirect:/patrolConfig_list1");
			return mv;
		}
		if(patrolConfig.getLeaveRegionTime()<0){
			mv.addObject("msg","允许巡更人员离开巡更范围时长不能小于0");
			mv.setViewName("redirect:/patrolConfig_list1");
			return mv;
		}
		patrolConfig.setCampusNum(config.getCampusNum());
		patrolConfig.setIsEmergency(config.getIsEmergency());
		this.patrolConfigService.updateConfig(patrolConfig);
		mv.setViewName("redirect:/patrolConfig_list1?method=editSuccess");
		return mv;
	}

}
