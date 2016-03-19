package org.xzc.sshb.dao.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.xzc.sshb.dao.IRecordDao;
import org.xzc.sshb.dao.IStateDao;
import org.xzc.sshb.domain.Record;
import org.xzc.sshb.domain.State;
import org.xzc.sshb.domain.User;

@Component
public class RecordDao extends BaseDao<Record> implements IRecordDao {
	private IStateDao stateDao;

	/**
	 * 用户
	 */
	@Override
	public List<Record> getByUser(User user) {
		return this.getByUser( user, -1, -1 );
	}

	/**
	 * 用户 分页 所有订单
	 */
	@Override
	public List<Record> getByUser(User user, int offset, int count) {
		return getByUserOrderBy( user, null, offset, count );
	}

	/**
	 * 用户 非状态
	 */
	@Override
	public List<Record> getByUserByNotState(User user, State state) {
		return this.getByUserByNotState( user, state, -1, -1 );
	}

	/**
	 * 用户,非状态,分页
	 */
	@Override
	public List<Record> getByUserByNotState(User user, State state, int offset, int count) {
		return getByUserByNotStateOrderBy( user, state, null, offset, count );
	}

	/**
	 * 用户 非状态 获得数量
	 */
	@Override
	public int getByUserByNotStateCount(User user, State state) {
		return ( (Long) getCurrentSession().createQuery( "select count(*) from Record where user=? and state!=?" )
				.setParameter( 0, user ).setParameter( 1, state ).uniqueResult() ).intValue();
	}

	/**
	 * 用户,非状态,排序
	 */
	@Override
	public List<Record> getByUserByNotStateOrderBy(User user, State state, String orderby) {
		return this.getByUserByNotStateOrderBy( user, state, orderby, -1, -1 );
	}

	/**
	 * 用户,非状态,排序,分页
	 */
	@Override
	public List<Record> getByUserByNotStateOrderBy(User user, State state, String orderby, int offset, int count) {
		Query q = getCurrentSession()
				.createQuery(
						"from Record where user=? and state!=? " + ( orderby == null ? "" : ( "order by " + orderby ) ) )
				.setParameter( 0, user ).setParameter( 1, state );
		if (offset != -1 && count != -1)
			q.setFirstResult( offset ).setMaxResults( count );
		return q.list();
	}

	/**
	 * 用户 状态
	 */
	@Override
	public List<Record> getByUserByState(User user, State state) {
		return this.getByUserByState( user, state, -1, -1 );
	}

	/**
	 * 根据用户 和 订单状态进行获得,有分页
	 */
	@Override
	public List<Record> getByUserByState(User user, State state, int offset, int count) {
		return this.getByUserByStateOrderBy( user, state, null, offset, count );
	}

	/**
	 * 根据用户 和 订单状态进行获得,返回数量,Query情况下返回的是Long
	 */
	@Override
	public int getByUserByStateCount(User user, State state) {
		return ( (Long) getCurrentSession().createQuery( "select count(*) from Record where user=? and state=?" )
				.setParameter( 0, user ).setParameter( 1, state ).uniqueResult() ).intValue();
	}

	/**
	 * 用户,状态,排序
	 */
	@Override
	public List<Record> getByUserByStateOrderBy(User user, State state, String orderby) {
		return this.getByUserByStateOrderBy( user, state, orderby, -1, -1 );
	}

	/**
	 * 用户,状态,排序,分页
	 */
	@Override
	public List<Record> getByUserByStateOrderBy(User user, State state, String orderby, int offset, int count) {
		Query q = getCurrentSession()
				.createQuery(
						"from Record where user=? and state=? " + ( orderby == null ? "" : ( "order by " + orderby ) ) )
				.setParameter( 0, user ).setParameter( 1, state );
		if (offset != -1 && count != -1)
			q.setFirstResult( offset ).setMaxResults( count );
		return q.list();
	}

	/**
	 * 数量
	 */
	@Override
	public int getByUserCount(User user) {
		return getByUserByNotStateCount( user, stateDao.get( State.RECORD_DELETED ) );
	}

	/**
	 * 用户,排序
	 */
	@Override
	public List<Record> getByUserOrderBy(User user, String orderby) {
		return this.getByUserOrderBy( user, orderby, -1, -1 );
	}

	/**
	 * 用户,排序,分页
	 */
	@Override
	public List<Record> getByUserOrderBy(User user, String orderby, int offset, int count) {
		Query q = getCurrentSession().createQuery(
				"from Record where user=? " + ( orderby == null ? "" : ( "order by " + orderby ) ) ).setParameter( 0,
				user );
		if (offset != -1 && count != -1)
			q.setFirstResult( offset ).setMaxResults( count );
		return q.list();
	}

	/*
	 * 标记为删除 即state=1
	 * */@Override
	public void markAsDeleted(Record record) {
		record = get( record.getId() );
		record.setState( stateDao.get( State.RECORD_DELETED ) );
	}

	@Override
	public void repair() {
		// 用coalesce解决null问题
		getCurrentSession()
				.createQuery(
						"update Record as r set r.totalPrice = (select coalesce(sum(ri.quantity*ri.item.price),0) from r.recordItems as ri)" )
				.executeUpdate();
		// .executeUpdate();
		// getCurrentSession()
		// .createSQLQuery(//原生sql语句也可以用ifnull解决问题
		// "update Record as r set r.totalPrice = (select ifnull(sum(ri.quantity*i.price),0) from recordItem as ri left join item as i  on ri.item_id=i.id where ri.record_id=r.id)"
		// )
		// .executeUpdate();
	}

	@Resource
	public void setStateDao(IStateDao stateDao) {
		this.stateDao = stateDao;
	}

	@Override
	public List<Record> listByState(State state) {
		return getCurrentSession().createQuery( "from Record where state=?" ).setParameter( 0, state ).list();
	}

	@Override
	public List<Record> list(User user, Date beginTime, Date endTime) {
		Criteria c = getCurrentSession().createCriteria( Record.class ).addOrder( Order.desc( "id" ) );
		if (user != null)
			c.add( Restrictions.eq( "user", user ) );
		if (beginTime != null)
			c.add( Restrictions.ge( "buyTime", beginTime ) );
		if (endTime != null)
			c.add( Restrictions.le( "buyTime", endTime ) );
		return c.list();
	}

}
