package org.xzc.sshb.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.xzc.sshb.dao.IBaseDao;

/**
 * DAO基类
 * 
 * @author xzchaoo
 * 
 * @param <T>
 */
public class BaseDao<T> implements IBaseDao<T> {
	protected Class<T> clazz;
	protected SessionFactory sessionFactory;

	public BaseDao() {
		// 反射拿到class
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];
	}

	@Override
	public void add(T t) {
		getCurrentSession().save( t );
	}

	@Override
	public void delete(T t) {
		getCurrentSession().delete( t );
	}

	@Override
	public T get(int id) {
		return (T) getCurrentSession().get( clazz, id );
	}

	@Override
	public List<T> get(List<Integer> ids) {
		return getCurrentSession().createCriteria( clazz ).add( Restrictions.in( "id", ids ) ).list();
	}

	@Override
	public List<T> list() {
		return this.list( -1, -1 );
	}

	@Override
	public List<T> list(int offset, int count) {
		return this.listOrderBy( null, offset, count );
	}

	@Override
	public int listCount() {
		return ( (Long) getCurrentSession().createQuery( "select count(*) from " + clazz.getName() ).uniqueResult() )
				.intValue();
	}

	@Override
	public List<T> listOrderBy(String orderby) {
		return this.listOrderBy( orderby, -1, -1 );
	}

	@Override
	public List<T> listOrderBy(String orderby, int offset, int count) {
		Query q = getCurrentSession().createQuery(
				"from " + clazz.getName() + ( orderby == null ? "" : ( " order by " + orderby ) ) );
		if (offset != -1 && count != -1)
			q.setFirstResult( offset ).setMaxResults( count );
		return q.list();
	}

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void update(T t) {
		getCurrentSession().update( t );
	}

	/**
	 * 方便使用
	 * 
	 * @return
	 */
	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}
