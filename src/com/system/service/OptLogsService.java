package com.system.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.parkbobo.utils.PageBean;
import com.system.dao.OptLogsDao;
import com.system.model.Manager;
import com.system.model.OptLogs;

@Component("optLogsService")
public class OptLogsService {
	@Resource(name="optLogsDaoImpl")
	private OptLogsDao optLogsDao;
	/**
	 * 添加日志
	 * @param fromModel
	 * @param manager
	 * @param description
	 */
	public void addLogo(String fromModel,Manager manager, String description)
	{
		OptLogs optLogs = new OptLogs();
		if(manager != null)
		{
			optLogs.setUserId(manager.getUserId());
			optLogs.setUsername(manager.getUsername());
		}
		optLogs.setFromModel(fromModel);
		optLogs.setDescription(description);
		optLogs.setCreateTime(new Date());
		this.optLogsDao.merge(optLogs);
	}
	public List<OptLogs> getByHql(String hql){
		return optLogsDao.getByHQL(hql);
	}
	public OptLogs getById(Long id){
		return optLogsDao.get(id);
	}
	public void merge(OptLogs optLogs){
		optLogsDao.merge(optLogs);
	}
	public void delete(Long id){
		optLogsDao.delete(id);
	}
	
	public PageBean<OptLogs> loadPage(String hql, int pageSize, int page){
		return optLogsDao.pageQuery(hql, pageSize, page);
	}
	
	public void bulkDelete(String ids){
		if(ids.length()>0){
			String[] strs = ids.split(",");
			Integer[] idArr = new Integer[strs.length];
			for (int i=0; i< strs.length; i++) {
				idArr[i] =  Integer.valueOf(strs[i]);
			}
			optLogsDao.bulkDelete(idArr);
		}
	}
	public void deleteByHql(String hql){
		this.optLogsDao.deleteByHql(hql);
	}
}
