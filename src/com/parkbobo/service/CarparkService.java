package com.parkbobo.service;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.CarparkDao;
import com.parkbobo.model.Carpark;

@Component("carparkService")
public class CarparkService {
	@Resource(name="carparkDaoImpl")
	private CarparkDao carparkDao;

	/**
	 * HQL语句查询
	 * @param hql
	 * @return
	 */
	public List<Carpark> getByHql(String hql){
		return this.carparkDao.getByHQL(hql);
	}
	
	/**
	 * 根据ID获取对象
	 * */
	public Carpark get(Long entityid){
		return carparkDao.get(entityid);
	}
	
	public List<Carpark> getAll() {
		return carparkDao.getAll();
	}
	/**
	 * 添加
	 * */
	public void add(Carpark carpark) {
		carparkDao.add(carpark);
	}
	/**
	 * 更新
	 * */
	public void merge(Carpark carpark) {
		carparkDao.merge(carpark);
	}

	public Carpark nameToPark(Object parkingName) {
		return this.carparkDao.getUniqueByProperty("name", parkingName);
	}
	
	public Carpark thIdToPark(Object parkId) {
		return this.carparkDao.getUniqueByProperty("thirdId", parkId);
	}

}