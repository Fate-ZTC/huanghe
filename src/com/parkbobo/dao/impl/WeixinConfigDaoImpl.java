package com.parkbobo.dao.impl;
import org.springframework.stereotype.Component;

import com.parkbobo.dao.WeixinConfigDao;
import com.parkbobo.model.WeixinConfig;
@Component("weixinConfigDaoImpl")
public class WeixinConfigDaoImpl extends BaseDaoSupport<WeixinConfig> implements WeixinConfigDao {
}