package com.system.dao.impl;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.impl.BaseDaoSupport;
import com.system.dao.MenuDao;
import com.system.model.Menu;

@Component("menuDaoImpl")
public class MenuDaoImpl extends BaseDaoSupport<Menu>
		implements MenuDao{

}
