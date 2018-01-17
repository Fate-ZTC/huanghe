package com.parkbobo.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.VehicleCostWechatDao;
import com.parkbobo.model.VehicleCostWechat;

@Component("vehicleCostWechatService")
public class VehicleCostWechatService {
	@Resource(name="vehicleCostWechatDaoImpl")
	private VehicleCostWechatDao vehicleCostWechatDao;
	
	public VehicleCostWechat add(VehicleCostWechat vcw){
		return vehicleCostWechatDao.add(vcw);
	}
	
	public void update(VehicleCostWechat vcw){
		vehicleCostWechatDao.update(vcw);
	}
	
	public VehicleCostWechat get(Serializable kid){
		if(vehicleCostWechatDao.existsByProperty("kid", kid))
			return vehicleCostWechatDao.get(kid);
		return null;
	}
	
	public List<VehicleCostWechat> getByHql(String hql){
		return vehicleCostWechatDao.getByHQL(hql);
	}
	
	public void delete(Serializable kid){
		vehicleCostWechatDao.delete(kid);
	}
	
	/**
	 * 根据入场ID获取最近一次缴费记录
	 * @param inunid
	 * @return
	 */
	public VehicleCostWechat loadByInunid(String inunid){
		String hql = "from VehicleCostWechat where inUnid = '" + inunid + "' and status = 1 order by planPayTime desc limit 1";
		List<VehicleCostWechat> list = vehicleCostWechatDao.getByHQL(hql);
		if(list.size() > 0){
			return list.get(0);
		}
		else{
			return null;
		}
	}
	
	public void deleteUnpayed(String inunid){
		String hql = "delete from VehicleCostWechat where status = 0 and inUnid = '" + inunid + "' ";
		vehicleCostWechatDao.deleteByHql(hql);
	}
	
	public VehicleCostWechat loadByPayUnid(String payUnid){
		if(vehicleCostWechatDao.existsByProperty("payUnid", payUnid))
			return vehicleCostWechatDao.getUniqueByProperty("payUnid", payUnid);
		else
			return null;
	}
	
	/**
	 * 根据入场ID获取交费列表
	 * @param inUnid
	 * @return
	 */
	public List<VehicleCostWechat> loadListWithInUnid(String inUnid){
		String hql = "from VehicleCostWechat where inUnid = '" + inUnid + "' ";
		return vehicleCostWechatDao.getByHQL(hql);
	}

	public VehicleCostWechatDao getVehicleCostWechatDao() {
		return vehicleCostWechatDao;
	}

	public void setVehicleCostWechatDao(VehicleCostWechatDao vehicleCostWechatDao) {
		this.vehicleCostWechatDao = vehicleCostWechatDao;
	}

}
