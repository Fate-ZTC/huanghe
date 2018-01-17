package com.parkbobo.dao.impl;
import org.springframework.stereotype.Component;

import com.parkbobo.dao.MobileCheckcodeDao;
import com.parkbobo.model.MobileCheckcode;
@Component("mobileCheckcodeDaoImpl")
public class MobileCheckcodeDaoImpl extends BaseDaoSupport<MobileCheckcode> implements MobileCheckcodeDao {
}