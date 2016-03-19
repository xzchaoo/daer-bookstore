package org.xzc.sshb.service;

import java.util.Date;
import java.util.List;

import org.xzc.sshb.domain.Record;
import org.xzc.sshb.domain.RecordItem;
import org.xzc.sshb.domain.User;

/**
 * 专注于各种数据的统计
 * 
 * @author xzchaoo
 * 
 */
public interface IStatisticsService {
	/**
	 * 列出user在beginTime到endTime之间的订单
	 * 
	 * @param user
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<Record> listRecord(User user, Date beginTime, Date endTime);

	/**
	 * 列出user在beginTime到endTime之间相关category类别的recorditem
	 * 
	 * @param user
	 * @param category
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<RecordItem> listRecordItem(User user, int category, Date beginTime, Date endTime);

	/**
	 * 列出这段时间的花费前count名
	 * @param user
	 * @param beginTime
	 * @param endTime
	 * @param count
	 * @return
	 */
	List<User> listUserByExpense(Date beginTime, Date endTime, int count);
}
