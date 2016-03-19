package org.xzc.sshb.service;

import java.util.List;

import org.xzc.sshb.dao.IUserDao;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.value.HuaqianValue;

public interface IUserService extends IUserDao {
	
	/**
	 * 从session里获取获得当前登录了的用户
	 * 
	 * @return
	 */
	User getCurrentUser();

	/**
	 * 从数据库里取一遍,然后自动更新到session里
	 * 
	 * @return
	 */
	User updateAndGetCurrentUser();

	/**
	 * 用户登录成功之后,调用这个函数进行设置
	 * 
	 * @param user
	 */
	void setCurrentUser(User user);

	/**
	 * 判断名字存在
	 * 
	 * @param name
	 * @return
	 */
	boolean isNameExists(String name);

	/**
	 * 修改密码
	 * 
	 * @param user
	 */
	void changePassword(User user);

	/**
	 * 标记为删除
	 * 
	 * @param user
	 */
	void markAsDeleted(User user);

	/**
	 * TODO 列出花钱排行钱count的人
	 * @param i
	 * @return
	 */
	List<HuaqianValue> listHuaqian(int count);
}
