package org.xzc.sshb.dao;

import java.util.List;

import org.xzc.sshb.domain.PayCard;

/**
 * 充值卡
 * @author xzchaoo
 *
 */
public interface IPayCardDao {

	/**
	 * 添加一张充值卡
	 * @param pc
	 */
	void add(PayCard pc);

	/**
	 * 更新一张充值卡
	 * @param pc
	 */
	void update(PayCard pc);

	/**
	 * 列出所有可用的充值卡
	 * 
	 * @return
	 */
	List<PayCard> listAvailablePayCards();

	/**
	 * 根据卡号和密码返回充值卡,如果不存在就返回null
	 * 
	 * @param key
	 * @param password
	 * @return
	 */
	PayCard get(String key, String password);

	/**
	 * 列出有多少可用的充值卡
	 * 
	 * @return
	 */
	int listAvailablePayCardsCount();

}
