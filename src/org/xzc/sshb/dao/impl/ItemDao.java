package org.xzc.sshb.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.xzc.sshb.dao.IItemDao;
import org.xzc.sshb.domain.Item;
import org.xzc.sshb.utils.StringUtils;
import org.xzc.sshb.value.RankItemValue;

@Component
public class ItemDao extends BaseDao<Item> implements IItemDao {

	/**
	 * 拼凑出 %name%的格式,记得先转义
	 * 
	 * @param text
	 * @return
	 */
	private static String matchAnyWhere(String text) {
		return "%" + StringUtils.escapeSqlLike( text ) + "%";
	}

	@Override
	public int listByNameKeywordCount(int category, String name) {
		String sql = "select count(*) from Item where name like ?";
		if (category != 0)
			sql += " and category.id=?";
		Query q = getCurrentSession().createQuery( sql ).setString( 0, matchAnyWhere( name ) );
		if (category != 0)
			q.setInteger( 1, category );
		return ( (Long) q.uniqueResult() ).intValue();
	}

	@Override
	public List<Item> listByNameKeyword(int category, String name, int offset, int count) {
		String sql = "from Item where name like ?";
		if (category != 0)
			sql += " and category.id=?";
		sql += "order by id asc";
		Query q = getCurrentSession().createQuery( sql ).setString( 0, matchAnyWhere( name ) );
		if (category != 0)
			q.setInteger( 1, category );
		if (offset != -1 && count != -1)
			q.setFirstResult( offset ).setMaxResults( count );
		return q.list();
	}

	@Override
	public List<Item> listByNameKeyword(String name) {
		return this.listByNameKeyword( 0, name, -1, -1 );
	}

	@Override
	public List<RankItemValue> listSaleRank(Date d, int count) {
		List<Object[]> list = getCurrentSession()
				.createQuery(
						"select ri.item, sum(ri.quantity) as quantity2 from RecordItem as ri where ri.record.buyTime>? group by ri.item order by sum(ri.quantity) desc" )
				.setDate( 0, d ).setMaxResults( count ).list();
		return objectsToRankItemValueList(list);
		/*List<RankItemValue> ret = new ArrayList<RankItemValue>( list.size() );
		for (Object[] oo : list) {
			RankItemValue v = new RankItemValue();
			v.item = (Item) oo[0];
			v.quantity = ( (Long) oo[1] ).intValue();
			ret.add( v );
		}
		return ret;*/
	}

	private static List<RankItemValue> objectsToRankItemValueList(List<Object[]> list){
		List<RankItemValue> ret = new ArrayList<RankItemValue>( list.size() );
		for (Object[] oo : list) {
			RankItemValue v = new RankItemValue();
			v.item = (Item) oo[0];
			v.quantity = ( (Long) oo[1] ).intValue();
			ret.add( v );
		}
		return ret;
	}
	@Override
	public List<RankItemValue> listSaleRank(int count) {
		List<Object[]> list = getCurrentSession()
				.createQuery(
						"select ri.item, sum(ri.quantity) as quantity2 from RecordItem as ri group by ri.item order by sum(ri.quantity) desc" )
				.setMaxResults( count ).list();
		return objectsToRankItemValueList(list);
	}

	@Override
	public int count(List<Integer> ids) {
		return ( (Long) getCurrentSession().createQuery( "select count(*) from Item where id in (:ids)" )
				.setParameterList( "ids", ids ).uniqueResult() ).intValue();
	}

	@Override
	public List<Item> listByCategory(int category, int offset, int count) {
		return getCurrentSession().createQuery( "from Item where category.id=?" ).setInteger( 0, category )
				.setFirstResult( offset ).setMaxResults( count ).list();
	}

	@Override
	public int listByCategoryCount(int category) {
		return ( (Long) getCurrentSession().createQuery( "select count(*) from Item where category.id=?" )
				.setInteger( 0, category ).uniqueResult() ).intValue();
	}
}
