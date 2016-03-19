package org.xzc.sshb.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xzc.sshb.constant.Constants;
import org.xzc.sshb.dao.IItemDao;
import org.xzc.sshb.dao.IRecordDao;
import org.xzc.sshb.dao.IRecordItemDao;
import org.xzc.sshb.dao.IStateDao;
import org.xzc.sshb.domain.Cart;
import org.xzc.sshb.domain.Cart.CartItem;
import org.xzc.sshb.domain.Item;
import org.xzc.sshb.domain.Record;
import org.xzc.sshb.domain.RecordItem;
import org.xzc.sshb.domain.State;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.service.ICartService;
import org.xzc.sshb.service.IItemService;
import org.xzc.sshb.service.IStateService;
import org.xzc.sshb.service.IUserService;

import com.opensymphony.xwork2.ActionContext;

@Service
public class CartService implements ICartService {
	/**
	 * 找出item在cartItemList中的索引靠的是比对id
	 * 
	 * @param item
	 *            要寻找的item
	 * @param cartItemList
	 *            购物车的itemList
	 * @return 返回item在itemList中的索引 不存在就返回-1
	 */
	private static final int find(int itemId, List<CartItem> cartItemList) {
		for (int i = 0; i < cartItemList.size(); ++i)
			if (cartItemList.get( i ).item.getId() == itemId)
				return i;
		return -1;
	}

	private static final int find(Item item, List<CartItem> cartItemList) {
		return find( item.getId(), cartItemList );
	}

	private IItemDao itemDao;
	private IItemService itemService;
	private IRecordDao recordDao;
	private IRecordItemDao recordItemDao;
	private IStateDao stateDao;
	private IUserService userService;

	public void add(int id, int quantity) {
		add( itemService.get( id ), quantity );
	}

	/**
	 * 将一件物品加入购物车(数量+1),此时的数量并不考虑是否超过物品剩余量,相当于add(item,1)
	 * 
	 * @param item
	 *            要加入购物车的物品
	 */
	public void add(Item item) {
		add( item, 1 );
	}

	/**
	 * @param item
	 *            要加入购物车的物品,此时的数量并不考虑是否超过物品剩余量
	 * @param quantity
	 *            要加入的数量,可以是负数 表示扣除,如果是0则直接返回
	 */
	public void add(Item item, int quantity) {
		item = itemService.get( item.getId() );
		List<CartItem> cartItemList = getCart().getCartItemList();
		int index = find( item.getId(), cartItemList );
		if (index != -1) {
			CartItem ci = cartItemList.get( index );
			ci.quantity += quantity;
			if (ci.quantity <= 0) {
				cartItemList.remove( index );
			}
			return;
		}
		// index==-1表明item不在购物车里
		// 如果quantity>0 那么就进行添加
		if (quantity <= 0)
			return;
		CartItem ci = new CartItem();
		ci.item = item;
		ci.quantity = quantity;
		cartItemList.add( ci );
	}

	@Override
	public void addToCartAjax(List<Integer> ids, List<Integer> quantities, boolean add, Map<String, Object> jsonResult) {

		// 简单的检查一下参数
		if (ids == null || quantities == null || ids.size() != quantities.size()) {
			jsonResult.put( "success", false );
			jsonResult.put( "msg", "参数错误" );
			return;
		}

		for (int i = 0; i < ids.size(); ++i) {
			int id = ids.get( i );
			int quantity = quantities.get( i );
			if (add)
				add( id, quantity );
			else
				set( id, quantity );
		}
		jsonResult.put( "success", true );
		jsonResult.put( "msg", "成功" );

	}

	/**
	 * 清空购物车
	 */
	public void clear() {
		getCart().getCartItemList().clear();
	}

	@Override
	public void doCheckoutAjax(List<Integer> ids, Map<String, Object> jsonResult) {

		List<CartItem> cartItems = getByIds( ids );
		if (cartItems.size() != ids.size()) {
			jsonResult.put( "success", false );
			jsonResult.put( "msg", "含有无效的商品id" );
			return;
		}

		// // 检查 剩余数量 和 总价格

		double totalPrice = checkItems( cartItems, jsonResult );
		if (totalPrice == -1) {
			return;
		}

		User cu = userService.getCurrentUser();
		if (cu.getMoney() < totalPrice) {
			jsonResult.put( "success", false );
			jsonResult.put( "msg", "余额不足" );
			return;
		}

		int recordId = doCheckoutAjax( cartItems, totalPrice );
		jsonResult.put( "recordId", recordId );
		jsonResult.put( "success", true );
		jsonResult.put( "msg", "结账成功" );
	}

	@Override
	public CartItem getById(int id) {
		// TODO 性能有待提高
		Cart cart = getCart();
		for (CartItem ci : cart.getCartItemList())
			if (ci.getItem().getId() == id)
				return ci;
		return null;
	}

	@Override
	public List<CartItem> getByIds(List<Integer> ids) {
		// TODO 性能有待提高
		List<CartItem> list = new ArrayList<CartItem>();
		for (int id : ids) {
			CartItem ci = getById( id );
			if (ci != null)
				list.add( ci );
		}
		return list;
	}

	/**
	 * 获取购物车 如果不存在就创建一个
	 * 
	 * @return 购物车
	 */
	public Cart getCart() {
		Cart cart = (Cart) ActionContext.getContext().getSession().get( Constants.SESSION_CART_KEY );
		if (cart == null) {
			cart = new Cart();
			setCart( cart );
		}
		return cart;
	}

	/**
	 * 计算总价,计算的时候会询问立即询问每个商品的价格,然后乘上数量,算出总价 如果发生了任何错误(比如商品被删除了!)则抛出异常
	 * 
	 * @return 返回总价
	 */
	public double getTotalPrice() {
		return getCart().getTotalPrice();
	}

	@Override
	public void set(int itemId, int quantity) {
		set( itemService.get( itemId ), quantity );
	}

	/**
	 * 设置购物车中item的数量为quantity
	 */
	@Override
	public void set(Item item, int quantity) {
		List<CartItem> cartItemList = getCart().getCartItemList();
		int index = find( item.getId(), cartItemList );
		// 如果商品之前不存在 就认为是加入购物车的动作
		if (index == -1) {
			// 忽略数量<=0的商品 一般操作正常都不会出现这个情况
			if (quantity <= 0)
				return;
			// 存入一个新的item
			CartItem ci = new CartItem();
			ci.item = item;
			ci.quantity = quantity;
			cartItemList.add( ci );
		} else {
			// 否则认为是对商品数量的修改
			// <=0认为是删除该商品
			if (quantity <= 0)
				cartItemList.remove( index );
			else
				cartItemList.get( index ).setQuantity( quantity );
		}
	}

	/**
	 * 设置购物车
	 * 
	 * @param cart
	 *            购物车
	 */
	public void setCart(Cart cart) {
		ActionContext.getContext().getSession().put( Constants.SESSION_CART_KEY, cart );
	}

	@Resource
	public void setItemDao(IItemDao itemDao) {
		this.itemDao = itemDao;
	}

	@Resource
	public void setItemService(IItemService itemService) {
		this.itemService = itemService;
	}

	@Resource
	public void setRecordDao(IRecordDao recordDao) {
		this.recordDao = recordDao;
	}

	@Resource
	public void setRecordItemDao(IRecordItemDao recordItemDao) {
		this.recordItemDao = recordItemDao;
	}

	@Resource
	public void setStateDao(IStateDao stateDao) {
		this.stateDao = stateDao;
	}

	@Resource
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	// TODO
	@Override
	public void update() {
		Cart cart = getCart();
	}

	/**
	 * 如果正常则返回总价, 如果某商品剩余不足则在jsonResult中第一个剩余不足的商品信息
	 * 
	 * @param cartItems
	 * @param jsonResult
	 * @return
	 */
	private double checkItems(List<CartItem> cartItems, Map<String, Object> jsonResult) {
		double sum = 0;
		for (CartItem ci : cartItems) {
			Item i = itemDao.get( ci.item.getId() );
			if (ci.quantity > i.getRemain()) {
				jsonResult.put( "success", false );
				jsonResult.put( "msg", i.getName() + "商品不足" );
				return -1;
			}
			sum += ci.quantity * i.getPrice();
		}
		return sum;
	}

	/**
	 * 必须保证数量充足 余额充足
	 * 
	 * @param checkoutItemList
	 * @param totalPrice
	 * @return
	 */
	private int doCheckoutAjax(List<CartItem> checkoutItemList, double totalPrice) {
		User user = userService.getCurrentUser();

		// 新的订单
		Record r = new Record();
		r.setUser( user );
		r.setTotalPrice( totalPrice );
		r.setBuyTime( new Date() );
		r.setEvaluatedCount( 0 );
		r.setItemCount( checkoutItemList.size() );
		r.setState( stateDao.get( State.RECORD_PAID ) );
		recordDao.add( r );

		State s = stateDao.get( State.RECORDITEM_NOT_RECEIVED );
		for (CartItem ci : checkoutItemList) {
			RecordItem ri = new RecordItem();
			Item i = ci.getItem();
			i = itemService.get( i.getId() );
			i.setRemain( i.getRemain() - ci.quantity );
			ri.setItem( i );
			ri.setQuantity( ci.quantity );
			ri.setRecord( r );
			ri.setState( s );
			recordItemDao.add( ri );
		}
		user.setMoney( user.getMoney() - r.getTotalPrice() );
		getCart().getCartItemList().removeAll( checkoutItemList );
		return r.getId();
	}

	@Override
	public List<CartItem> list() {
		return getCart().getCartItemList();
	}

}
