package com.mobile.service;




import com.mobile.dao.EnumerateDetailDao;
import com.mobile.dao.UsersDao;
import com.mobile.model.Users;
import com.parkbobo.utils.MD5;
import com.system.utils.PageBean;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;


@Component("usersService")
public class UsersService {
	@Resource(name = "usersDaoImpl")
	private UsersDao usersDao;
	@Resource(name="enumerateDetailDaoImpl")
	private EnumerateDetailDao enumerateDetailDao;
	/**
	 * 分页查询用户
	 * @param hql
	 * @param pageSize
	 * @param page
	 * @return
	 */
	public PageBean<Users> loadPage(String hql, int pageSize, int page) {
		return usersDao.pageQuery(hql, pageSize, page);
	}
	/**
	 * 通过userid字段检测是否有该用户信息
	 * 如有则返回该用户
	 * 没有返回null
	 * @param usercode
	 * @return
	 */
	public Users checkExistByUserId(String usercode) {
		if(usersDao.existsByProperty("userid", usercode)){
			return usersDao.getUniqueByProperty("userid", usercode);
		}
		else{
			return null;
		}
	}
	/**
	 * 通过单个字段查询
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public Users getUniqueByProperty(String propertyName, Object value) {
		return usersDao.getUniqueByProperty(propertyName, value);
	}
	/**
	 * 局部更新
	 * @param entityid
	 * @param propertyNames
	 * @param values
	 */
	public void localUpdateOneFields(Serializable entityid,
			String[] propertyNames, Object[] values) {
		usersDao.localUpdateOneFields(entityid, propertyNames, values);
	}
	/**
	 * 通过HQL语句查询
	 * @param hql
	 * @return
	 */
	public List<Users> getByHql(String hql) {
		return usersDao.getByHQL(hql);
	}
	/**
	 * 通过主键ID查询
	 * @param usersId
	 * @return
	 */
	public Users getById(Integer usersId) {
		return usersDao.get(usersId);
	}
	
	public Users get(){
		String hql = "from Users where userId =1";
		return this.usersDao.getByHQL(hql).get(0);
	}
	
	public List<Object[]> getBySql(String sql){
		return usersDao.getBySql(sql);
	}
	/**
	 * 通过ID删除企业
	 * @param ids
	 * @param users
	 */
	/*public void bulkDelete(String ids, Users users) {
		if(ids.length() > 0){
			String[] strs = ids.split(",");
			for (int i=0; i< strs.length; i++) {
//				authenticationDao.delete( Long.valueOf(strs[i]));
				authenticationDao.deleteByHql("delete from Authentication as u where  u.companyid = " + Long.valueOf(strs[i]));
			}
			addLog(users, "删除企业用户,企业编号：" +ids);
		}
	}*/
	
	
	/**
	 * 通过ID锁定用户，多个用户ID用','分隔
	 * @param ids
	 * @param users
	 */
	public void lock(String ids, Users users) {
		if(ids.length() > 0){
			String[] strs = ids.split(",");
			for (int i=0; i< strs.length; i++) {
				if(Long.valueOf(strs[i]) != 1){
					usersDao.localUpdateOneField(Long.valueOf(strs[i]), "activation", (short) 0);
				}
			}
//			addLog(users, "锁定账号，账号ID："+ids);
		}
	}
	/**
	 * 通过ID解锁用户，多个用户ID用','分隔
	 * @param ids
	 * @param users
	 */
	public void unlock(String ids, Users users) {
		if(ids.length() > 0){
			String[] strs = ids.split(",");
			for (int i=0; i< strs.length; i++) {
				if(Long.valueOf(strs[i]) != 1){
					usersDao.localUpdateOneField(Long.valueOf(strs[i]), "activation", (short) 1);
				}
			}
//			addLog(users, "解锁账号，账号ID："+ids);
		}
	}
	/**
	 * 审核
	 * @param id
	 * @param authentication
	 * @param users
	 */
	/*public void review(String mobiles , Integer isSms ,String ids, Authentication authentication, Users users) {
		if(ids.length() > 0){
			String[] strs = ids.split(",");
			for (int i=0; i< strs.length; i++) {
				Authentication auth = authenticationDao.get(Integer.valueOf(strs[i]));
				auth.setVerifytime(new Date());
				auth.setStatus(authentication.getStatus());
				//auth.setComments(authentication.getComments());
				auth.setUserid(users.getUserId());
				auth.setUsername(users.getUsername());
				authenticationDao.merge(auth);
				if(authentication.getStatus() == 1){
					List<Users> usersList = usersDao.getByHQL("from Users as u  where u.authentication.companyid =" + Long.valueOf(strs[i]));
					for (Users _users : usersList) {
						_users.setActivation(1);
						usersDao.merge(_users);
						初始化一个好友聊天分类
						 * 
						initCategory(_users);
					}
				}
				
				
			}
			if(isSms == 1) {
				if(mobiles != null && mobiles.length() > 0){
					mobiles.substring(0, mobiles.length()-1);
					EnumerateDetail enums = new EnumerateDetail();
					if(authentication.getStatus() == 1){
						enums =  enumerateDetailDao.getUniqueByProperty("paraname", "reviewsuccess");
					}
					else if(authentication.getStatus() == 2){
						enums =  enumerateDetailDao.getUniqueByProperty("paraname", "reviewfail");
					}
					if(enums != null){
						String phonelist = mobiles;
						String content = enums.getParavalue();
						SMSSend.getDefaultInstance().sendSMS(phonelist, content);
					}
				}
			}
			addLog(users, "审核企业用户，企业编号："+ids);
		}
		
	}*/
	
	
	/**
	 * 重置密码为：123456
	 * @param id
	 * @param users
	 */
	/*public void resetpwd(Integer id, Users users) {
		Users u = usersDao.get(id);
		if(u.getUsersGroup().getGroupid() == 1){
			ShaPasswordEncoder sp = new ShaPasswordEncoder();
			u.setPassword(sp.encodePassword("123456", u.getUsername()));
			usersDao.merge(u);
			addLog(users, "重置密码，账号："+u.getUsername());
		}
	}
	public boolean existsByProperty(String propertyName, Object value){
		return usersDao.existsByProperty(propertyName, value);
	}*/
	/**
	 * 企业用户注册
	 * @param authentication
	 * @param users
	 */
	/*public void register(Authentication authentication, Users users) {
		authentication.setPosttime(new Date());
		authentication.setStatus(0);
		authentication = authenticationDao.add(authentication);
		
		UsersGroup usersGroup =  new UsersGroup();
		usersGroup.setGroupid(1);
		users.setAuthentication(authentication);
		users.setUsersGroup(usersGroup);
		users.setMemo("0");//0:企业主账号，1：子账号
		users.setRegisterTime(new Date());
		ShaPasswordEncoder sp = new ShaPasswordEncoder();
		users.setPassword(sp.encodePassword("123456", users.getUsername()));
		users.setActivation((short) 0);
		users.setLoginCount(0L);
		users.setIsAuth((short) 1);
		users.setStatus((short) 0);
		Role role = new Role();
		role.setRoleId(1);
		users.setRole(role);
		users = usersDao.add(users);
		
	}*/
	/**
	 * 添加系统日志
	 * @param users
	 * @param description
	 */
	/*public void addLog(Users users, String description){
		OptLogs log = new OptLogs();
		log.setCreateTime(new Date());
		log.setDescription(description);
		log.setFromModel("企业用户");
		log.setUserId(users.getUserId());
		if( users.getNickname() != null && !users.getNickname().equals("")){
			log.setUsername(users.getUsername() + "(" + users.getNickname() + ")");
		}else{
			log.setUsername(users.getUsername());
		}
		optLogsDao.merge(log);
	}*/
	
//	/**
//	 * 查询用户邮箱是否存在
//	 * @return
//	 */
//	@Transactional
//	public String getPasswordByEmail(Users user) {
//          if(user!=null){ 
//		List<Users> users = this.usersDao.getByHQL(" from Users s where s.username ='"+user.getUsername()+"' and s.email = '"+user.getMail()+"'");
//     
//		Users u = new Users();
//		if(users !=null && users.size()>0){
//			u = users.get(0);
//			String uuid = UUID.randomUUID().toString().replace("-", "");
//			u.setActivationCode(uuid);
//			this.usersDao.update(u);
//			return uuid;
//		}
//          }
//		return "error";
//	}
	
	
	
	
	/**
	 * 重置密码
	 * @return
	 */
	/*public boolean resetPassword(Users user){
		System.out.println( user.getUsername()+user.getEmail());
		List<Users> users = this.usersDao.getByHQL(" from Users s where s.activationCode ='"+user.getActivationCode()+"' and s.email='"+user.getEmail()+"'");
		System.out.println(users.size()+"PPPPP");
		Users u = new Users();
		if(users !=null && users.size()>0){
			u = users.get(0);
			u.setActivationCode("");
			ShaPasswordEncoder sp = new ShaPasswordEncoder();
			u.setPassword(sp.encodePassword(user.getPassword(), u.getUsername()));
//			this.usersDao.update(u);
			return true;
		}
		    return false;
	} */
	
	
	public List<Users> getByHQL(String hql){
		return this.usersDao.getByHQL(hql);
	}
	
	/**
	 * 修改密码
	 * @return
	 */
	public void update(Users users){
		this.usersDao.update(users);
	}
	
	/**
	 * 
	 * @return
	 */
	
	public void merge(Users users){
		this.usersDao.merge(users);
	}
	
	public Users add(Users users){
		return usersDao.addEntity(users);
		/*Users user = usersDao.getUniqueByProperty("username", users.getUsername());
		//保存用户权限关系
		List<Resources> list = resourcesDao.getByHQL("from Resources where resourcetype = '1'");

		for (Resources res: list) {
			UsersResources usersResources = new UsersResources();
			UsersResourcesId id = new UsersResourcesId();
			id.setUserId(user.getUserId());
			id.setResourcesId(res.getResourcesId());
			usersResources.setId(id);
			usersResources.setType((short) 1);
			usersResourcesDao.merge(usersResources);
		}*/
	}
	
	
	
	public UsersDao getUsersDao() {
		return usersDao;
	}

	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}
	
	public List<Users> getHql(String hql) {
		// TODO Auto-generated method stub
		return usersDao.getByHQL(hql);
	}
	public EnumerateDetailDao getEnumerateDetailDao() {
		return enumerateDetailDao;
	}
	public void setEnumerateDetailDao(EnumerateDetailDao enumerateDetailDao) {
		this.enumerateDetailDao = enumerateDetailDao;
	}
	public Users getUserByUsername(String username) {
		Users user = this.usersDao.getUniqueByProperty("username", username);
		if(user != null){
			return user;
		}else{
			return null;
		}
		
	}
	public Users login(String nickname, String password) {
		String hql = "from Users s where s.nickname='"+nickname+"'";
		List<Users> users = usersDao.getByHQL(hql);
		if(users!=null && users.size()>0){
			Users user = users.get(0);
			String dbPassword = user.getPassword();
			String md5password = MD5.getDefaultInstance().MD5Encode(password);
			if(md5password.equals(dbPassword)){
				return user;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	public Users openidToUsers(String openid) {
		String hql = "from Users s where s.openid='"+openid+"'";
		return null;
	}
	public Users useridToUser(String userid) {
		String hql = "from Users s where s.userid='"+userid+"'";
		List<Users> users = usersDao.getByHQL(hql);
		if(users!=null && users.size()>0){
			return users.get(0);
		}
		return null;
	}
	public Users getUsercode(String usercode) {
		String hql = "from Users s where s.usercode='"+usercode+"'";
		List<Users> users = usersDao.getByHQL(hql);
		if(users!=null && users.size()>0){
			return users.get(0);
		}
		return null;
	}

	/**
	 * 根据用户获取积分
	 * @param usercode
	 * @return
	 */
	public Integer loadPoint(String usercode){
		Users users = usersDao.getUniqueByProperty("userid", usercode);
		if(users.getPoints() != null){
			return users.getPoints();
		} else{
			users.setPoints(0);
			usersDao.update(users);
			return 0;
		}
	}

	/**
	 * 根据用户增减积分
	 * @param usercode
	 * @param points
	 * @return
	 */
	public Integer addPoint(String usercode, Integer points){
		Users users = usersDao.getUniqueByProperty("userid", usercode);
		users.setPoints(users.getPoints() + points);
		usersDao.update(users);
		return users.getPoints();
	}
	
	
}
