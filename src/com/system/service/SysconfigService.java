package com.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.system.dao.SysconfigDao;
import com.system.model.Sysconfig;
@Component("sysconfigService")
public class SysconfigService {
	@Resource(name="sysconfigDaoImpl")
	private SysconfigDao sysconfigDao;
	
	public Map<String, String> getConfig() {
		Map<String, String> map = new HashMap<String, String>();
		List<Sysconfig> sysconfigs = this.sysconfigDao.getAll();
		for (Sysconfig sysconfig : sysconfigs) {
			map.put(sysconfig.getVarname(), sysconfig.getVarinfo());
		}
		return map;
	}
	public String getValue(String key) {
		Sysconfig sysconfig = this.sysconfigDao.getUniqueByProperty("varname", key);
		if(sysconfig != null) {
			return sysconfig.getVarvalue();
		}
		return null;
	}
	public List<Sysconfig> getByHql(String hql) {
		return this.sysconfigDao.getByHQL(hql);
	}
	public void updateValue(String name, String value) {
		Sysconfig sysconfig = this.sysconfigDao.getUniqueByProperty("varname", name);
		if(sysconfig != null){
			sysconfig.setVarvalue(value);
			this.sysconfigDao.update(sysconfig);
		}
	}
	
	public List<Sysconfig> getVarnameToSys(String key){
		List<Sysconfig> sysconfigs = this.sysconfigDao.getByProperty("varname", key);
		return sysconfigs;
	}
}
