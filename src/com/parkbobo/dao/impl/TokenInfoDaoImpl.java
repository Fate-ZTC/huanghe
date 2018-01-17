package com.parkbobo.dao.impl;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.TokenInfoDao;
import com.parkbobo.model.TokenInfo;

@Component("tokenInfoDaoImpl")
public class TokenInfoDaoImpl extends BaseDaoSupport<TokenInfo>
		implements TokenInfoDao{

}
