package com.system.controller;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.utils.PageBean;
import com.system.model.OptLogs;
import com.system.service.OptLogsService;
import com.system.utils.StringUtil;
/**
 * 系统日志
 * @author LH
 * @since 1.0
 */
@Controller
public class OptLogsController{
	private static final long serialVersionUID = -5338138550681650355L;
	
	@Resource
	private OptLogsService optLogsService;
	
	
	/**
	 * 日志列表
	 */
	@RequestMapping("optLogs_list")
	public ModelAndView list(Integer pageSize,Integer page,String keywords)
	{	
		ModelAndView mv = new ModelAndView();
		String hql = "from OptLogs as t where 1=1 ";
		if(StringUtil.isNotEmpty(keywords)){
			hql += " and (t.fromModel like '%" + keywords + "%' or t.username like '%" + keywords + "%')";
		}
		hql += " order by t.createTime desc";
		PageBean<OptLogs> optLogsPage = this.optLogsService.loadPage(hql,pageSize==null?12:pageSize, page==null?1:page);
		mv.addObject("optLogsPage", optLogsPage);
		mv.setViewName("manager/system/optLogs/optLogs-list");
		return mv;
	}
	/**
	 * 删除日志
	 */
	@RequestMapping("optLogs_delete")
	public ModelAndView delete(String ids)
	{
		ModelAndView mv = new ModelAndView();
		this.optLogsService.bulkDelete(ids);
		mv.setViewName("redirect:/optLogs_list?method=deleteSuccess");
		return mv;
	}
	
}
