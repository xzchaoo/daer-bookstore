package org.xzc.sshb.dao;

import java.util.List;

import org.xzc.sshb.domain.Item;
import org.xzc.sshb.domain.RecordItem;
import org.xzc.sshb.domain.State;

/**
 * 订单中的商品项
 * @author xzchaoo
 *
 */
public interface IRecordItemDao extends IBaseDao<RecordItem> {
	/**
	 * 获得获得关于某个item的RecordItem
	 * 
	 * @param id
	 * @param offset
	 * @param count
	 * @return
	 */
	List<RecordItem> listByItem(Item item);

	/**
	 * 获得获得关于某个item的RecordItem,分页
	 * 
	 * @param id
	 * @param offset
	 * @param count
	 * @return
	 */
	List<RecordItem> listByItem(Item item, int offset, int count);

	/**
	 * 列出跟这个item相关的recorditem 并且recorditem的状态是state
	 * 
	 * @param item
	 * @param state
	 * @return
	 */
	List<RecordItem> listByItemByState(Item item, State state);

	/**
	 * 列出跟这个item相关的recorditem 并且recorditem的状态是state,分页
	 * 
	 * @param item
	 * @param state
	 * @param offset
	 * @param count
	 * @return
	 */
	List<RecordItem> listByItemByState(Item item, State state, int offset, int count);

	/**
	 * 列出跟这个item相关的recorditem 并且recorditem的状态是state 数量
	 * 
	 * @param item
	 * @param state
	 * @return
	 */
	int listByItemByStateCount(Item item, State state);

	List<RecordItem> listByItemByStateOrderBy(Item item, int state, int offset, int count,
			String orderBy);

	/**
	 * 其他方法都尽量扔到这个方法来处理,然后对外提供少量几个常用接口 item不为空,state不为空 若为空扔空指针异常 offset和count同时为-1的话表示查询全部,而不是分页
	 * orderBy的内容形如 id asc, name desc 如果你在SQL语句里使用了 表的别名的话 记得处理这个问题 比如这样是不好的: select u.name from
	 * User as u order by id desc
	 * 
	 * @param item
	 * @param state
	 * @param offset
	 * @param count
	 * @param orderBy
	 * @return
	 */
	List<RecordItem> listByItemByStateOrderBy(Item item, State state, int offset, int count,
			String orderBy);

	/**
	 * 获得获得关于某个item的RecordItem数量
	 * 
	 * @param id
	 * @return
	 */
	int listByItemCount(Item item);

	/**
	 * 列出商品,分页,排序
	 * @param item
	 * @param offset
	 * @param count
	 * @param orderBy
	 * @return
	 */
	List<RecordItem> listByItemOrderBy(Item item, int offset, int count, String orderBy);

	List<RecordItem> listByItemOrderBy(Item item, String orderBy);

}
