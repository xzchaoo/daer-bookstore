package org.xzc.sshb.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.xzc.sshb.dao.IPayCardDao;
import org.xzc.sshb.domain.PayCard;

/**
 * 暂时没多写什么
 * 
 * @author xzchaoo
 * 
 */
@Component
public class PayCardDao implements IPayCardDao {
	private SessionFactory sessionFactory;

	@Override
	public void add(PayCard pc) {
		getCurrentSession().save( pc );
	}

	@Override
	public void update(PayCard pc) {
		getCurrentSession().update( pc );
	}

	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<PayCard> listAvailablePayCards() {
		return getCurrentSession().createQuery( "from PayCard where used=false" ).list();
	}

	@Override
	public PayCard get(String key, String password) {
		return (PayCard) getCurrentSession().createQuery( "from PayCard where key=? and password=?" ).setString( 0, key )
				.setString( 1, password ).uniqueResult();
	}

	@Override
	public int listAvailablePayCardsCount() {
		return ((Long)getCurrentSession().createQuery( "select count(*) from PayCard where used=false" ).uniqueResult()).intValue();
	}

}