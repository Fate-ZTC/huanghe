package com.mobile.service;


import com.mobile.dao.EnumerateDetailDao;
import com.mobile.model.EnumerateDetail;
import com.mobile.model.EnumerateDetailId;
import com.mobile.model.Users;
import com.system.utils.PageBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

@Component("enumerateDetailService")
public class EnumerateDetailService {
	@Resource(name = "enumerateDetailDaoImpl")
	private EnumerateDetailDao enumerateDetailDao;
	
	/**
	 * 获取学期第一天的日期
	 * @return
	 * @throws ParseException
	 */
	public String lodadFirstDay(){
		String hql = "from EnumerateDetail where id.id = 1 and id.ino = 1";
		EnumerateDetail ed = enumerateDetailDao.getByHQL(hql).get(0);
		return ed.getParavalue();
	}
	
	/**
	 * 获取签到迟到判断
	 * @return
	 * @throws ParseException
	 */
	public Integer lodadLateMinute() throws ParseException{
		String hql = "from EnumerateDetail where id.id = 1 and id.ino = 2";
		EnumerateDetail ed = enumerateDetailDao.getByHQL(hql).get(0);
		return Integer.parseInt(ed.getParavalue());
	}
	
	/**
	 * 获取签到迟到判断
	 * @return
	 * @throws ParseException
	 */
	public Double loadSignRadius() throws ParseException{
		String hql = "from EnumerateDetail where id.id = 1 and id.ino = 3";
		EnumerateDetail ed = enumerateDetailDao.getByHQL(hql).get(0);
		return Double.parseDouble(ed.getParavalue());
	}
	
	/**
	 * 获取APP是否需要更新模块列表
	 * @return
	 * @throws ParseException
	 */
	public String loadAppModuleStatus() throws ParseException{
		String hql = "from EnumerateDetail where id.id = 5 and id.ino = 1";
		EnumerateDetail ed = enumerateDetailDao.getByHQL(hql).get(0);
		return ed.getParavalue();
	}
	
	/**
	 * 更新APP是否需要更新模块列表参数的值
	 * @return
	 * @throws ParseException
	 */
	public void updateAppModuleStatus() throws ParseException{
		String hql = "from EnumerateDetail where id.id = 5 and id.ino = 1";
		EnumerateDetail ed = enumerateDetailDao.getByHQL(hql).get(0);
		Integer version = Integer.parseInt(ed.getParavalue()) + 1;
		ed.setParavalue(version + "");
		enumerateDetailDao.update(ed);
	}
	
	/**
	 * 获取附近信息查询的距离
	 * @return
	 * @throws ParseException
	 */
	public Integer lodadNearbyDistance() throws ParseException{
		String hql = "from EnumerateDetail where id.id = 4 and id.ino = 1";
		EnumerateDetail ed = enumerateDetailDao.getByHQL(hql).get(0);
		return Integer.parseInt(ed.getParavalue());
	}
	
	public List<EnumerateDetail> getByHql(String hql) {
		return enumerateDetailDao.getByHQL(hql);
	}

	public EnumerateDetail getById(EnumerateDetailId id) {
		return enumerateDetailDao.get(id);
	}

	public PageBean<EnumerateDetail> loadPage(String hql, int pageSize, int page) {
		return enumerateDetailDao.pageQuery(hql, pageSize, page);
	}

	public void bulkDelete(String ids, Users users) {
		if(ids.length() > 0){
			String[] strs = ids.split(",");
			for (String str : strs) {
				String[] arr = str.split("-");
				EnumerateDetailId id  = new EnumerateDetailId();
				id.setId(Long.parseLong(arr[0]));
				id.setIno(Long.parseLong(arr[1]));
				enumerateDetailDao.delete(id);
			}
		//	addLog(users, "删除枚举参数,参数ID：" +ids);
		}
	}

	public void update(EnumerateDetail enumerateDetail, Users users) {
		enumerateDetailDao.merge(enumerateDetail);
		//addLog(users, "修改枚举参数,参数值：" +enumerateDetail.getParavalue());
	}

	public void add(EnumerateDetail enumerateDetail, Users users) {
		List<EnumerateDetail> detailList = enumerateDetailDao.getByHQL("from EnumerateDetail as e where e.id.id = " + enumerateDetail.getId().getId() + " order by e.id.ino desc");
		if(detailList.size() > 0){
			enumerateDetail.getId().setIno(detailList.get(0).getId().getIno()+1);
		}else{
			enumerateDetail.getId().setIno(1L);
		}
		enumerateDetailDao.merge(enumerateDetail);
		//addLog(users, "添加枚举参数,参数值：" +enumerateDetail.getParavalue());
	}
	/*public void addLog(Users users, String description){
		OptLogs log = new OptLogs();
		log.setCreateTime(new Date());
		log.setDescription(description);
		log.setFromModel("枚举管理");
		log.setUserId(users.getUserId());
		if( users.getNickname() != null && !users.getNickname().equals("")){
			log.setUsername(users.getUsername() + "(" + users.getNickname() + ")");
		}else{
			log.setUsername(users.getUsername());
		}
		optLogsDao.merge(log);
	}*/
	public EnumerateDetailDao getenumerateDetailDao() {
		return enumerateDetailDao;
	}

	public void setenumerateDetailDao(EnumerateDetailDao enumerateDetailDao) {
		this.enumerateDetailDao = enumerateDetailDao;
	}


	public List<EnumerateDetail> getPage(String hql, int offset, int length) {
		// TODO Auto-generated method stub
		return enumerateDetailDao.getPage(hql, offset, length);
	}
}
