package com.parkbobo.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolUserRegionDao;
import com.parkbobo.model.PatrolUserRegion;
import com.parkbobo.utils.PageBean;

@Service
public class PatrolUserRegionService {

//	final String PATROL_ADMIN_CONFIG_PARAM = Configuration.getInstance().getValue("patrolAdminConfigParam");
//	final String patrolAdminConfigURL = Configuration.getInstance().getValue("patrolAdminConfigURL");
//	final String APP_KEY = Configuration.getInstance().getValue("AppKey");
//	final String SECRET = Configuration.getInstance().getValue("Secret");

	@Resource(name="patrolUserRegionDaoImpl")
	private PatrolUserRegionDao patrolUserRegionDao;



	/**
	 * 增加用户区域信息
	 * @param patrolUserRegion
	 * @return
	 */
	public PatrolUserRegion addRecord(PatrolUserRegion patrolUserRegion){
		return this.patrolUserRegionDao.add(patrolUserRegion);
	}
	/**
	 * 修改用户区域信息
	 */
	public PatrolUserRegion updateRecord(PatrolUserRegion patrolUserRegion){
		this.patrolUserRegionDao.update(patrolUserRegion);
		return patrolUserRegion;
	}
	/**
	 * 根据id获取信息
	 */
	public PatrolUserRegion getById(Integer id){
		return this.patrolUserRegionDao.get(id);
	}

	public PatrolUserRegion getUniqueByProperty(String propertyName,Object value){
		return this.patrolUserRegionDao.getUniqueByProperty(propertyName, value);
	}
	public List<PatrolUserRegion> getByProperty(String propertyName,Object value){
		return this.patrolUserRegionDao.getByProperty(propertyName, value);
	}
	public List<PatrolUserRegion> getByProperty(String propertyName ,Object value ,String orderBy,boolean isAsc){
		return this.patrolUserRegionDao.getByProperty(propertyName, value, orderBy, isAsc);
	}
	public List<PatrolUserRegion> getByHQL(String hql){
		return this.patrolUserRegionDao.getByHQL(hql);
	}
	public List<Object[]> getBySql(String sql){
		return this.patrolUserRegionDao.getBySql(sql);
	}

	public PatrolUserRegion getCountTime(String jobNum){
		String hql = "from PatrolUserRegion where endTime is null and  jobNum = '"+jobNum+"' order by startTime desc limit 1";
		List<PatrolUserRegion> list = this.patrolUserRegionDao.getByHQL(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 判断是否偷懒
	 * 
	 */
	public boolean isLazy(PatrolUserRegion patrolUserRegion,Integer lazyTime){
		if(new Date().getTime()-patrolUserRegion.getLastUpdateTime().getTime()>=1000*60*lazyTime){
			return true;
		}
		return false;
	}
	public void merge(PatrolUserRegion patrolUserRegion) {
		this.patrolUserRegionDao.merge(patrolUserRegion);
	}
	public List<PatrolUserRegion> getAbnormal() {
		String hql = "from PatrolUserRegion where endTime is null and status = 2";
		return this.patrolUserRegionDao.getByHQL(hql);
	}
	public List<PatrolUserRegion> getAll() {
		return this.patrolUserRegionDao.getAll();
	}
	public void bulkDelete(Integer[] ids){
		patrolUserRegionDao.bulkDelete(ids);
	}
	public PageBean<PatrolUserRegion> getPatrolUserBySth(String username,Integer regionId,Integer exceptionType,String startTime,String endTime,Integer page,Integer pageSize) throws UnsupportedEncodingException{
		String hql = "from PatrolUserRegion where 1=1 ";
		if (StringUtils.isNotBlank(username)) {
			hql += " and username like '%"+username+"%'";
		}
		if (StringUtils.isNotBlank(startTime)) {
			hql += " and startTime >= '"+startTime+"'";
		}
		if (StringUtils.isNotBlank(endTime)) {
			hql += " and endTime < '"+endTime+"'";
		}
		if (regionId!=null&&regionId!=-1) {
			hql += " and regionId = "+regionId;
		}
		if(exceptionType!=null&&exceptionType!=-1){
			if(exceptionType==1){
				hql += " and patrolException.type is not null ";
			}else{
				hql += " and patrolException is null ";
			}
		}
		hql += "order by lastUpdateTime desc";		
		
		return this.patrolUserRegionDao.pageQuery(hql,pageSize==null?12:pageSize, page==null?1:page);
	}
	public PatrolUserRegion getByJobNum(String jobNum) {
		String hql = "from PatrolUserRegion where jobNum ='"+ jobNum +"' and endTime is null order by startTime desc limit 1";
		List<PatrolUserRegion> list = this.patrolUserRegionDao.getByHQL(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	public List<PatrolUserRegion> getsByJobNum(String jobNum) {
		String hql = "from PatrolUserRegion where jobNum ='"+ jobNum +"' and endTime is null order by startTime desc";
		return   this.patrolUserRegionDao.getByHQL(hql);
	}


	/**
	 * 根据sql查询距离
	 * @param sql sql
	 * @return		返回计算的距离度,需要乘111000才能转化为m
     */
	public double getDistanceBySql(String sql) {
		return this.patrolUserRegionDao.getDistanceBySql(sql);
	}


//	/**
//	 * 进行消息推送
//	 * @param title		标题
//	 * @param content	内容
//     * @param alias		推送人员
//	 * @param type 		1 消防 2 安防
//     */
//	public void pushSend(String type,String title,String content,String alias) {
//		if(alias != null && !"".equals(alias)) {
//			JPushClientExample push = new JPushClientExample(APP_KEY, SECRET);
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("type", type);
//			map.put("title", title);
//			map.put("content", content);
//			push.aliasSendMsg(title, content, map, alias);
//		}
//	}
//
//
//	public String getPartrolAdminUserId() {
//		HttpRequest httpRequest = new HttpRequest();
//		String param = "moduleId=" + PATROL_ADMIN_CONFIG_PARAM;
//		String result = httpRequest.sendGet(patrolAdminConfigURL,param);
//		if(result != null && !"".equals(result)) {
//			JSONObject resultObject = JSONObject.parseObject(result);
//			if(resultObject != null) {
//				int code = resultObject.getIntValue("code");
//				boolean status = resultObject.getBoolean("status");
//				if(code == 200 && status) {
//					//获取data
//					JSONObject dataObject = resultObject.getJSONObject("data");
//					if(dataObject != null) {
//						System.out.println(dataObject.getString("privateUsers"));
//						return dataObject.getString("privateUsers");
//					}
//				}
//			}
//		}
//		return null;
//	}

}
