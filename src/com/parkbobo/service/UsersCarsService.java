package com.parkbobo.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.parkbobo.dao.UsersCarsDao;
import com.parkbobo.model.UserAuth;
import com.parkbobo.model.UsersCars;
import com.parkbobo.utils.PageBean;

@Component("usersCarsService")
public class UsersCarsService {
	@Resource(name = "usersCarsDaoImpl")
	private UsersCarsDao usersCarsDao;
	
	public List<UsersCars> getByHql(String hql){
		return usersCarsDao.getByHQL(hql);
	}
	
	public void delete(Serializable kid){
		usersCarsDao.delete(kid);
	}
	
	public UsersCars get(Serializable kid){
		return usersCarsDao.get(kid);
	}
	
	public void update(UsersCars usrCar){
		usersCarsDao.update(usrCar);
	}
	
	public UsersCars add(UsersCars usrCar){
		return usersCarsDao.add(usrCar);
	}
	
	/**
	 * 根据车牌号、手机号码检测是否已经存在
	 * @param plate
	 * @param mobile
	 * @return
	 */
	public Boolean existWithPlateMobile(String plate, String mobile){
		String[] params = {"carPlate", "mobile"};
		Object[] values = {plate, mobile};
		return usersCarsDao.existsByPropertys(params, values);
	}
	
	/**
	 * 根据手机号码获取绑定的车牌列表
	 * @param mobile
	 * @return
	 */
	public List<UsersCars> loadByMobile(String mobile){
		String hql = "from UsersCars where mobile = '" + mobile + "'";
		return usersCarsDao.getByHQL(hql);
	}
	
	/**
	 * 根据手机号码获取审核通过的车牌列表
	 * @param mobile
	 * @return
	 */
	public List<UsersCars> loadByMobileAuth(String mobile){
		String hql = "from UsersCars where mobile = '" + mobile + "' and authStatus=1";
		return usersCarsDao.getByHQL(hql);
	}

	public UsersCarsDao getUsersCarsDao() {
		return usersCarsDao;
	}

	public void setUsersCarsDao(UsersCarsDao usersCarsDao) {
		this.usersCarsDao = usersCarsDao;
	}
	
	/**
	 * 分页查询
	 * */
	public void pageQuery(String hql,Integer pageSize,Integer page){
		this.usersCarsDao.pageQuery(hql, pageSize, page);
	}

	/**
	 * 信息认证分页查询
	 * */
	public PageBean<UserAuth> pageQuerySql(String startTime,String endTime,String mobile,Integer pageSize,Integer page){
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT u.mobile,string_agg(to_char(c.auth_status,'9'),',') as driver_status,u.auth_status as driving_status,u.posttime from lq_users_cars as c RIGHT JOIN lq_users as u ON u.mobile=c.mobile where 1=1");
		int allRow = 0;
		int size = 0;
		String countHql = "from Users where 1=1";
		if(StringUtils.isNotBlank(startTime)){
			countHql += " and posttime>='" + startTime + "'";
			sb.append(" and u.posttime>='" + startTime + "'");
		}
		if(StringUtils.isNotBlank(endTime)){
			countHql += " and posttime<='" + endTime + "'";
			sb.append(" and u.posttime<='" + endTime + "'");
		}
		if(StringUtils.isNotBlank(mobile)){
			countHql += " and mobile like '%" + mobile + "%'";
			sb.append(" and u.mobile like '%" + mobile + "%'");
		}
		List<UsersCars> countList = this.usersCarsDao.getByHQL(countHql);
		if(countHql !=null){
			allRow = countList.size();
		}
		int totalPage = PageBean.countTotalPage(pageSize, allRow);
		if(page > totalPage){page = totalPage;}
		int pageStartR = PageBean.countOffset(pageSize, page);
		if(pageStartR < 0){pageStartR = 0;}
		final int offset = pageStartR;
		final int length = pageSize;
		PageBean<UserAuth> pageBean = new PageBean();
		pageBean.setTotalPage(totalPage);
		final int currentPage = PageBean.countCurrentPage(page);
		sb.append(" GROUP BY u.mobile ORDER BY driver_status,driving_status LIMIT "+length+" OFFSET "+offset);
		List<Object[]> list = this.usersCarsDao.getBySql(sb.toString());
		List<UserAuth> userAuths = new ArrayList<UserAuth>();
		if(list!=null && list.size()>0){
			UserAuth userAuth = null;
			for (int i = 0; i < list.size(); i++) {
				userAuth = new UserAuth();
				userAuth.setMobile((String)list.get(i)[0]);
				userAuth.setDriverStatus(list.get(i)[1]==null?null:(String)list.get(i)[1]);
				userAuth.setDrivingStauts(list.get(i)[2]==null?null:(Integer)list.get(i)[2]);
				userAuth.setPosttime(list.get(i)[3]==null?null:(Date)list.get(i)[3]);
				userAuths.add(userAuth);
			}
		}
		pageBean.setPageSize(pageSize);
		pageBean.setCurrentPage(currentPage);
		pageBean.setAllRow(allRow);
		pageBean.setTotalPage(totalPage);
		pageBean.setList(userAuths);
		pageBean.init();
		return pageBean;
	}
	
}
