package org.xzc.sshb.service;

import java.util.Date;
import java.util.List;

import org.xzc.sshb.domain.Item;
import org.xzc.sshb.value.RankItemValue;

public interface IItemService extends IBaseService<Item> {

	List<RankItemValue> getMonthRankItems();

	/**
	 * 更新每月排行榜信息
	 */
	void updateMonthRankItems();

	/**
	 * 上次更新排行榜的时间
	 * 
	 * @return
	 */
	Date getLastUpdateSaleRankDate();

	/**
	 * 根据category,keyword找item,为0表示所有类型
	 * 
	 * @param name
	 * @return
	 */
	List<Item> listByNameKeyword(String name);
	
	List<Item> listByNameKeyword(int category, String name, int offset, int count);

	int listByNameKeywordCount(int category, String name);

	/**
	 * 根据分类查找
	 * @param category
	 * @param offset
	 * @param count
	 * @return
	 */
	List<Item> listByCategory(int category, int offset, int count);

	/**
	 * 每个类别的数量
	 * @return
	 */
	int listByCategoryCount(int category);

	
	/**
	 * 列出销量前count的商品
	 * @param count
	 * @return
	 */
	List<RankItemValue> listBySaleVolume(int count);

}
