package com.mobile.service;

import com.mobile.dao.InfoListDao;
import com.mobile.model.InfoList;
import com.system.utils.PageBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Component("infoListService")
public class InfoListService {
	@Resource(name = "infoListDaoImpl")
	private InfoListDao infoListDao;
	
	public PageBean<InfoList> pageQuery(String hql, int pageSize, int page){
		return this.infoListDao.pageQuery(hql, pageSize, page);
	}
	
	public List<InfoList> getByHql(String hql){
		return this.infoListDao.getByHQL(hql);
	}
	
	public InfoList loadById(Serializable infoid){
		return this.infoListDao.get(infoid);
	}
	
	public InfoList add(InfoList info){
		return this.infoListDao.addEntity(info);
	}
	
	public void update(InfoList info){
		this.infoListDao.update(info);
	}
	
	public void delete(Serializable infoid){
		this.infoListDao.delete(infoid);
	}

	public InfoListDao getInfoListDao() {
		return infoListDao;
	}

	public void setInfoListDao(InfoListDao infoListDao) {
		this.infoListDao = infoListDao;
	}

}
