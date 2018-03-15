package com.system.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.utils.PageBean;
import com.system.dao.DepartmentDao;
import com.system.model.Department;
import com.system.model.Manager;

@Component("departmentService")
public class DepartmentService {
	@Resource(name="departmentDaoImpl")
	private DepartmentDao departmentDao;
	@Resource
	private OptLogsService optLogsService;
	
	public List<Department> getByHql(String hql){
		return departmentDao.getByHQL(hql);
	}
	public Department getById(Serializable id){
		return departmentDao.get(id);
	}
	public void add(Department dept,Manager manager){
		departmentDao.merge(dept);
		optLogsService.addLogo("部门管理", manager, "添加部门,部门名称：" +dept.getName());
	}
	public void update(Department dept,Manager manager){
		departmentDao.merge(dept);
		optLogsService.addLogo("部门管理", manager, "修改部门,部门ID：" +dept.getName());
	}
	public PageBean<Department> loadPage(String hql , int pageSize, int page){
		return departmentDao.pageQuery(hql, pageSize, page);
	}
	public void bulkDelete(String ids,Manager manager){
		if(ids.length() > 0){
			String[] strs = ids.split(",");
			Integer[] idArr = new Integer[strs.length];
			for (int i=0; i< strs.length; i++) {
				idArr[i] = Integer.parseInt(strs[i]);
			}
			departmentDao.bulkDelete(idArr);
			optLogsService.addLogo("部门管理", manager, "删除部门,部门ID：" +ids);
		}
	}

}
