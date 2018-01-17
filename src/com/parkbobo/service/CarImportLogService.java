package com.parkbobo.service;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.CarExitLogDao;
import com.parkbobo.dao.CarImportLogDao;
import com.parkbobo.model.CarExitLog;
import com.parkbobo.model.CarImportLog;

@Component("carImportLogService")
public class CarImportLogService {
	@Resource(name="carImportLogDaoImpl")
	private CarImportLogDao carImportLogDao;
	@Resource(name="carExitLogDaoImpl")
	private CarExitLogDao carExitLogDao;

	/**
	 * HQL语句查询
	 * @param hql
	 * @return
	 */
	public List<CarImportLog> getByHql(String hql){
		return this.carImportLogDao.getByHQL(hql);
	}
	
	public CarImportLog get(String entityid){
		return carImportLogDao.get(entityid);
	}
	
	public List<CarImportLog> getAll() {
		return carImportLogDao.getAll();
	}
	
	public void add(CarImportLog carImportLog){
		this.carImportLogDao.add(carImportLog);
	}
	
	public void merge(CarImportLog carImportLog){
		this.carImportLogDao.merge(carImportLog);
	}
	/**
	 * 根据车牌号获取滞留在停车场的车辆
	 * @return 
	 * 
	 * */
	public CarImportLog retentionCar(String carNumber){
		String hql = "from CarImportLog where isImport=1 and plateNo='"+carNumber+"' order by importLogid desc limit 1 offset 0";
		List<CarImportLog> list = this.carImportLogDao.getByHQL(hql);
		if(list!=null && list.size()>0){
			//判断他没有出场
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String enterTime = sdf.format(list.get(0).getEnterTime());
			hql = "from CarExitLog where isImport=0 and plateNo='"+carNumber+"' and leaveTime>'"+enterTime+"'";
			List<CarExitLog> list2 = carExitLogDao.getByHQL(hql);
			if(list2==null || list2.size()==0){
				return list.get(0);
			}
		}
		return null;
	}
	
}