package com.system.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

import com.parkbobo.utils.PageBean;
import com.system.dao.ManagerDao;
import com.system.model.Manager;


@Component("managerService")
public class ManagerService {
	@Resource(name = "managerDaoImpl")
	private ManagerDao managerDao;
	@Resource
	private OptLogsService optLogsService;
	public Manager updateUserInfo(Manager manager) {
		Manager u = managerDao.get(manager.getUserId());
		u.setRealname(manager.getRealname());
		u.setMobile(manager.getMobile());
		u.setQq(manager.getQq());
		u.setMemo("Y");
		managerDao.merge(u);
		optLogsService.addLogo("个人资料", u, "更新个人资料");
		return u;
		
	}
	public Manager getById(Integer userId) {
		return managerDao.get(userId);
	}
	public Manager getUniqueByProperty(String propertyName, String value) {
		return managerDao.getUniqueByProperty(propertyName, value);
	}
	public void localUpdateOneFields(Integer userId, String[] propertyNames,
			Object[] values) {
		managerDao.localUpdateOneFields(userId, propertyNames, values);
	}
	public void updatePassword(Manager manager, String password) {
		Manager u = managerDao.get(manager.getUserId());
		ShaPasswordEncoder sp = new ShaPasswordEncoder();
		u.setPassword(sp.encodePassword(password, u.getUsername()));
		managerDao.merge(u);
		optLogsService.addLogo("修改密码", u, "更新个人密码");
	}
	public boolean checkProperty(String propertyName,Object value,Integer id) {
		if(id != null){
			List<Manager> userList = managerDao.getByHQL("from Manager as u where u." + propertyName + " = '" + value + "' and u.userId != " + id);
			if(userList.size() > 0){
				return true;
			}else{
				return false;
			}
		}else{
			return managerDao.existsByProperty(propertyName, value);
		}
		
	}
	public PageBean<Manager> loadPage(String hql, int pageSize, int page) {
		return managerDao.pageQuery(hql, pageSize, page);
	}
	public void add(Manager manager, String enablRegionIds, Manager loginManager) {
		ShaPasswordEncoder sp = new ShaPasswordEncoder();
		manager.setPassword(sp.encodePassword(manager.getPassword(), manager.getUsername()));
		manager.setLoginCount(0);
 		manager.setActivation(1); 
		manager.setIsAuth(1);
		manager.setStatus(0);
		manager = managerDao.add(manager);
		optLogsService.addLogo("用户管理", loginManager, "添加用户，用户名：" + manager.getUsername());
	}
	public void update(Manager manager, String enablRegionIds, Manager loginManager) {
		ShaPasswordEncoder sp = new ShaPasswordEncoder();
		Manager u = managerDao.get(manager.getUserId());
		u.setPassword(sp.encodePassword(manager.getPassword(), u.getUsername()));
		u.setQq(manager.getQq());
		u.setDepartment(manager.getDepartment());
		u.setRole(manager.getRole());
		u.setRealname(manager.getRealname());
		u.setMobile(manager.getMobile());
		managerDao.update(u);
		optLogsService.addLogo("用户管理", loginManager, "修改用户，用户名：" + u.getUsername());
	}
	public void bulkDelete(String ids, Manager loginManager) {
		if(ids.length() > 0){
			String[] strs = ids.split(",");
			for (int i=0; i< strs.length; i++) {
				Manager u = managerDao.get(Integer.parseInt(strs[i]));
				managerDao.delete(u.getUserId());
			}
			optLogsService.addLogo("用户管理", loginManager, "删除系统用户,用户ID：" +ids);
		}
	}
	public void resetPassword(Integer id, Manager loginManager) {
		Manager u = managerDao.get(id);
		ShaPasswordEncoder sp = new ShaPasswordEncoder();
		u.setPassword(sp.encodePassword("123456", u.getUsername()));
		managerDao.merge(u);
		optLogsService.addLogo("用户管理", loginManager, "重置用户密码，用户ID：" +id );
	}
	public void lock(Integer id, Manager loginManager) {
		managerDao.localUpdateOneField(id, "status", 1);
		optLogsService.addLogo("用户管理", loginManager, "锁定用户,用户ID：" + id);
	}
	public void unlock(Integer id, Manager loginManager) {
		managerDao.localUpdateOneField(id, "status", 0);
		optLogsService.addLogo("用户管理", loginManager, "解锁用户,用户ID：" + id);
	}
}
