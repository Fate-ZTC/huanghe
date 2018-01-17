package com.parkbobo.service;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.WeixinConfigDao;
import com.parkbobo.model.Carpark;
import com.parkbobo.model.WeixinConfig;

@Component("weixinConfigService")
public class WeixinConfigService {
	@Resource(name="weixinConfigDaoImpl")
	private WeixinConfigDao weixinConfigDao;

	/**
	 * HQL语句查询
	 * @param hql
	 * @return
	 */
	public List<WeixinConfig> getByHql(String hql){
		return this.weixinConfigDao.getByHQL(hql);
	}
	
	/**
	 * 根据ID获取对象
	 * */
	public WeixinConfig get(String entityid){
		return weixinConfigDao.get(entityid);
	}
	
	public List<WeixinConfig> getAll() {
		return weixinConfigDao.getAll();
	}
	/**
	 * 添加
	 * */
	public void add(WeixinConfig config) {
		weixinConfigDao.add(config);
	}
	/**
	 * 更新
	 * */
	public void merge(WeixinConfig config) {
		weixinConfigDao.merge(config);
	}
	
	public WeixinConfig nameToConfig(String wxName){
		return weixinConfigDao.getUniqueByProperty("wxName", wxName);
	}
	
}