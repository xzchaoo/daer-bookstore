package org.xzc.sshb.dao;

import java.util.Date;
import java.util.List;

import org.xzc.sshb.domain.Record;
import org.xzc.sshb.domain.State;
import org.xzc.sshb.domain.User;

/**
 * 订单
 * 
 * @author xzchaoo
 * 
 */
public interface IRecordDao extends IBaseDao<Record> {

	/**
	 * 根据订单项的状态修复所有订单状态, 包括 订单项的数量(itemCount) 已评价的订单量(evaluatedCount) 总价格(totalPrice) 和状态(state),感觉写崩了啊
	 * 
	 * @return
	 */
	@Deprecated
	void repair();

	/**
	 * 将某个订单标记为删除,而不是真的删除
	 * @param record
	 */
	void markAsDeleted(Record record);

	/**
	 * 获取跟一个用户相关的订单
	 * @param user
	 * @return
	 */
	List<Record> getByUser(User user);

	/**
	 * 获取跟一个用户相关的订单,分页
	 * @param user
	 * @param offset
	 * @param count
	 * @return
	 */
	List<Record> getByUser(User user, int offset, int count);

	/**
	 * 获取跟一个用户相关的订单的数量
	 * @param user
	 * @return
	 */
	int getByUserCount(User user);

	/**
	 * 获取跟一个用户相关的,并且处于某状态的订单
	 * @param user
	 * @param state
	 * @return
	 */
	List<Record> getByUserByState(User user, State state);

	List<Record> getByUserByState(User user, State state, int offset, int count);

	int getByUserByStateCount(User user, State state);

	List<Record> getByUserByNotState(User user, State state);

	List<Record> getByUserByNotState(User user, State state, int offset, int count);

	int getByUserByNotStateCount(User user, State state);

	List<Record> getByUserOrderBy(User user, String orderby);

	List<Record> getByUserOrderBy(User user, String orderby, int offset, int count);

	List<Record> getByUserByStateOrderBy(User user, State state, String orderby);

	List<Record> getByUserByStateOrderBy(User user, State state, String orderby, int offset, int count);

	List<Record> getByUserByNotStateOrderBy(User user, State state, String orderby);

	List<Record> getByUserByNotStateOrderBy(User user, State state, String orderby, int offset, int count);

	List<Record> listByState(State state);
	
	/**
	 * 列出user在这段时间内的订单,如果user为null,则为全部用户,
	 * 如果beginTime为null,那么起始时间无限制
	 * 如果endTime为null,那么结束时间无限制
	 * @param user
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<Record> list(User user, Date beginTime, Date endTime);

}
