package com.parkbobo.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;

import com.parkbobo.dao.PatrolLocationInfoDao;
import com.parkbobo.model.PatrolLocationInfo;

@Component("patrolLocationInfoDaoImpl")
public class PatrolLocationInfoDaoImpl extends BaseDaoSupport<PatrolLocationInfo> implements PatrolLocationInfoDao{
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getBySql(final String sql) {
		List<Integer> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public List<Integer> doInHibernate(Session session) throws HibernateException,
					SQLException {
				return session.createSQLQuery(sql).list();
			}
		});
		return list;
	}
}
