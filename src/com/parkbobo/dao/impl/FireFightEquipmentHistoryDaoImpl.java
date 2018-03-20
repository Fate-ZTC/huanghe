package com.parkbobo.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;

import com.parkbobo.dao.FireFightEquipmentHistoryDao;
import com.parkbobo.model.FireFightEquipmentHistory;

@Component("fireFightEquipmentHistoryDaoImpl")
public class FireFightEquipmentHistoryDaoImpl extends BaseDaoSupport<FireFightEquipmentHistory> implements FireFightEquipmentHistoryDao{
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getBySql(final String sql) {
		List<Object[]> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public List<Object[]> doInHibernate(Session session) throws HibernateException,
					SQLException {
				return session.createSQLQuery(sql).list();
			}
		});
		return list;
	}
}
