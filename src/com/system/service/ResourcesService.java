package com.system.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.utils.PageBean;
import com.system.dao.ManagerResourcesDao;
import com.system.dao.MenuDao;
import com.system.dao.ResourcesDao;
import com.system.dao.RoleDao;
import com.system.dao.RoleResourcesDao;
import com.system.model.Manager;
import com.system.model.ManagerResources;
import com.system.model.Menu;
import com.system.model.Resources;
import com.system.model.Role;
import com.system.model.RoleResources;
import com.system.utils.StringUtil;
@Component("resourcesService")
public class ResourcesService {
	@Resource(name="resourcesDaoImpl")
	private ResourcesDao resourcesDao;
	@Resource(name="menuDaoImpl")
	private MenuDao menuDao;
	@Resource(name="roleResourcesDaoImpl")
	private RoleResourcesDao roleResourcesDao;
	@Resource(name="managerResourcesDaoImpl")
	private ManagerResourcesDao managerResourcesDao;
	@Resource(name="roleDaoImpl")
	private RoleDao roleDao;
	@Resource
	private OptLogsService optLogsService;
	/**
	 * 通过单个字段值查询
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public List<Resources> getByProperty(String propertyName,Object value){
		return resourcesDao.getByProperty(propertyName, value);
	}
	/**
	 * 通过HQL语句查询
	 * @param hql
	 * @return
	 */
	public List<Resources> getByHql(String hql){
		return resourcesDao.getByHQL(hql);
	}
	/**
	 * 通过主键ID查询
	 * @param id
	 * @return
	 */
	public Resources getById(Integer id){
		return resourcesDao.get(id);
	}
	/**
	 * 添加资源权限
	 * @param resources
	 * @param users
	 */
	public void add(Resources resources,Manager manager){
		resourcesDao.merge(resources);
		optLogsService.addLogo("资源权限管理", manager, "添加资源权限：" +resources.getName());
	}
	/**
	 * 修改资源权限
	 * @param resources
	 * @param users
	 */
	public void update(Resources resources,Manager manager){
		resourcesDao.merge(resources);
		optLogsService.addLogo("资源权限管理", manager, "修改资源权限：" +resources.getName());
	}
	/**
	 * 分页查询资源权限
	 * @param hql
	 * @param pageSize
	 * @param page
	 * @return
	 */
	public PageBean<Resources> loadPage(String hql, int pageSize, int page){
		return resourcesDao.pageQuery(hql, pageSize, page);
	}
	/**
	 * 删除资源权限，ID用","分割
	 * @param ids
	 * @param users
	 */
	public void bulkDelete(String ids,Manager manager){
		if(ids.length() > 0){
			String[] strs = ids.split(",");
			Integer[] idArr = new Integer[strs.length];
			for (int i=0; i< strs.length; i++) {
				idArr[i] = Integer.valueOf(strs[i]);
			}
			resourcesDao.bulkDelete(idArr);
			optLogsService.addLogo("资源权限管理", manager, "删除资源权限,权限ID：" +ids);
		}
	}
	/**
	 * 获取资源权限树
	 * @return
	 */
	public String loadTree(String type) {
		StringBuffer json = new StringBuffer();
		List<Menu> menuList = menuDao.getByHQL("from Menu as m where (m.menutype = '" + type + "' or m.menutype = '0') and m.enable = 1 order by m.orderid");
		json.append("{\"id\":\"m_0\",\"pId\":\"0\",\"name\":\"系统权限\",\"drag\":false, \"drop\":false,\"open\":true},");
		for (Menu menu : menuList) {
			if(menu.getMenu() == null){
				json.append("{\"id\":\"m_" + menu.getMenuId() + "\",\"pId\":\"m_0\",\"name\":\"" + menu.getName() + "\",\"drag\":false, \"drop\":false,\"open\":true},");
			}else{
				json.append("{\"id\":\"m_" + menu.getMenuId() + "\",\"pId\":\"m_" + menu.getMenu().getMenuId() + "\",\"name\":\"" + menu.getName() + "\",\"drag\":false, \"drop\":false,\"open\":true},");
				List<Resources> resourcesList = resourcesDao.getByHQL("from Resources as rs where rs.menu.menuId = " 
						+ menu.getMenuId() + " and rs.enable = 1 and (rs.resourcetype = '0' or rs.resourcetype = '" + type + "') order by rs.orderid");
				for (Resources resources : resourcesList) {
					json.append("{\"id\":\"" + resources.getResourcesId() + "\",\"pId\":\"m_" + menu.getMenuId() + "\",\"name\":\"" + resources.getName() + "\",\"drag\":false, \"drop\":false,\"open\":true},");
				}
			}
		}
		return StringUtil.deleteLastStr(json.toString());
	}
	/**
	 * 通过角色ID获取资源权限树
	 * @param id
	 * @return
	 */
	public String loadRoleTree(Integer id) {
		Map<Integer,Integer> idMap = new HashMap<Integer, Integer>();
		Role role = roleDao.get(id);
		List<RoleResources> roleResourcesList = roleResourcesDao.getByHQL("from RoleResources as r where r.id.roleId = " + id);
		for (RoleResources roleResources : roleResourcesList) {
			idMap.put(roleResources.getResources().getResourcesId(), roleResources.getResources().getResourcesId());
		}
		StringBuffer json = new StringBuffer();
		List<Menu> menuList = menuDao.getByHQL("from Menu as m where (m.menutype = '" + role.getRoleType() + "' or m.menutype = '0') and m.enable = 1 order by m.orderid");
		json.append("{id:\"m_0\",pId:\"0\",name:\"系统权限\",drag:false, drop:false,open:true},");
		for (Menu menu : menuList) {
			if(menu.getMenu() == null){
				json.append("{id:\"m_" + menu.getMenuId() + "\",pId:\"m_0\",name:\"" + menu.getName() + "\",drag:false, drop:false,open:true},");
			}else{
				json.append("{id:\"m_" + menu.getMenuId() + "\",pId:\"m_" + menu.getMenu().getMenuId() + "\",name:\"" + menu.getName() + "\",drag:false, drop:false,open:true},");
				List<Resources> resourcesList = resourcesDao.getByHQL("from Resources as rs where rs.menu.menuId = " 
						+ menu.getMenuId() + " and rs.enable = 1 and (rs.resourcetype = '" + role.getRoleType() + "' or rs.resourcetype = '0') order by rs.orderid");
				for (Resources resources : resourcesList) {
					if(idMap.containsKey(resources.getResourcesId())){
						json.append("{id:\"" + resources.getResourcesId() + "\",pId:\"m_" + menu.getMenuId() + "\",name:\"" + resources.getName() + "\",drag:false, drop:false,checked:true,open:true},");
					}else{
						json.append("{id:\"" + resources.getResourcesId() + "\",pId:\"m_" + menu.getMenuId() + "\",name:\"" + resources.getName() + "\",drag:false, drop:false,open:true},");
					}
				}
			}
		}
		return StringUtil.deleteLastStr(json.toString());
	}
	/**
	 * 通过用户ID获取资源权限树
	 * @param id
	 * @return
	 */
	public String loadManagerTree(Serializable id, String type) {
		Map<Integer, Integer> idMap = new HashMap<Integer, Integer>();
		List<ManagerResources> managerResourcesList = managerResourcesDao.getByHQL("from ManagerResources as ur where ur.id.userId = " + id);
		for (ManagerResources managerResources : managerResourcesList) {
			idMap.put(managerResources.getResources().getResourcesId(), managerResources.getResources().getResourcesId());
		}
		StringBuffer json = new StringBuffer();
		List<Menu> menuList = menuDao.getByHQL("from Menu as m where (m.menutype = '"+type+"' or m.menutype = '0') and m.enable = 1 order by m.orderid");
		json.append("{id:\"m_0\",pId:\"0\",name:\"系统权限\",drag:false, drop:false,open:true},");
		for (Menu menu : menuList) {
			if(menu.getMenu() == null){
				json.append("{id:\"m_" + menu.getMenuId() + "\",pId:\"m_0\",name:\"" + menu.getName() + "\",drag:false, drop:false,open:true},");
			}else{
				json.append("{id:\"m_" + menu.getMenuId() + "\",pId:\"m_" + menu.getMenu().getMenuId() + "\",name:\"" + menu.getName() + "\",drag:false, drop:false,open:true},");
				List<Resources> resourcesList = resourcesDao.getByHQL("from Resources as rs where rs.menu.menuId = " 
						+ menu.getMenuId() + " and rs.enable = 1 and (rs.resourcetype = '0' or rs.resourcetype = '" + type + "') order by rs.orderid");
				for (Resources resources : resourcesList) {
					if(idMap.containsKey(resources.getResourcesId())){
						json.append("{id:\"" + resources.getResourcesId() + "\",pId:\"m_" + menu.getMenuId() + "\",name:\"" + resources.getName() + "\",drag:false, drop:false,checked:true,open:true},");
					}else{
						json.append("{id:\"" + resources.getResourcesId() + "\",pId:\"m_" + menu.getMenuId() + "\",name:\"" + resources.getName() + "\",drag:false, drop:false,open:true},");
					}
				}
			}
		}
		return StringUtil.deleteLastStr(json.toString());
	}
}
