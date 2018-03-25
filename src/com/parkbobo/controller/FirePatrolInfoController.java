package com.parkbobo.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.model.FirePatrolImg;
import com.parkbobo.model.FirePatrolInfo;
import com.parkbobo.service.FirePatrolExceptionService;
import com.parkbobo.service.FirePatrolImgService;
import com.parkbobo.service.FirePatrolInfoService;
import com.parkbobo.utils.PageBean;
import com.system.utils.StringUtil;

/**
 * 巡查记录
 * @author zj
 * @version 1.0
 */
@Controller
public class FirePatrolInfoController {
	
	@Resource
	private FirePatrolInfoService firePatrolInfoService;
	@Resource
	private FirePatrolExceptionService firePatrolExceptionService;
	@Resource
	private FirePatrolImgService firePatrolImgService; 
	
	@RequestMapping("firePatrolInfo_list")
	public ModelAndView list(FirePatrolInfo firePatrolInfo,Date startTime,Date endTime,Integer page,Integer pageSize) throws UnsupportedEncodingException
	{
		ModelAndView mv = new ModelAndView();
		
		String hql = "from  FirePatrolException f where 1=1";
		if(StringUtil.isNotEmpty(firePatrolInfo.getFireFightEquipment().getName())){
			hql +=" and f.fireFightEquipment.name like '% "+firePatrolInfo.getFireFightEquipment().getName()+"%'";
		}
		if(StringUtils.isNotBlank(firePatrolInfo.getFirePatrolUser().getUsername())){
			hql += " and f.firePatrolUser.username like '%" + firePatrolInfo.getFirePatrolUser().getUsername()+"%'";
		}
		if(firePatrolInfo.getPatrolStatus()!=null){
			hql += " and patrolStatus ="+firePatrolInfo.getPatrolStatus();
		}
		if(startTime!=null){
			hql += " and timestamp > '"+startTime+"'";
		}
		if(endTime!=null){
			hql += " and timestamp < '"+endTime+"'";
		}
		PageBean<FirePatrolInfo> firePatrolInfoPage = this.firePatrolInfoService.getByHql(hql,pageSize==null?12:pageSize, page==null?1:page);
		mv.addObject("firePatrolInfoPage", firePatrolInfoPage);
		mv.setViewName("manager/system/firePatrolInfo/firePatrolInfo-list");
		return mv;
	}
	/**
	 * 删除异常信息
	 * @return
	 */
	@RequestMapping("firePatrolExc_delete")
	public ModelAndView delete(String ids,HttpSession session)
	{
		ModelAndView mv = new ModelAndView();
		this.firePatrolInfoService.bulkDelete(ids);
		mv.setViewName("redirect:/firePatrolExc_list?method=deleteSuccess");
		return mv;
	}
	
	/**
	 * 显示所有异常信息
	 */
	@RequestMapping("showExcptions")
	public ModelAndView showExc(String exceptionTypes,Integer id){
		ModelAndView mv = new ModelAndView();
		FirePatrolInfo firePatrolInfo = this.firePatrolInfoService.get(id);
		String[] exceptions = null;
		if(exceptionTypes.length() > 0){
			String[] strs = exceptionTypes.split(",");
			exceptions = new String[strs.length];
			for (int i=0; i< strs.length; i++) {
				exceptions[i]=this.firePatrolExceptionService.getById(Integer.parseInt(strs[i])).getExceptionName();
			}
		}
		mv.addObject("exceptions",exceptions);
		mv.addObject("firePatrolInfo",firePatrolInfo);
		mv.setViewName("redirect:/firePatrolExc_list?method=deleteSuccess");
		return mv;
	}
	/**
	 * 展示图片
	 * @param id
	 * @return
	 */
	@RequestMapping("showImgs")
	public ModelAndView showImgs(Integer id){
		ModelAndView mv = new ModelAndView();
		List<FirePatrolImg> list = this.firePatrolImgService.getByp(id);
		mv.addObject("list",list);
		mv.setViewName("redirect:/firePatrolExc_list?method=deleteSuccess");
		return mv;
	}
}
