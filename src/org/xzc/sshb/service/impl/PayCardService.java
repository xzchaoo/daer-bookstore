package org.xzc.sshb.service.impl;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xzc.sshb.dao.IPayCardDao;
import org.xzc.sshb.dao.IUserDao;
import org.xzc.sshb.domain.PayCard;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.service.IPayCardService;


@Service
public class PayCardService implements IPayCardService {

	private IPayCardDao payCardDao;
	private IUserDao userDao;

	@Override
	public void add(PayCard pc) {
		payCardDao.add( pc );
	}

	@Override
	public void charge(PayCard pc, User u) {
		// TODO 这里没有做额外的检查哦..
		u = userDao.get( u.getId() );
		u.setMoney( u.getMoney() + pc.getValue() );
		pc.setUsed( true );

		userDao.update( u );
		payCardDao.update( pc );
	}

	@Override
	public PayCard generatePayCard() {
		// 这里使用UUID
		PayCard pc = new PayCard();
		pc.setKey( UUID.randomUUID().toString().replace( "-", "" ) );
		pc.setPassword( UUID.randomUUID().toString().replace( "-", "" ) );
		return pc;
	}

	@Override
	public PayCard getCharge(String key, String password) {
		return payCardDao.get( key, password );
	}

	@Override
	public List<PayCard> listAvailablePayCards() {
		return payCardDao.listAvailablePayCards();
	}

	@Override
	public int listAvailablePayCardsCount() {
		return payCardDao.listAvailablePayCardsCount();
	}

	@Resource
	public void setPayCardDao(IPayCardDao payCardDao) {
		this.payCardDao = payCardDao;
	}

	@Resource
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

}
