package org.xzc.sshb.dao;

import java.util.Date;
import java.util.List;

import org.xzc.sshb.domain.Item;
import org.xzc.sshb.value.RankItemValue;

/**
 * 商品
 * @author xzchaoo
 * 
 */
public interface IItemDao extends IBaseDao<Item> {

	/**
	 * 根据name关键字查找
	 * 
	 * @param name
	 * @return
	 */
	List<Item> listByNameKeyword(String name);

	/**
	 * 根据分类name关键字查找,分页
	 * @param category 如果为0表示所有类型
	 * @param name
	 * @param offset
	 * @param count
	 * @return
	 */
	List<Item> listByNameKeyword(int category, String name, int offset, int count);

	int listByNameKeywordCount(int category ,String name);

	/**
	 * 觉得这样好像意义不大,至少目前没用
	 * 
	 * @param ids
	 * @return
	 */
	@Deprecated
	int count(List<Integer> ids);

	/**
	 * 列出从d到现在的销售量排行前count名
	 * 
	 * @param d
	 * @param count
	 * @return
	 */
	List<RankItemValue> listSaleRank(Date d, int count);

	/**
	 * 根据分类列出
	 * @param category
	 * @param offset
	 * @param count
	 * @return
	 */
	List<Item> listByCategory(int category, int offset, int count);

	/**
	 * 每个分类的数量
	 * @param category
	 * @return
	 */
	int listByCategoryCount(int category);

	/**
	 * 列出销量排行前count的商品
	 * @param i
	 * @return
	 */
	List<RankItemValue> listSaleRank(int count);

}
