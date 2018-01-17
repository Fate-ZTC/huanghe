package com.parkbobo.service;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.CarExitLogDao;
import com.parkbobo.dao.CarparkBerthDao;
import com.parkbobo.model.CarExitLog;
import com.parkbobo.model.CarparkBerth;

@Component("carparkBerthService")
public class CarparkBerthService {
	@Resource(name="carparkBerthDaoImpl")
	private CarparkBerthDao carparkBerthDao;

	/**
	 * HQL语句查询
	 * @param hql
	 * @return
	 */
	public List<CarparkBerth> getByHql(String hql){
		return this.carparkBerthDao.getByHQL(hql);
	}
	
	
	public CarparkBerth get(String entityid){
		return carparkBerthDao.get(entityid);
	}
	
	public List<CarparkBerth> getAll() {
		return carparkBerthDao.getAll();
	}
	
	public void merge(CarparkBerth carparkBerth){
		this.carparkBerthDao.merge(carparkBerth);
	}
	
	public void add(CarparkBerth carparkBerth){
		this.carparkBerthDao.add(carparkBerth);
	}
	
	/**
	 * 根据车牌号获取最新的车位信息
	 * @return 
	 * */
	public CarparkBerth newBerth(String carNumber,String enterTime){
		String hql = "from CarparkBerth where carPlate='"+carNumber+"' and passTime>='"+enterTime+"' order by passTime desc";
		List<CarparkBerth> list = this.carparkBerthDao.getByHQL(hql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public void deleteBerths(Long carparkid,String berthName,Integer direct){
		String hql = "from CarparkBerth where carparkid="+carparkid+" and spotId='"+berthName+"' and direct="+direct;
		List<CarparkBerth> list = this.carparkBerthDao.getByHQL(hql);
		if(list!=null && list.size()>0){
			for (CarparkBerth carparkBerth : list) {
				this.carparkBerthDao.delete(carparkBerth.getBerthId());
			}
		}
	}
	
}