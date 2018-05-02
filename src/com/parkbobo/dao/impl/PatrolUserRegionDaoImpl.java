package com.parkbobo.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;

import com.parkbobo.dao.PatrolUserRegionDao;
import com.parkbobo.model.PatrolUserRegion;

@Component("patrolUserRegionDaoImpl")
public class PatrolUserRegionDaoImpl extends BaseDaoSupport<PatrolUserRegion> implements PatrolUserRegionDao{
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

	@Override
	public double getDistanceBySql(String sql) {
		Session session = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			session = getHibernateTemplate().getSessionFactory().getCurrentSession();
			connection = session.connection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				double distance = resultSet.getDouble(1);
				return distance;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(resultSet != null) {
				try {
					resultSet.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(statement != null) {
				try {
					statement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return -1;
	}
}
