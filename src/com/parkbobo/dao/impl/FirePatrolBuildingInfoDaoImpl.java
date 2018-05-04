package com.parkbobo.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.parkbobo.dao.FirePatrolBuildingInfoDao;
import com.parkbobo.model.FirePatrolBuildingInfo;

/**
 * @author lijunhong
 * @since 18/5/3 下午9:06
 */
@Repository("firePatrolBuildingInfoDao")
public class FirePatrolBuildingInfoDaoImpl extends BaseDaoSupport<FirePatrolBuildingInfo> implements FirePatrolBuildingInfoDao{

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
