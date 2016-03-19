package org.xzc.sshb.service;

import java.util.List;
import java.util.Map;

import org.xzc.sshb.domain.Record;
import org.xzc.sshb.domain.State;
import org.xzc.sshb.domain.User;

public interface IRecordService extends IBaseService<Record>{

	void markAsDeleted(Record record);

	List<Record> getByUser(User user);

	List<Record> getByUser(User user, int offset, int count);

	int getByUserCount(User user);

	List<Record> getByUserByState(User user, State state);

	List<Record> getByUserByState(User user, State state, int offset, int count);

	int getByUserByStateCount(User user, State state);

	List<Record> getByUserByNotState(User user, State state);

	List<Record> getByUserByNotState(User user, State state, int offset, int count);

	int getByUserByNotStateCount(User user, State state);

	List<Record> getByUserOrderBy(User user, String orderby);

	List<Record> getByUserOrderBy(User user, String orderby, int offset, int count);

	List<Record> getByUserByStateOrderBy(User user, State state, String orderby);

	List<Record> getByUserByStateOrderBy(User user, State state, String orderby, int offset,
			int count);

	List<Record> getByUserByNotStateOrderBy(User user, State state, String orderby);

	List<Record> getByUserByNotStateOrderBy(User user, State state, String orderby, int offset,
			int count);

	void receive(Record record);

	void cancel(Record record);

	/**
	 * 马上发货, FOR DEBUG
	 */
	void sendRightNow();

	/**
	 * 通过ajax方式删除一个record,尽量不抛出异常 而是处理错误 jsonResult的格式:{ success:false, msg:"你不能修改别人的订单" }
	 * 
	 * @param record
	 * @param jsonResult
	 */
	void deleteRecordAjax(Record record, Map<String, Object> jsonResult);

}
