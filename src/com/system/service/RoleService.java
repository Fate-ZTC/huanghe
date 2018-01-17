package com.system.service;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.utils.PageBean;
import com.system.dao.RoleDao;
import com.system.dao.RoleResourcesDao;
import com.system.model.Manager;
import com.system.model.Role;
import com.system.model.RoleResources;
import com.system.model.RoleResourcesId;

@Component("roleService")
public class RoleService {
	@Resource(name="roleDaoImpl")
	private RoleDao roleDao;
	@Resource(name="roleResourcesDaoImpl")
	private RoleResourcesDao roleResourcesDao;
	@Resource
	private OptLogsService optLogsService;
	
	public List<Role> getByHql(String hql){
		return roleDao.getByHQL(hql);
	}
	
	public Role getById(Integer id){
		return roleDao.get(id);
	}
	public void add(String resourcesIds, Role role,Manager manager){
		role = roleDao.add(role);
		if(resourcesIds.length() > 0){
			String[] strs = resourcesIds.split(",");
			for (String str : strs) {
				RoleResources roleResources = new RoleResources();
				RoleResourcesId id = new RoleResourcesId();
				id.setResourcesId(Integer.valueOf(str));
				id.setRoleId(role.getRoleId());
				roleResources.setId(id);
				roleResources.setType(1);
				roleResourcesDao.merge(roleResources);
			}
		}
		optLogsService.addLogo("角色管理", manager, "添加角色:"+role.getName());
	}
	public void update(String resourcesIds, Role role,Manager manager){
		roleDao.update(role);
		List<RoleResources> roleResourcesList =this.roleResourcesDao.getByHQL("from RoleResources as r where r.id.roleId = " + role.getRoleId());
		for (RoleResources rr : roleResourcesList) {
			this.roleResourcesDao.delete(rr.getId());
		}
		if(resourcesIds.length() > 0){
			String[] strs = resourcesIds.split(",");
			for (String str : strs) {
				RoleResources roleResources = new RoleResources();
				RoleResourcesId id = new RoleResourcesId();
				id.setResourcesId(Integer.valueOf(str));
				id.setRoleId(role.getRoleId());
				roleResources.setId(id);
				roleResources.setType(1);
				roleResourcesDao.merge(roleResources);
				
			}
		}
		optLogsService.addLogo("角色管理", manager, "修改角色:"+role.getName());
	}
	public PageBean<Role> loadPage(String hql, int pageSize, int page){
		return roleDao.pageQuery(hql, pageSize, page);
	}
	public void bulkDelete(String ids,Manager manager){
		if(ids.length() > 0){
			String[] strs = ids.split(",");
			for (int i=0; i< strs.length; i++) {
				roleDao.delete(Integer.valueOf(strs[i]));
			}
			optLogsService.addLogo("角色管理", manager, "删除角色,角色ID：" +ids);
		}
	}
}
