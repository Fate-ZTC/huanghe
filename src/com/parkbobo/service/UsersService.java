package com.parkbobo.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.UsersDao;
import com.parkbobo.model.Users;

@Component("usersService")
public class UsersService {
	@Resource(name = "usersDaoImpl")
	private UsersDao usersDao;
	
	public List<Users> getByHql(String hql){
		return usersDao.getByHQL(hql);
	}
	
	public Users loadByPhone(String mobile){
		if(usersDao.existsByProperty("mobile", mobile)){
			return usersDao.get(mobile);
		}
		else{
			return null;
		}
	}
	
	public Users loadByOpenid(String openid){
		if(usersDao.existsByProperty("openid", openid)){
			return usersDao.getUniqueByProperty("openid", openid);
		}
		else{
			return null;
		}
	}
	
	public Users get(Serializable mobile){
		return usersDao.get(mobile);
	}
	
	public Users add(Users user){
		return usersDao.add(user);
	}
	
	public void update(Users user){
		usersDao.update(user);
	}
	
	public void save(Users users){
		usersDao.merge(users);
	}

	public UsersDao getUsersDao() {
		return usersDao;
	}

	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}
}
