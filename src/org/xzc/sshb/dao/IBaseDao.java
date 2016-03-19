package org.xzc.sshb.dao;

import java.util.List;

/**
 * Dao的基类用于提供一些基本的方法
 * 
 * @author xzchaoo
 * 
 * @param <T>
 */
public interface IBaseDao<T> {
	/**
	 * 添加
	 * 
	 * @param t
	 */
	void add(T t);

	/**
	 * 删除,慎用!
	 * 
	 * @param t
	 */
	void delete(T t);

	/**
	 * 用于更新
	 * 
	 * @param t
	 */
	void update(T t);

	/**
	 * 根据id获得
	 * 
	 * @param id
	 * @return
	 */
	T get(int id);

	/**
	 * 根据一堆id进行获得,必须保证有id这一属性
	 * 
	 * @param ids
	 * @return
	 */
	List<T> get(List<Integer> ids);
	
	/**
	 * 列表
	 * @return
	 */
	List<T> list();

	/**
	 * 列表,分页
	 * @param offset
	 * @param count
	 * @return
	 */
	List<T> list(int offset, int count);

	/**
	 * 列表,数量
	 * @return
	 */
	int listCount();

	/**
	 * 列表,排序
	 * @param orderby
	 * @return
	 */
	List<T> listOrderBy(String orderby);

	/**
	 * 列表,排序,分页
	 * @param orderby
	 * @param offset
	 * @param count
	 * @return
	 */
	List<T> listOrderBy(String orderby, int offset, int count);
}
