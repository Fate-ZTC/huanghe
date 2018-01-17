package com.system.controller;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.system.model.Sysconfig;
import com.system.service.SysconfigService;
import com.system.utils.StringUtil;

@Controller("sysconfigAction")
@Scope("prototype")
public class SysconfigController{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6515859749305836020L;
	@Resource
	private SysconfigService sysconfigService;
	private static Map<String, String> configMap = null;
	
	@RequestMapping("sysconfig_edit")
	public ModelAndView edit(String method,Map<String, String> configMap,HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		if(StringUtil.isNotEmpty(method) && method.equals("edit")){
			configMap = null;
			Enumeration<String> paramName = request.getParameterNames();
			while (paramName.hasMoreElements()) {
				String name = (String) paramName.nextElement();
				String value = request.getParameter(name);
				if(!name.equals("method")){
					if(StringUtil.isNotEmpty(value)){
						sysconfigService.updateValue(name,value);
					}else{
						sysconfigService.updateValue(name,null);
					}
				}
			}
			configMap = null;
			configMap = null;
			mv.setViewName("redirect:/system/sysconfig_edit?method=editSuccess");
		}else{
			mv.setViewName("manager/system/sysconfig/sysconfig-edit");
		}
		return mv;
	}
	
	
}
