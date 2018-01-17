package com.system.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.utils.PageBean;
import com.system.dao.MenuDao;
import com.system.model.Menu;

@Component("menuService")
public class MenuService {
	@Resource(name="menuDaoImpl")
	private MenuDao menuDao;

	public MenuDao getMenuDao() {
		return menuDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}
	
	public List<Menu> getByHql(String hql){
		return menuDao.getByHQL(hql);
	}
	public Menu getById(Integer menuId){
		return menuDao.get(menuId);
	}
	public void merge(Menu menu){
		menuDao.merge(menu);
	}
	public void update(Menu menu){
		menuDao.update(menu);
	}
	public void delete(Long id){
		menuDao.delete(id);
	}
	public PageBean<Menu> loadPage(String hql, int pageSize, int page){
		return menuDao.pageQuery(hql, pageSize, page);
	}
	public void bulkDelete(String ids){
		menuDao.bulkDelete(ids.split("|"));
	}
	
	public List<Menu> getAll(){
		List<Menu> menuList = menuDao.getByHQL("from Menu as m where m.menu.menuId is NULL and m.enable = 1 order by m.orderid");
		for (Menu menu : menuList) {
			List<Menu> childrenMenu = menuDao.getByHQL("from Menu as m where m.menu.menuId = " + menu.getMenuId() + " and m.enable = 1  order by m.orderid");
			menu.setChildrenMenu(childrenMenu);
		}
		return menuList;
	}
}
