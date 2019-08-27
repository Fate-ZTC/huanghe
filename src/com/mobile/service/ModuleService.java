package com.mobile.service;

import com.system.utils.PageBean;


import com.mobile.dao.ModuleDao;
import com.mobile.model.Module;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Component("moduleService")
public class ModuleService {
	@Resource(name = "moduleDaoImpl")
	private ModuleDao moduleDao;
	
	public List<Module> getByHql(String hql){
		return moduleDao.getByHQL(hql);
	}
	
	public PageBean<Module> pageQuery(String hql, int pageSize, int page){
		return moduleDao.pageQuery(hql, pageSize, page);
	}
	
	public Module add(Module module){
		return moduleDao.addEntity(module);
	}
	
	public void update(Module module){
		moduleDao.update(module);
	}
	
	public List<Module> loadParentList(){
		String hql = "from Module where parentModule IS NULL order by moduleid";
		return moduleDao.getByHQL(hql);
	}
	
	/**
	 * 分级获取所有模块
	 * @return
	 */
	public List<Module> loadAllLvels(){
		String hql = "from Module where parentModule IS NULL order by moduleid";
		List<Module> list = moduleDao.getByHQL(hql);
		for(Module m : list){
			hql = "from Module where parentModule.moduleid = " + m.getModuleid() + " order by moduleid";
			m.setModuleList(moduleDao.getByHQL(hql));
		}
		return list;
	}
	
	public static void main(String[] args) {
		String str = "123456789";
		System.out.println(str.indexOf("5"));
	}
	
	public Module loadById(Serializable moduleid){
		return moduleDao.get(moduleid);
	}
	
	public void delete(Serializable moduleid){
		moduleDao.delete(moduleid);
	}

	public ModuleDao getModuleDao() {
		return moduleDao;
	}

	public void setModuleDao(ModuleDao moduleDao) {
		this.moduleDao = moduleDao;
	}

}
