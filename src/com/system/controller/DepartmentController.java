package com.system.controller;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.parkbobo.dao.FirePatrolInfoDao;
import com.parkbobo.model.FirePatrolUser;
import com.system.dao.ManagerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.utils.PageBean;
import com.system.model.Department;
import com.system.model.Manager;
import com.system.service.DepartmentService;
import com.system.utils.StringUtil;
/**
 * 部门管理
 * @author LH
 * @since 1.0
 */
@Controller
public class DepartmentController {
	@Resource
	private FirePatrolInfoDao firePatrolInfoDao;
	/**
	 *
	 */
	private static final long serialVersionUID = -1620751832599338243L;
	@Resource
	private DepartmentService departmentService;
	private PageBean<Department> departmentPage;
	/**
	 * 部门列表
	 * @return
	 */
	@RequestMapping("department_list")
	public ModelAndView list(Department department,Integer page,Integer pageSize)
	{
		ModelAndView mv = new ModelAndView();
		String hql = "from Department as d where 1=1";
		if(department != null && StringUtil.isNotEmpty(department.getName()))
		{
			hql+=" and d.name like '%" + department.getName() + "%'";
		}
		hql += " order by d.orderid";
		departmentPage = this.departmentService.loadPage(hql,pageSize==null?12:pageSize, page==null?1:page);
		mv.addObject("departmentPage", departmentPage);
		mv.setViewName("manager/system/department/department-list");
		return mv;
	}
	@RequestMapping("department_listNew")
	public ModelAndView listNew(Department department,Integer page,Integer pageSize)
	{
		ModelAndView mv = new ModelAndView();
		String hql = "from Department as d where 1=1";
		if(department != null && StringUtil.isNotEmpty(department.getName()))
		{
			hql+=" and d.name like '%" + department.getName() + "%'";
		}
		hql += " order by d.orderid";
		departmentPage = this.departmentService.loadPage(hql,pageSize==null?12:pageSize, page==null?1:page);
		mv.addObject("departmentPage", departmentPage);
		mv.setViewName("manager/system/department/department-listNew");
		return mv;
	}
	@RequestMapping("department_add")
	public ModelAndView add(String method,Department department,HttpSession session )
	{
		ModelAndView mv = new ModelAndView();
		//添加
		if(StringUtil.isNotEmpty(method) && method.equals("add"))
		{
			Manager user = (Manager) session.getAttribute("loginUser");
			department.setCreateTime(new Date());
			departmentService.add(department, user);
			mv.setViewName("redirect:/department_list?method=addSuccess");
		}
		//跳转到添加页面
		else
		{
			mv.setViewName("manager/system/department/department-add");
		}
		return mv;
	}
	@RequestMapping("department_edit")
	public ModelAndView edit(String method,Department department,HttpSession session,Integer id)
	{
		ModelAndView mv = new ModelAndView();
		//编辑
		if(StringUtil.isNotEmpty(method) && method.equals("edit"))
		{
			Manager user = (Manager) session.getAttribute("loginUser");
			department.setCreateTime(new Date());
			departmentService.update(department, user);
			mv.setViewName("redirect:/department_list?method=editSuccess");
		}
		//跳转到编辑页面
		else
		{
			department = departmentService.getById(id);
			mv.addObject("department", department);
			mv.setViewName("manager/system/department/department-edit");
		}
		return mv;
	}
	/**
	 * 删除部门
	 * @return
	 */
	@RequestMapping("department_delete")
	public ModelAndView delete(String ids,HttpSession session)
	{
		ModelAndView mv = new ModelAndView();
		Manager user = (Manager) session.getAttribute("loginUser");
//		List<Manager> list=managerDao.getByHQL("from Manager as m where m.department='"+ids+"'");
		String sql="SELECT * FROM lq_manager where departmentid in ("+ids+")";
		List<Map<String,Object>> entitys = firePatrolInfoDao.findForJdbc(sql);
		if(entitys.size()<1){
			this.departmentService.bulkDelete(ids, user);
			mv.setViewName("redirect:/department_list?method=deleteSuccess");
		}else {
			mv.setViewName("redirect:/department_listNew?method=deleteSuccess");
		}
		return mv;
	}

}
