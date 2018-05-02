package com.parkbobo.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.PatrolRegionDao;
import com.parkbobo.model.PatrolRegion;

@Component("patrolRegionDaoImpl")
public class PatrolRegionDaoImpl extends BaseDaoSupport<PatrolRegion> implements PatrolRegionDao{


    @Override
    public int updatePatrolRegionDao(String sql) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getHibernateTemplate().getSessionFactory().openSession().connection();
            statement = connection.createStatement();
            int resuult = statement.executeUpdate(sql);
            return resuult;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(connection != null) {
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
        return 0;
    }
}
