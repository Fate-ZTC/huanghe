package com.parkbobo.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.model.FirePatrolException;
import com.parkbobo.service.FirePatrolExceptionService;
import com.parkbobo.utils.PageBean;
import com.system.utils.StringUtil;

@Controller
public class FirePatrolExceptionController {

	private FirePatrolExceptionService firePatrolExceptionService;

	@RequestMapping("firePatrolExc_list")
	public ModelAndView list(FirePatrolException firePatrolException,Integer page,Integer pageSize) throws UnsupportedEncodingException
	{
		ModelAndView mv = new ModelAndView();
		String hql = "from  FirePatrolException f where 1=1";
		if(firePatrolException!=null){
			if(StringUtil.isNotEmpty(firePatrolException.getExceptionName())){
				hql +=" and f.exceptionName like '% "+firePatrolException.getExceptionName()+"%'";
			}
		}
		hql += " order by f.sort";
		PageBean<FirePatrolException> firePatrolExceptionPage = this.firePatrolExceptionService.getByHql(hql,pageSize==null?12:pageSize, page==null?1:page);
		mv.addObject("firePatrolExceptionPage", firePatrolExceptionPage);
		mv.setViewName("manager/system/firePatrolExc/firePatrolExc-list");
		return mv;
	}

	@RequestMapping("firePatrolExc_add")
	public ModelAndView add(String method,FirePatrolException firePatrolException,HttpSession session )
	{
		ModelAndView mv = new ModelAndView();
		//添加
		if(StringUtil.isNotEmpty(method) && method.equals("add"))
		{
			if(firePatrolException!=null){
				firePatrolException.setUpdateTime(new Date());
				this.firePatrolExceptionService.addRecord(firePatrolException);
			}
			mv.setViewName("redirect:/firePatrolExc_list?method=addSuccess");
		}
		//跳转到添加页面
		else
		{
			mv.setViewName("manager/system/firePatrolExc/firePatrolExc-add");
		}
		return mv;
	}
	@RequestMapping("firePatrolExc_edit")
	public ModelAndView edit(String method,FirePatrolException firePatrolException,HttpSession session,Integer id)
	{
		ModelAndView mv = new ModelAndView();
		//编辑
		if(StringUtil.isNotEmpty(method) && method.equals("edit"))
		{
			firePatrolException.setUpdateTime(new Date());
			this.firePatrolExceptionService.updateRecord(firePatrolException);
			mv.setViewName("redirect:/firePatrolExc_list?method=editSuccess");
		}
		//跳转到编辑页面
		else
		{
			firePatrolException = this.firePatrolExceptionService.getById(id);
			mv.addObject("firePatrolException", firePatrolException);
			mv.setViewName("manager/system/firePatrolExc/firePatrolExc-edit");
		}
		return mv;
	}
	/**
	 * 删除部门
	 * @return
	 */
	@RequestMapping("firePatrolExc_delete")
	public ModelAndView delete(String ids,HttpSession session)
	{
		ModelAndView mv = new ModelAndView();
		this.firePatrolExceptionService.bulkDelete(ids);
		mv.setViewName("redirect:/firePatrolExc_list?method=deleteSuccess");
		return mv;
	}
}
