package org.xzc.sshb.service;

import java.util.List;

import org.xzc.sshb.action.ItemEvaluationValue;
import org.xzc.sshb.dao.IBaseDao;
import org.xzc.sshb.domain.Item;
import org.xzc.sshb.domain.RecordItem;
import org.xzc.sshb.value.ItemViewDetailSalesVolumeValue;

public interface IRecordItemService {
	/**
	 * 对ri所指定的项进行评价
	 * 
	 * @param ri
	 */
	void evaluate(RecordItem ri);

	/**
	 * 根据id获得
	 * 
	 * @param id
	 * @return
	 */
	RecordItem get(int id);
	
	/**
	 * 列出数量
	 * @param item
	 * @return
	 */
	int listByItemCount(Item item);

	/**
	 * 根据item获得相关的已经评论的RecordItem
	 */
	List<ItemEvaluationValue> listEvaluations(Item item);

	/**
	 * 根据item获得相关的已经评论的RecordItem,分页
	 */
	List<ItemEvaluationValue> listEvaluations(Item item, int offset, int count);

	/**
	 * 根据item获得相关的已经评论的RecordItem,注意这个数量跟listByItemCount不一定相等
	 */
	int listEvaluationsCount(Item item);

	/**
	 * 根据item获得销量情况,这个数量和listByItemCount一样,这里还是允许即使订单被删除了也是可以看到购买记录的吧?还是要考虑取消?
	 * 
	 * @param item
	 * @param offset
	 * @param count
	 * @return
	 */
	List<ItemViewDetailSalesVolumeValue> listSalesVolume(Item item, int offset, int count);

}
