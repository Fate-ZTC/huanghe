package com.parkbobo.dao.impl;

import com.parkbobo.dao.PatrolSignPointInfoDao;
import com.parkbobo.model.PatrolSignPointInfo;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component("patrolSignPointInfoDaoImpl")
public class PatrolSignPointInfoDaoImpl extends BaseDaoSupport<PatrolSignPointInfo>
        implements PatrolSignPointInfoDao{

    @Override
    public Boolean isInRegion(Integer regionId, Double lng, Double lat) {
        Session session = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select st_contains((select region_location from patrol_region where id = " + regionId + "), st_point(" + lng + "," + lat + "))";
            session = getHibernateTemplate().getSessionFactory().getCurrentSession();
            connection = session.connection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                Boolean isIn = resultSet.getBoolean(1);
                return isIn;
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
        return false;
    }

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
