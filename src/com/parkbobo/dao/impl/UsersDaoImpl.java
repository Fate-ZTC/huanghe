package com.parkbobo.dao.impl;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.UsersDao;
import com.parkbobo.model.Users;

@Component("usersDaoImpl")
public class UsersDaoImpl extends BaseDaoSupport<Users>
		implements UsersDao{

}
