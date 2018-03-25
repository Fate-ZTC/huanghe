package com.parkbobo.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.parkbobo.dao.FirePatrolUserDao;
import com.parkbobo.model.FirePatrolUser;
import com.parkbobo.utils.PageBean;

@Service
public class FirePatrolUserService {
	
	@Resource(name="firePatrolUserDaoImpl")
	private FirePatrolUserDao firePatrolUserDao;
	
	/**
	 * 登录
	 */
	public FirePatrolUser userLogin(String jobNum,String password){
		if(!(StringUtils.isNotBlank(jobNum)&&StringUtils.isNotBlank(password))){
			return null;
		}
		FirePatrolUser patrolUser = this.firePatrolUserDao.getUniqueByPropertys(new String[]{"jobNum","password"}, new Object[]{jobNum,password});
		return patrolUser;
	}
	/**
	 * 获取所有用户信息
	 * @return 用户信息集合
	 */
	public List<FirePatrolUser> getAllUser(){
		return this.firePatrolUserDao.getAll();
	}
	/**
	 * 分页查询
	 * @param username 用户名
	 * @param jobNum 工号
	 * @param page 页码
	 * @param pageSize 每页条数
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public PageBean<FirePatrolUser> getUsers(String hql,Integer pageSize,Integer page) throws UnsupportedEncodingException{
		return this.firePatrolUserDao.pageQuery(hql, pageSize, page);
	}
	
	public int countUsers(String username,String jobNum) throws UnsupportedEncodingException{
		String hql = "from FirePatrolUser where idDel = 0";
		if(StringUtils.isNotBlank(username)){
			hql += " and username like '% " +URLDecoder.decode(URLEncoder.encode(username, "ISO8859_1"), "UTF-8")+"'%";
		}
		if(StringUtils.isNotBlank(jobNum)){
			hql +=" and jobNum like '% "+jobNum+"%'";
		}
		return this.firePatrolUserDao.totalCount(hql);
	}
	/**
	 * 根据id删除巡查员
	 */
	public void deleteById(Integer id){
		this.firePatrolUserDao.delete(id);
	}
	/**
	 * 新增用户 
	 * @return  0 工号已存在  1成功   2未知错误
	 */
	public int addUser(FirePatrolUser patrolUser){
		boolean isExist = this.firePatrolUserDao.existsByProperty("jobNum",patrolUser.getJobNum());
		if(isExist){
			return 0;
		}
		try {
			this.firePatrolUserDao.add(patrolUser);
		} catch (Exception e) {
			return 2;
		}
		return 1;
	}
	/**
	 * 修改用户信息
	 * @return 0工号重复   1成功   2未知错误
	 */
	public int updateUser(FirePatrolUser patrolUser){
		String hql = "from PatrolUser where jobNum='"+patrolUser.getJobNum()+"' and "+
				" id <>"+patrolUser.getId();
		List<FirePatrolUser> list = this.firePatrolUserDao.getByHQL(hql);
		if(list != null && list.size()>0){
			return 0;
		}
		try {
			this.firePatrolUserDao.update(patrolUser);
		} catch (Exception e) {
			return 2;
		}
		return 1;
	}
	/**
	 * 根据id获取用户信息
	 */
	public FirePatrolUser getById(Integer id){
		return this.firePatrolUserDao.get(id);
	}
	/**
	 * 根据jobNum 获取用户
	 */
	public FirePatrolUser getByJobNum(String jobNum){
		return this.firePatrolUserDao.getUniqueByProperty("jobNum", jobNum);
	}
	
	public List<FirePatrolUser> getAll(){
		return this.firePatrolUserDao.getAll();
	}
		
	public void update(FirePatrolUser patrolUser) {
		this.firePatrolUserDao.update(patrolUser);
	}
	public List<FirePatrolUser> getBySth(String username, String jobNum) throws UnsupportedEncodingException {
		String hql = " from FirePatrolUser where 1=1";
		if(StringUtils.isNotBlank(username)){
			hql += " and username like '% " +URLDecoder.decode(URLEncoder.encode(username, "ISO8859_1"), "UTF-8")+"'%";
		}
		if(StringUtils.isNotBlank(jobNum)){
			hql +=" and jobNum like '% "+jobNum+"%'";
		}
		return this.firePatrolUserDao.getByHQL(hql);
	}
}