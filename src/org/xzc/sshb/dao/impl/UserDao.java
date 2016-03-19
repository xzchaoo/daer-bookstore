package org.xzc.sshb.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.xzc.sshb.dao.IUserDao;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.value.HuaqianValue;

@Component
public class UserDao extends BaseDao<User> implements IUserDao {

	@Override
	public User get(String name) {
		return (User) getCurrentSession().createQuery( "from User where name=?" ).setString( 0, name ).uniqueResult();
	}

	@Override
	public boolean validate(User user) {
		return getCurrentSession().createCriteria( clazz ).add( Restrictions.eq( "name", user.getName() ) )
				.add( Restrictions.eq( "password", user.getPassword() ) ).uniqueResult() != null;
	}

	@Override
	public User get(String name, String password) {
		return (User) getCurrentSession().createQuery( "from User where name=? and password=?" ).setString( 0, name )
				.setString( 1, password ).uniqueResult();
	}

	// TODO 没法用HQL 只好用原生sql了
	@Override
	public List<HuaqianValue> listHuaqian(int count) {
		// String sql = "select u,sum(r.totalPrice) as totalp from User as u left join u.records as r group by u";
		// String sql="from User as u order by (select sum(totalPrice) from u.records) desc";
		String sql = "select u.id,sum(r.totalPrice) as total from user as u left join record as r on r.user_id=u.id group by u.id order by total desc";
		SQLQuery sq = getCurrentSession().createSQLQuery( sql );
		sq.setMaxResults( count );
		List<Object[]> list = sq.list();
		List<HuaqianValue> ret = new ArrayList<HuaqianValue>();
		for (Object[] os : list) {
			HuaqianValue v = new HuaqianValue();
			v.user = get( (Integer) os[0] );
			v.total = (Double) os[1];
			ret.add( v );
		}
		return ret;
		// Query q = getCurrentSession().createQuery( sql ).setMaxResults( count );
		// List<Object[]> list = q.list();
		// return ( q ).list();
	}

}
