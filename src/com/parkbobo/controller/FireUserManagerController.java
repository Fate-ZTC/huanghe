package com.parkbobo.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.model.FirePatrolUser;
import com.parkbobo.service.FirePatrolUserService;
import com.parkbobo.utils.PageBean;
import com.system.utils.StringUtil;

/**
 * 消防巡查人员管理
 * @author zj
 *@version 1.0
 */
@Controller
public class FireUserManagerController {

	@Resource
	private FirePatrolUserService firePatrolUserService;

	/**
	 * 巡查人员
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("firePatrolUserList")
	public ModelAndView list(FirePatrolUser firePatrolUser,Integer page,Integer pageSize) throws UnsupportedEncodingException
	{
		ModelAndView mv = new ModelAndView();
		
		String hql = "from  FirePatrolUser f where isDel = 0";
		if(firePatrolUser != null && StringUtil.isNotEmpty(firePatrolUser.getUsername()))
		{
			hql+=" and f.username like '%" + firePatrolUser.getUsername() + "%'";
		}
		if(StringUtil.isNotEmpty(firePatrolUser.getJobNum())){
			hql +=" and f.jobNum like '% "+firePatrolUser.getJobNum()+"%'";
		}
		hql += " order by f.id";
		PageBean<FirePatrolUser> firePatrolUserPage = this.firePatrolUserService.getUsers(hql,pageSize==null?12:pageSize, page==null?1:page);
		mv.addObject("firePatrolUserPage", firePatrolUserPage);
		mv.addObject("firePatrolUser",firePatrolUser);
		mv.setViewName("manager/system/firePatrol/fireUser-list");
		return mv;
	}
	@RequestMapping("firePatrolUser_add")
	public ModelAndView add(String method,FirePatrolUser firePatrolUser,HttpSession session )
	{
		ModelAndView mv = new ModelAndView();
		//添加
		if(StringUtil.isNotEmpty(method) && method.equals("add"))
		{
			FirePatrolUser byJobNum = this.firePatrolUserService.getByJobNum(firePatrolUser.getJobNum());
			if(byJobNum!=null){
				mv.setViewName("manager/system/firePatrol/fireUser-add");
				mv.addObject("msg","工号已存在,请更换");
				return mv;
			}
			Date date = new Date();
			firePatrolUser.setCreateTime(date);
			firePatrolUser.setCampusNum(1);
			firePatrolUser.setIsDel((short)0);
			firePatrolUser.setLastUpdateTime(date);
			firePatrolUserService.addUser(firePatrolUser);
			mv.setViewName("redirect:/firePatrolUserList?method=addSuccess");
		}
		//跳转到添加页面
		else
		{
			mv.setViewName("manager/system/firePatrol/fireUser-add");
		}
		return mv;
	}
	@RequestMapping("firePatrolUser_edit")
	public ModelAndView edit(String method,FirePatrolUser firePatrolUser,HttpSession session,Integer id)
	{
		ModelAndView mv = new ModelAndView();
		//编辑
		if(StringUtil.isNotEmpty(method) && method.equals("edit"))
		{
			firePatrolUser.setLastUpdateTime(new Date());
			firePatrolUserService.update(firePatrolUser);
			mv.setViewName("redirect:/firePatrolUserList?method=editSuccess");
		}
		//跳转到编辑页面
		else
		{
			firePatrolUser = firePatrolUserService.getById(id);
			mv.addObject("firePatrolUser", firePatrolUser);
			mv.setViewName("manager/system/firePatrol/fireUser-edit");
		}
		return mv;
	}
	/**
	 * 删除巡查人员
	 * @return
	 */
	@RequestMapping("firePatrolUser_delete")
	public ModelAndView delete(String ids,HttpSession session)
	{
		ModelAndView mv = new ModelAndView();
		if(ids==null){
			mv.setViewName("redirect:/firePatrolUserList");
			mv.addObject("msg","请勾选信息");
			return mv;
		}
		String[] idArr = ids.split(",");
		for(int i = 0;i < idArr.length;i++){
			int id = Integer.parseInt(idArr[i]);
			FirePatrolUser patrolUser = this.firePatrolUserService.getById(id);
			patrolUser.setIsDel((short)1);
			this.firePatrolUserService.update(patrolUser);
		}
		mv.setViewName("redirect:/firePatrolUserList?method=deleteSuccess");
		return mv;
	}
}
