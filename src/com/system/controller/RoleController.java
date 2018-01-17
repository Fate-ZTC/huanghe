package com.system.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.utils.PageBean;
import com.system.model.Manager;
import com.system.model.Role;
import com.system.service.ResourcesService;
import com.system.service.RoleService;
import com.system.utils.StringUtil;
/**
 * 角色管理
 * @author LH
 * @since 1.0
 */
@Controller
public class RoleController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8337140766523613719L;
	@Resource
	private RoleService roleService;
	@Resource
	private ResourcesService resourcesService;
	
	private String roleType;
	/**
	 * 角色列表
	 * @return
	 */
	@RequestMapping("role_list")
	public ModelAndView list(Integer pageSize,Integer page,Role role)
	{
		ModelAndView mv = new ModelAndView();
		String hql = "from Role as r where 1 = 1";
		if(role != null){
			if(role.getName() != null && !role.getName().equals("")){
				hql += " and r.name like '%" + role.getName() + "%'";
			}
			if(role.getEnable() !=null && role.getEnable() != -1){
				hql += " and r.enable = " + role.getEnable();
			}
		}
		hql += " order by r.createTime desc";
		PageBean<Role> rolePage = this.roleService.loadPage(hql,pageSize==null?12:pageSize, page==null?1:page);
		mv.addObject("rolePage", rolePage);
		mv.setViewName("manager/system/role/role-list");
		return mv;
	}
	@RequestMapping("role_add")
	public ModelAndView add(String resourcesIds,String method,Role role,HttpSession session)
	{
		//添加
		ModelAndView mv = new ModelAndView();
		if(StringUtil.isNotEmpty(method) && method.equals("add"))
		{
			Manager user = (Manager) session.getAttribute("loginUser");
			role.setCreateTime(new Date());
			roleService.add(resourcesIds, role, user);
			mv.setViewName("redirect:/role_list?method=addSuccess");
			return mv;
		}
		//跳转到添加页面
		else
		{
			String resourcesJson = resourcesService.loadTree("1");
			mv.addObject("resourcesJson",resourcesJson);
			mv.setViewName("manager/system/role/role-add");
		}
		return mv;
	}
	
	@RequestMapping("role_edit")
	public ModelAndView edit(String method,String resourcesIds,Role role,HttpSession session,Integer id)
	{
		//编辑
		ModelAndView mv = new ModelAndView();
		if(StringUtil.isNotEmpty(method) && method.equals("edit"))
		{
			Manager user = (Manager) session.getAttribute("loginUser");
			role.setCreateTime(new Date());
			roleService.update(resourcesIds, role, user);
			mv.setViewName("redirect:/role_list?method=editSuccess");
		}
		//跳转到编辑页面
		else
		{
			String resourcesJson = resourcesService.loadRoleTree(id);
			mv.addObject("resourcesJson",resourcesJson);
			role = roleService.getById(id);
			mv.addObject("role",role);
			mv.setViewName("manager/system/role/role-edit");
		}
		return mv;
	}
	/**
	 * 删除角色
	 * @return
	 */
	@RequestMapping("role_delete")
	public ModelAndView delete(String ids,HttpSession session)
	{
		ModelAndView mv = new ModelAndView();
		Manager user = (Manager) session.getAttribute("loginUser");
		this.roleService.bulkDelete(ids, user);
		mv.setViewName("redirect:/role_list?method=deleteSuccess");
		return mv;
	}
	/**
	 * 获取角色对应权限
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("role_getRoleResources")
	public void getRoleResources(String ids,HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			String json = resourcesService.loadTree(ids);
			out.print("[" + json + "]");
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"error\":\"\"} ");
		}finally{
			out.flush();
			out.close();
		}
	}
	
}
