package com.mobile.service;


import com.mobile.dao.FreshMenDao;
import com.mobile.model.FreshMen;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

@Component("freshMenService")
public class FreshMenService {
	@Resource(name = "freshMenDaoImpl")
	private FreshMenDao freshMenDao;
	
	public FreshMen get(Serializable examcode){
		if(freshMenDao.existsByProperty("examcode", examcode)){
			return freshMenDao.get(examcode);
		}
		else{
			return null;
		}
	}

	public FreshMenDao getFreshMenDao() {
		return freshMenDao;
	}

	public void setFreshMenDao(FreshMenDao freshMenDao) {
		this.freshMenDao = freshMenDao;
	}

}
