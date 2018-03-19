package com.parkbobo.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.parkbobo.dao.PatrolUserDao;
import com.parkbobo.model.PatrolUser;

@Service
public class PatrolUserService {

	@Resource(name="patrolUserDaoImpl")
	private PatrolUserDao patrolUserDao;
	/**
	 * 登录
	 */
	public PatrolUser userLogin(String jobNum,String password){
		if(!(StringUtils.isNotBlank(jobNum)&&StringUtils.isNotBlank(password))){
			return null;
		}
		PatrolUser patrolUser = this.patrolUserDao.getUniqueByPropertys(new String[]{"jobNum","password"}, new Object[]{jobNum,password});
		return patrolUser;
	}
	/**
	 * 获取所有用户信息
	 * @return 用户信息集合
	 */
	public List<PatrolUser> getAllUser(){
		return this.patrolUserDao.getAll();
	}
	/**
	 * 根据id删除巡查员
	 */
	public void deleteById(Integer id){
		this.patrolUserDao.delete(id);
	}
	/**
	 * 新增用户 
	 * @return  0 工号已存在  1成功   2未知错误
	 */
	public int addUser(PatrolUser patrolUser){
		boolean isExist = this.patrolUserDao.existsByProperty("jobNum",patrolUser.getJobNum());
		if(isExist){
			return 0;
		}
		try {
			this.patrolUserDao.add(patrolUser);
		} catch (Exception e) {
			return 2;
		}
		return 1;
	}
	/**
	 * 修改用户信息
	 * @return 0工号重复   1成功   2未知错误
	 */
	public int updateUser(PatrolUser patrolUser){
		String hql = "from PatrolUser where jobNum='"+patrolUser.getJobNum()+"' and "+
				" id <>"+patrolUser.getId();
		List<PatrolUser> list = this.patrolUserDao.getByHQL(hql);
		if(list != null && list.size()>0){
			return 0;
		}
		try {
			this.patrolUserDao.update(patrolUser);
		} catch (Exception e) {
			return 2;
		}
		return 1;
	}
	/**
	 * 根据id获取用户信息
	 */
	public PatrolUser getById(Integer id){
		return this.patrolUserDao.get(id);
	}
	/**
	 * 根据jobNum 获取用户
	 */
	public PatrolUser getByJobNum(String jobNum){
		return this.patrolUserDao.getUniqueByProperty("jobNum", jobNum);
	}
	
	public List<PatrolUser> getAll(){
		return this.patrolUserDao.getAll();
	}
		
	public void update(PatrolUser patrolUser) {
		this.patrolUserDao.update(patrolUser);
	}
	
}
