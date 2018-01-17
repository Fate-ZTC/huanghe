package com.parkbobo.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;

import com.parkbobo.dao.UsersCarsDao;
import com.parkbobo.model.UsersCars;

@Component("usersCarsDaoImpl")
public class UsersCarsDaoImpl extends BaseDaoSupport<UsersCars>
		implements UsersCarsDao{
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getBySql(final String sql) {
		List<Object[]> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<Object[]> doInHibernate(Session session) throws HibernateException,
					SQLException {
				return session.createSQLQuery(sql).list();
			}
		});
		return list;
	}
}
