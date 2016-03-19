package org.xzc.sshb.service;

import java.util.List;
import java.util.Map;

import org.xzc.sshb.action.CartAction.ShowCartItemValue;
import org.xzc.sshb.domain.Cart;
import org.xzc.sshb.domain.Cart.CartItem;
import org.xzc.sshb.domain.Item;

public interface ICartService {
	/**
	 * 添加一件商品到购物车
	 * 
	 * @param item
	 */
	void add(Item item);

	/**
	 * coutn件商品到购物车
	 * 
	 * @param item
	 * @param count
	 */
	void add(Item item, int count);

	/**
	 * 将数量设置为quantity,为0或负数表示产出
	 * 
	 * @param item
	 * @param quantity
	 */
	void set(Item item, int quantity);

	/**
	 * 根据商品id获得购物车商品项
	 * 
	 * @param id
	 * @return
	 */
	CartItem getById(int id);

	/**
	 * 根据一堆商品id获得一堆购物车商品项
	 * 
	 * @param ids
	 * @return
	 */
	List<CartItem> getByIds(List<Integer> ids);

	/**
	 * 清空购物车
	 */
	void clear();

	/**
	 * 购物车总价
	 * @return
	 */
	double getTotalPrice();

	/**
	 * 获得购物车对象
	 * @return
	 */
	Cart getCart();

	/**
	 * TODO感觉名字要改一下 用途是将ids对应的数量设置为quantities
	 * 
	 * @param ids
	 * @param quantities
	 * @param add
	 * @param jsonResult
	 */
	void addToCartAjax(List<Integer> ids, List<Integer> quantities, boolean add, Map<String, Object> jsonResult);

	/**
	 * 将对应的item的数量设置为quantity
	 * 
	 * @param itemId
	 * @param quantity
	 */
	void set(int itemId, int quantity);

	/**
	 * 刷新购物车,从数据库里拉来新的数据
	 */
	void update();

	/**
	 * TODO
	 * @param ids
	 * @param jsonResult
	 */
	void doCheckoutAjax(List<Integer> ids, Map<String, Object> jsonResult);
	
	/**
	 * 列出购物车商品项
	 * @return
	 */
	List<CartItem> list();

}
