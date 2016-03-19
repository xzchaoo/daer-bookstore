package org.xzc.sshb.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xzc.sshb.constant.Constants;
import org.xzc.sshb.dao.IStateDao;
import org.xzc.sshb.dao.IUserDao;
import org.xzc.sshb.domain.State;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.service.IUserService;
import org.xzc.sshb.value.HuaqianValue;

import com.opensymphony.xwork2.ActionContext;

/**
 * 这里涉及到的密码都是已经加密的
 * 
 * @author xzchaoo
 * 
 */
@Service
public class UserService extends BaseService<User> implements IUserService {
	private IUserDao userDao;
	private IStateDao stateDao;

	@Override
	public User get(String name) {
		return userDao.get( name );
	}

	@Override
	public User getCurrentUser() {
		return (User) ActionContext.getContext().getSession().get( Constants.SESSION_LOGINED_USER_KEY );
	}

	@Override
	public boolean isNameExists(String name) {
		return userDao.get( name ) != null;
	}

	@Override
	public void markAsDeleted(User user) {
		user = userDao.get( user.getId() );
		user.setState( stateDao.get( State.USER_DELETED ) );
		userDao.update( user );
	}

	@Override
	public void setCurrentUser(User user) {
		ActionContext.getContext().getSession().put( Constants.SESSION_LOGINED_USER_KEY, user );
	}

	@Resource
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
		this.baseDao = userDao;
	}

	@Override
	public User updateAndGetCurrentUser() {
		User u = getCurrentUser();
		u = userDao.get( u.getId() );
		setCurrentUser( u );
		return u;
	}

	/**
	 * 验证用户密码
	 */
	@Override
	public boolean validate(User user) {
		return userDao.validate( user );
	}

	// TODO;
	@Override
	public void changePassword(User user) {
		User u = this.get( user.getId() );
		u.setPassword( user.getPassword() );
	}

	@Resource
	public void setStateDao(IStateDao stateDao) {
		this.stateDao = stateDao;
	}

	@Override
	public User get(String name, String password) {
		return userDao.get( name, password );
	}

	@Override
	public List<HuaqianValue> listHuaqian(int count) {
		return userDao.listHuaqian(count);
	}

}