package com.parkbobo.dao;

import java.util.List;

import com.parkbobo.model.UsersCars;

public interface UsersCarsDao extends BaseDao<UsersCars>{
	public abstract List<Object[]> getBySql(String sql);

}
