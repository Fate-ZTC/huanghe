package com.mobile.dao.impl;


import com.mobile.dao.UsersDao;
import com.mobile.model.Users;
import org.springframework.stereotype.Component;

@Component("usersDaoImpl")
public class UsersDaoImpl extends GisBaseDAOSupport<Users>
		implements UsersDao {
	

}
