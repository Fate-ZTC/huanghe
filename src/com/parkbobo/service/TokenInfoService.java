package com.parkbobo.service;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.TokenInfoDao;
import com.parkbobo.model.TokenInfo;

@Component("tokenInfoService")
public class TokenInfoService {
	@Resource(name="tokenInfoDaoImpl")
	private TokenInfoDao tokenInfoDao;
	
	private TokenInfo get(Serializable kid){
		return tokenInfoDao.get(kid);
	}
	
	private void save(TokenInfo token){
		tokenInfoDao.merge(token);
	}

	public TokenInfoDao getTokenInfoDao() {
		return tokenInfoDao;
	}

	public void setTokenInfoDao(TokenInfoDao tokenInfoDao) {
		this.tokenInfoDao = tokenInfoDao;
	}

}
