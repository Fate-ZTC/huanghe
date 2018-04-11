package com.parkbobo.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.parkbobo.dao.BaseDao;
import com.parkbobo.utils.GenericsUtils;
import com.parkbobo.utils.PageBean;




/**
 * <p>DAO层基础接口实现类<p>
 * @author LH
 * 
 */
@SuppressWarnings("unchecked")
@Component("baseDaoSupport")
public class BaseDaoSupport<T> implements BaseDao<T> {
	private Class<T> entityClass = GenericsUtils.getSuperClassGenricType(this.getClass());
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	/**
	 * 
	 * 保存实体
	 * @param  entity  实体
	 * 
	 */
	public void save(T entity) {
		hibernateTemplate.save(entity);
	}
	
	/**
	 * 
	 * 更新实体
	 * @param  entity  实体
	 * 
	 */
	public void update(T entity) {
		hibernateTemplate.update(entity);
	}
	
	/**
	 * 
	 * 更新或保存实体
	 * @param  entity  实体
	 * 
	 */
	public void merge(T entity){
		hibernateTemplate.merge(entity);
	}
	/**
	 * 
	 * 根据ID删除实体
	 * @param entityid  序列化实体ID
	 * 
	 */
	public void delete(Serializable entityid) {
		hibernateTemplate.delete(hibernateTemplate.load(entityClass, entityid));
	}
	
	/**
	 * 
	 * 根据ID获取实体
	 * @param entityid  序列化实体ID
	 * @return T 获取的实体对象 
	 * 
	 */
	public T get(Serializable entityid) {
		return (T) hibernateTemplate.get(entityClass,entityid);
	}
	/**
	 * 加锁查询
	 * @param entityid
	 * @param lockMode
	 * @return
	 */
	public T get(Serializable entityid, LockMode lockMode)
	{
		return (T) hibernateTemplate.load(entityClass, entityid, lockMode);
	}
	
	/**
	 * 
	 * 获取所有记录
	 * @return List<T> 符合条件的对象列表
	 * 
	 */
	public List<T> getAll() {
		return hibernateTemplate.loadAll(entityClass);
	}
	
	/**
	 * 
	 * 分页查询记录,使用hql，复杂分页采用，如：HQL语句中涉及到多个实体对象（表）
	 * @param hql   HQL语句
	 * @param offset   起始位置
	 * @param length   最大长度
	 * @return List<T> 符合条件的对象列表
	 * 
	 */
	
	public List<T> getPage(final String hql, final int offset, final int length) {
		List<T> list = (List<T>)hibernateTemplate.executeFind(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException,SQLException{
				List<T> result = (List<T>)session
									.createQuery(hql)
									.setFirstResult(offset)
									.setMaxResults(length)
									.list();
				return result;
			}
		});
		return list;
	}
	/**
	 * 
	 * 查询总记录数，使用hql
	 * @param  hql  HQL语句
	 * @return int  总记录数 
	 * 
	 */
	
	public int totalCount(String hql) {
		Object o = hibernateTemplate.find(hql).get(0);
		if(o != null)
		{
			return Integer.parseInt(String.valueOf(o));
		}
		else
		{
			return 0;
		}
		
	}
	
	/**
	 * 
	 * 分页查询记录，使用hql，普通分页采用此方法（HQL语句中只涉及一个实体对象（表））
	 * @param hql   HQL语句
	 * @param pageSize   每页显示条数
	 * @param page  当前页数
	 * @return PageBean<T> 符合条件的PageBean对象
	 * 
	 */
	@SuppressWarnings("static-access")
	
	public PageBean<T> pageQuery(final String hql, int pageSize,int page) {
		String countQueryString = " select count (*) " + removeSelect(removeOrders(hql));
		int allRow = 0;
		int size = 0;
		List countlist = hibernateTemplate.find(countQueryString);
		if(countlist != null){
			size = countlist.size();
		}
		if(size>0){
			allRow = Integer.parseInt(String.valueOf(countlist.get(0)));
		}
		int totalPage = PageBean.countTotalPage(pageSize, allRow);
		if(page > totalPage){page = totalPage;}
		int pageStartR = PageBean.countOffset(pageSize, page);
		if(pageStartR < 0){pageStartR = 0;}
		final int offset = pageStartR;
		final int length = pageSize;
		PageBean pageBean = new PageBean();
		pageBean.setTotalPage(totalPage);
		final int currentPage = PageBean.countCurrentPage(page);
		List<T> list = (List<T>)hibernateTemplate.executeFind(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException,SQLException{
				List<T> result = (List<T>)session
									.createQuery(hql)
									.setFirstResult(offset)
									.setMaxResults(length)
									.list();
				return result;
			}
		});
		pageBean.setPageSize(pageSize);
		pageBean.setCurrentPage(currentPage);
		pageBean.setAllRow(allRow);
		pageBean.setTotalPage(totalPage);
		pageBean.setList(list);
		pageBean.init();
		return pageBean;
	}
	/**
	 * 
	 * 根据HQL语句查询记录
	 * @param hql   HQL语句
	 * @return List<T> 符合条件的对象列表
	 * 
	 */
	
	public List<T> getByHQL(String hql) {
		return (List<T>) hibernateTemplate.find(hql);
	}
	
	/**
	 * 
	 * 获取全部对象,带排序字段与升降序参数
	 * @param orderBy 排序字段名
	 * @param isAsc boolean型
	 * @return List<T>   符合条件的对象列表
	 * 
	 */
	
	public List<T> getAll(String orderBy, boolean isAsc) {
		
		if(isAsc){
			return (List<T>) hibernateTemplate.findByCriteria(DetachedCriteria.forClass(entityClass).addOrder(Order.asc(orderBy)));
		}else{
			return (List<T>) hibernateTemplate.findByCriteria(DetachedCriteria.forClass(entityClass).addOrder(Order.desc(orderBy)));
		}
	}
	/**
	 * 
	 * 根据属性名和属性值查询对象.
	 * @param propertyName 字段名
	 * @param value 值
	 * @return List<T> 符合条件的对象列表
	 * 
	 */
	
	public List<T> getByProperty(String propertyName, Object value) {
		
		return (List<T>) hibernateTemplate.findByCriteria(DetachedCriteria.forClass(entityClass).add(Restrictions.eq(propertyName, value)));
	}
	/**
	 * 
	 * 根据属性名和属性值查询对象,带排序参数.
	 * @param propertyName  条件字段名
	 * @param value 值
	 * @param orderBy 排序字段名
	 * @param isAsc boolean型
	 * @return List<T> 符合条件的对象列表
	 * 
	 */
	
	public List<T> getByProperty(String propertyName, Object value,
			String orderBy, boolean isAsc) {
		
		if(isAsc){
			return (List<T>) hibernateTemplate.findByCriteria(DetachedCriteria.forClass(entityClass).add(Restrictions.eq(propertyName, value)).addOrder(Order.asc(orderBy)));
		}else{
			return (List<T>) hibernateTemplate.findByCriteria(DetachedCriteria.forClass(entityClass).add(Restrictions.eq(propertyName, value)).addOrder(Order.desc(orderBy)));
		}
	}
	/**
	 * 
	 * 根据属性名和属性值查询单个对象.
	 * @param propertyName  条件字段名
	 * @param value 值
	 * @return T 实体 
	 * 
	 */
	
	public T getUniqueByProperty(String propertyName, Object value) {
		
		List<T> list  = (List<T>) hibernateTemplate.findByCriteria(DetachedCriteria.forClass(entityClass).add(Restrictions.eq(propertyName, value)));
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 
	 * 根据单个属性名和属性值判断是否存在数据.
	 * @param propertyName  条件字段名
	 * @param value 值
	 * 
	 */
	
	public boolean existsByProperty(String propertyName, Object value) {
		
		List<T> list  = (List<T>) hibernateTemplate.findByCriteria(DetachedCriteria.forClass(entityClass).add(Restrictions.eq(propertyName, value)));
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 
	 * 根据多个属性名和属性值查询单个对象.
	 * @param propertyNames 字段名
	 * @param values 值
	 * @return T 实体 
	 * 
	 */
	
	public T getUniqueByPropertys(String[] propertyNames, Object[] values) {
		
		String hql = "from " + getEntityName(entityClass) + " as model where ";
		for(String propertyName : propertyNames){
			hql +=  " model." + propertyName + " = ? and";
		}
		hql = StringUtils.removeEnd(hql, "and");
		if(hibernateTemplate.find(hql, values).size()>0){
			return (T) hibernateTemplate.find(hql, values).get(0);
		}else{
			return null;
		}
	}
	/**
	 * 
	 * 根据多个属性名和属性值判断是否存在数据.
	 * @param propertyNames 字段名数组
	 * @param values 值数组
	 * 
	 */
	
	public boolean existsByPropertys(String[] propertyNames, Object[] values) {
		
		String hql = "from " + getEntityName(entityClass) + " as model where ";
		for(String propertyName : propertyNames){
			hql +=  " model." + propertyName + " = ? and";
		}
		hql = StringUtils.removeEnd(hql, "and");
		if(hibernateTemplate.find(hql, values).size()>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 
	 * 根据属性名和属性值数组查询对象.
	 * @param propertyNames 字段名数组
	 * @param values 值数组
	 * @return List<T> 符合条件的对象列表
	 * 
	 */
	
	public  List<T> getByPropertys(String[] propertyNames ,Object[] values){
		
		String hql = "from " + getEntityName(entityClass) + " as model where ";
		for(String propertyName : propertyNames){
			hql +=  " model." + propertyName + " = ? and";
		}
		hql = StringUtils.removeEnd(hql, "and");
		return (List<T>) hibernateTemplate.find(hql, values);
	}
	
	/**
	 * 
	 * 根据属性名和属性值模糊查询对象.
	 * @param propertyName 字段名
	 * @param value 值
	 * @return List<T> 符合条件的对象列表
	 * 
	 */
	
	public  List<T> fuzzyFindByProperty(String propertyName ,Object value){
		
		String hql =  "from " + getEntityName(entityClass) + " as model where " +
				" model." + propertyName + " like '" + value + "'";
		return (List<T>) hibernateTemplate.find(hql);
	}
	
	/**
	 * 局部更新.
	 * 根据主键ID局部更新一条数据一个字段值.
	 * @param  entityid 序列号实体ID
	 * @param  propertyName 需要更新字段名
	 * @param  value 字段新值
	 * 
	 */
	
	public boolean localUpdateOneField(Serializable entityid,  String propertyName,
			 Object value) {
		String queryString = "update " + getEntityName(entityClass) + " as model set model." +
				propertyName + " = ? where model." + getIdName(entityClass) + " = " + entityid;
		int i = hibernateTemplate.bulkUpdate(queryString, value);
		if(i==1){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 局部更新.注意values中的值不能为null
	 * 根据主键ID局部更新一条数据多个字段值.
	 * @param  entityid 序列号实体ID
	 * @param  propertyNames 需要更新字段名数组
	 * @param  values 字段新值数组
	 * 
	 */
	
	public boolean localUpdateOneFields(Serializable entityid,
			String[] propertyNames, Object[] values) {
		String queryString = "update " + getEntityName(entityClass) + " as model set ";
		for(String propertyName : propertyNames){
			queryString +=  " model." + propertyName + " = ? ,";
		}
		queryString = StringUtils.removeEnd(queryString, ",");
		queryString+= " where model." + getIdName(entityClass) + " = " + entityid;
		int i = hibernateTemplate.bulkUpdate(queryString, values);
		if(i==1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * 根据HQL语句批量更新.
	 * @param  queryString hql语句
	 * @param  values 值数组
	 * @return int 更新成功条数
	 */
	
	public int bulkUpdate(String queryString, Object[] values) {
		return hibernateTemplate.bulkUpdate(queryString, values);
	}
	/**
	 * 
	 * 批量保存实体.
	 * @param  list  
	 * 
	 */
	public void bulksave(final List<T> list) {
		 hibernateTemplate.execute(new HibernateCallback() {
			 public Object doInHibernate(Session session) throws HibernateException, SQLException {
				 for(int i = 0; i < list.size(); i++){
					 session.save(list.get(i));
					 if(list.size() < 20 && i == list.size()-1 || i%20 == 0){
						 session.flush();
						 session.clear();
					 }
				 }
				 return null;
	         }
	     });
	}
	/**
	 * 根据关键字ID数组批量删除.
	 * @param  ids 关键字ID数组
	 */
	
	public int bulkDelete(final Object[] ids) {
		final String queryString = "delete " +getEntityName(entityClass) +" as model where  model." + getIdName(entityClass) + " in (:ids) ";
		Object obj = hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(queryString);
                query.setParameterList("ids", ids);
                return query.executeUpdate();
            }
        });
		return (Integer) obj;
	}
	/**
	 * 根据HQL语句删除.
	 * @param  hql 关键字ID数组
	 */
	public int deleteByHql(final String hql) {
		Object obj = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				return query.executeUpdate();
			}
		});
		return (Integer) obj;
	}

	/**
	 * 
	 * 保存实体.
	 * @param  entity  实体对象
	 * @return 返回保存实体对象，一般用在需要获取报错实体的ID
	 */
	
	public T add(T entity) {
		hibernateTemplate.save(entity);
		return entity;
	}
	public void lock(T entity)
	{
		hibernateTemplate.lock(entity, LockMode.UPGRADE);
	}
	/**
     * 取得对象的主键名,辅助函数.
     * @param <T>
	 * @param clazz 实体类
     */
	private String getIdName(Class<T> clazz) {
        ClassMetadata meta =hibernateTemplate.getSessionFactory().getClassMetadata(clazz);
        String idName = meta.getIdentifierPropertyName();
        return idName;
    }
	/**
	 * 辅助函数.
	 * 获取实体的名称.
	 * @param <T>
	 * @param clazz 实体类
	 */
	private static <T> String getEntityName(Class<T> clazz){
		String entityname = clazz.getSimpleName();
		Entity entity = clazz.getAnnotation(Entity.class);
		if(entity.name()!=null && !"".equals(entity.name())){
			entityname = entity.name();
		}
		return entityname;
	}
	/**
	 * 辅助函数.
     * 去除hql的orderby 子句，用于pageQuery.
     *	@param hql 分页查询hql语句
     */
	private static String removeOrders(String hql) {
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(hql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }
    
    /**
     * 辅助函数.
     * 去除hql的select 子句，未考虑union的情况,用于pageQuery.
     * @param hql 分页查询hql语句
     */
	private static String removeSelect(String hql) {
        int beginPos = hql.toLowerCase().indexOf("from");
        return hql.substring(beginPos);
    }
	
}
