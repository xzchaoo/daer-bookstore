package org.xzc.sshb.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.xzc.sshb.dao.IRecordItemDao;
import org.xzc.sshb.domain.Item;
import org.xzc.sshb.domain.RecordItem;
import org.xzc.sshb.domain.State;

/**
 * @author xzchaoo
 * 
 */
@Component
public class RecordItemDao extends BaseDao<RecordItem> implements IRecordItemDao {

	@Override
	public List<RecordItem> listByItem(Item item) {
		return this.listByItem( item, -1, -1 );
	}

	@Override
	public List<RecordItem> listByItem(Item item, int offset, int count) {
		return listByItemOrderBy( item, offset, count, null );
	}

	@Override
	public List<RecordItem> listByItemByState(Item item, State state) {
		return listByItemByState( item, state, -1, -1 );
	}

	@Override
	public List<RecordItem> listByItemByState(Item item, State state, int offset, int count) {
		return listByItemByStateOrderBy( item, state, offset, count, null );
	}

	@Override
	public int listByItemByStateCount(Item item, State state) {
		return ( (Long) getCurrentSession()
				.createQuery( "select count(*) from RecordItem where item= ? and state=?" )
				.setParameter( 0, item ).setParameter( 1, state ).uniqueResult() ).intValue();
	}

	@Override
	public List<RecordItem> listByItemByStateOrderBy(Item item, State state, int offset, int count,
			String orderBy) {
		return listByItemByStateOrderBy( item, state.getId(), offset, count, orderBy );
	}

	@Override
	public int listByItemCount(Item item) {
		return ( (Long) getCurrentSession()
				.createQuery( "select count(*) from RecordItem where item= ?" )
				.setParameter( 0, item ).uniqueResult() ).intValue();
	}

	@Override
	public List<RecordItem> listByItemOrderBy(Item item, int offset, int count, String orderBy) {
		Query q = getCurrentSession().createQuery(
				"from RecordItem where item=?" + ( orderBy == null ? "" : "order by " + orderBy ) )
				.setParameter( 0, item );
		if (offset != -1 && count != -1)
			q.setFirstResult( offset ).setMaxResults( count );
		return q.list();
	}

	@Override
	public List<RecordItem> listByItemOrderBy(Item item, String orderBy) {
		return listByItemOrderBy( item, -1, -1, orderBy );
	}

	@Override
	public List<RecordItem> listByItemByStateOrderBy(Item item, int state, int offset, int count,
			String orderBy) {
		Query q = getCurrentSession()
				.createQuery(
						"from RecordItem where item=? and state.id=?"
								+ ( orderBy == null ? "" : "order by " + orderBy ) )
				.setParameter( 0, item ).setInteger( 1, state );
		if (offset != -1 && count != -1)
			q.setFirstResult( offset ).setMaxResults( count );
		return q.list();
	}
}
