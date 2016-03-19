package org.xzc.sshb.dao;

import java.util.List;

import org.xzc.sshb.domain.User;
import org.xzc.sshb.value.HuaqianValue;

/**
 * 这个类用到的密码 都是加密过后的
 * 
 * @author xzchaoo
 * 
 */
public interface IUserDao extends IBaseDao<User> {
	/**
	 * 根据用户名来获取
	 * 
	 * @param name
	 * @return
	 */
	User get(String name);

	/**
	 * 根据账号和密码获取
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	User get(String name, String password);

	/**
	 * 检查用户的账号和密码是否对应
	 * 
	 * @param user
	 *            要检查的user
	 * @return 检查用户的账号和密码是否对应
	 */
	@Deprecated
	boolean validate(User user);

	/**
	 * TODO
	 * @param count
	 * @return
	 */
	List<HuaqianValue> listHuaqian(int count);

}