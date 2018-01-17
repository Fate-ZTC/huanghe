package com.parkbobo.dao.impl;
import org.springframework.stereotype.Component;

import com.parkbobo.dao.CarparkDao;
import com.parkbobo.model.Carpark;
@Component("carparkDaoImpl")
public class CarparkDaoImpl extends BaseDaoSupport<Carpark> implements CarparkDao {
}