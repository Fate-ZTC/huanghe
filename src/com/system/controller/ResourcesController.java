package com.system.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.utils.PageBean;
import com.system.model.Manager;
import com.system.model.Menu;
import com.system.model.Resources;
import com.system.service.MenuService;
import com.system.service.ResourcesService;
import com.system.utils.StringUtil;

/**
 * 资源权限管理
 * @author LH
 * @since 1.0
 */
@Controller
public class ResourcesController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4402074219894942360L;
	
	@Resource
	private ResourcesService resourcesService;
	@Resource(name="menuService")
	private MenuService menuService;

	
	/**
	 * 资源权限列表
	 */
	@RequestMapping("resources_list")
	public ModelAndView list(Resources resources,Integer pageSize,Integer page)
	{
		ModelAndView mv = new ModelAndView();
		String hql = "from Resources as re where 1=1";
		if(resources != null){
			if(resources.getName() != null && !resources.getName().equals("")){
				hql += " and re.name like '%" + resources.getName() + "%'";
			}
			if(resources.getMenu()!=null && resources.getMenu().getMenuId() != null && resources.getMenu().getMenuId() != -1){
				hql += " and re.menu.menuId = " + resources.getMenu().getMenuId();
			}
		}
		hql += " order by re.createTime desc";
		PageBean<Resources> resourcesPage = this.resourcesService.loadPage(hql, pageSize==null?12:pageSize, page==null?1:page);
		List<Menu> menuList = menuService.getAll();
		mv.addObject("resourcesPage",resourcesPage);
		mv.addObject("menuList", menuList);
		mv.setViewName("manager/system/resources/resources-list");
		return mv;
	}
	/**
	 * 删除资源权限
	 */
	@RequestMapping("resources_delete")
	public ModelAndView delete(String ids,HttpSession session)
	{
		ModelAndView mv = new ModelAndView(); 
		Manager user = (Manager) session.getAttribute("loginUser");
		this.resourcesService.bulkDelete(ids, user);
		mv.setViewName("redirect:/resources_list?method=deleteSuccess");
		return mv;
	}
	/**
	 * 添加资源权限
	 */
	@RequestMapping("resources_add")
	public ModelAndView add(String method,Resources resources,HttpSession session)
	{
		//添加
		ModelAndView mv = new ModelAndView(); 
		if(StringUtil.isNotEmpty(method) && method.equals("add"))
		{
			Manager user = (Manager) session.getAttribute("loginUser");
			resources.setCreateTime(new Date());
			resourcesService.add(resources, user);
			mv.setViewName("redirect:/resources_list?method=addSuccess");
		}
		//跳转到添加页面
		else
		{
			List<Menu> menuList = menuService.getAll();
			mv.addObject("menuList", menuList);
			mv.setViewName("manager/system/resources/resources-add");
		}
		return mv;
	}
	/**
	 * 编辑资源权限
	 */
	@RequestMapping("resources_edit")
	public ModelAndView edit(String method,Resources resources,HttpSession session,Integer id)
	{
		//编辑
		ModelAndView mv = new ModelAndView(); 
		if(StringUtil.isNotEmpty(method) && method.equals("edit"))
		{
			Manager user = (Manager) session.getAttribute("loginUser");
			resources.setCreateTime(new Date());
			resourcesService.update(resources, user);
			mv.setViewName("redirect:/resources_list?method=editSuccess");
		}
		//跳转到编辑页面
		else
		{
			resources = resourcesService.getById(id);
			List<Menu> menuList = menuService.getAll();
			mv.addObject("menuList", menuList);
			mv.addObject("resources", resources);
			mv.setViewName("manager/system/resources/resources-edit");
		}
		return mv;
	}
	
}
