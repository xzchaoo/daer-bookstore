package org.xzc.sshb.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.xzc.sshb.dao.IStateDao;
import org.xzc.sshb.domain.State;

/**
 * 没什么好说的
 * 
 * @author xzchaoo
 * 
 */
@Component
public class StateDao implements IStateDao {
	protected SessionFactory sessionFactory;

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public State get(int id) {
		return (State) sessionFactory.getCurrentSession().get( State.class, id );
	}

}
