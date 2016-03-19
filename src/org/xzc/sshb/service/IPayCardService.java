package org.xzc.sshb.service;

import java.util.List;

import org.xzc.sshb.domain.PayCard;
import org.xzc.sshb.domain.User;

public interface IPayCardService {
	/**
	 * 添加一张充值卡
	 * 
	 * @param pc
	 */
	public void add(PayCard pc);

	/**
	 * 随机生成一张充值卡,只填写了key和password,value=0,used=false
	 * 
	 * @return
	 */
	public PayCard generatePayCard();

	/**
	 * 使用pc为u充值
	 * 
	 * @param pc
	 * @param u
	 */
	public void charge(PayCard pc, User u);

	/**
	 * 列出所有可用的充值卡
	 * 
	 * @return
	 */
	public List<PayCard> listAvailablePayCards();

	/**
	 * 根据key和password获得充值卡
	 * @param key
	 * @param password
	 * @return
	 */
	public PayCard getCharge(String key, String password);
	
	/**
	 * 返回当前有多少可用的充值卡
	 * @return
	 */
	public int listAvailablePayCardsCount();
}
