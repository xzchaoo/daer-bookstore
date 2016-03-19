package org.xzc.sshb.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xzc.sshb.dao.IStateDao;
import org.xzc.sshb.domain.Item;
import org.xzc.sshb.domain.Record;
import org.xzc.sshb.domain.State;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.service.IStateService;

@Service
public class StateService implements IStateService {

	private IStateDao stateDao;

	@Override
	public State get(int id) {
		return stateDao.get( id );
	}

	public State getByClass(Class<?> clazz) {
		if (clazz == User.class)
			return this.get( State.USER_NORMAL );
		if (clazz == Item.class)
			return this.get( State.ITEM_NORMAL );
		if (clazz == Record.class)
			return this.get( State.RECORD_PAID );
		return null;
	}

	@Resource
	public void setStateDao(IStateDao stateDao) {
		this.stateDao = stateDao;
	}
}
