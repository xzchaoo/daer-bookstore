package org.xzc.sshb.action;

import java.util.ArrayList;
import java.util.List;

import org.xzc.sshb.domain.Cart.CartItem;

/**
 * 用于处理跟购物车有关的请求,而不是跟其他Action合并
 * 
 * @author xzchaoo
 * 
 */
public class CartAction extends MyBaseAction {
	/**
	 * 当需要显示购物车的时候用的传输对象
	 * 
	 * @author xzchaoo
	 * 
	 */
	public static final class ShowCartItemValue {
		public int id;
		public String imgPath;
		public String name;
		public double price;
		public int quantity;

		public int getId() {
			return id;
		}

		public String getImgPath() {
			return imgPath;
		}

		public String getName() {
			return name;
		}

		public double getPrice() {
			return price;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setId(int id) {
			this.id = id;
		}

		public void setImgPath(String imgPath) {
			this.imgPath = imgPath;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

	}

	/**
	 * 用于表示addToCartAjax操作是 修改(false) 还是 添加(true)
	 */
	private boolean add = false;

	/**
	 * 用于购物车数据
	 */
	private List<ShowCartItemValue> cartItemList;

	/**
	 * ids和quantitys 分别表示item的id和数量 ids[0]=1 counts[0]=10 表示id为1的物品10件
	 * 在doCheckoutAjax中ids表示前端选中了哪些物品要进行结账
	 */
	private List<Integer> ids;

	/**
	 * 表示要操作的物品对象 一般用于单次操作 比如将某物品数量+1(当你点击加入购物车的时候)
	 * 会与quantity和edit配套使用,edit为true表示将对象的数量直接设置为quantity,为false则quantity此时表示对数量进行加减
	 */

	private List<Integer> quantities;

	/**
	 ** ajax方式处理 "添加到购物车", "设置物品件数" "批量设置物品件数" 这三个请求 注意要更新购物车的item的数量 保证最新
	 * 考虑一下这里要不要处理"用户加入购物车的数量比库存还大"
	 * 
	 * @return
	 */
	public String addToCartAjax() {
		try {
			cartService.addToCartAjax( ids, quantities, add, jsonResult );
		} catch (Exception e) {
			jsonResult.put( "success", false );
			jsonResult.put( "msg", "未知原因" );
		}
		return SUCCESS;
	}

	/**
	 * ajax方式处理结账请求
	 * 
	 * @return
	 */
	public String doCheckoutAjax() {

		if (ids == null || ids.isEmpty()) {
			jsonResult.put( "success", false );
			jsonResult.put( "msg", "没有选中任何商品" );
			return SUCCESS;
		}

		cartService.doCheckoutAjax( ids, jsonResult );
		/*
		User cu = userService.getCurrentUser();
		boolean success = true;
		List<CartItem> checkoutItemList = null;
		checkoutItemList = cartService.getByIds( ids );
		if (checkoutItemList == null || checkoutItemList.isEmpty()) {
			jsonResult.put( "success", false );
			jsonResult.put( "msg", "你没有选中任何商品" );
			return SUCCESS;
		}
		double totalPrice = 0;
		for (CartItem ci : checkoutItemList)
			totalPrice += ci.getQuantity() * ci.getItem().getPrice();
		// ids是要进行结算的item_id
		if (cu.getMoney() < totalPrice) {
			// 不够支付
			jsonResult.put( "success", false );
			jsonResult.put( "msg", "余额不足" );
		} else {
			try {
				int recordId = cartService.doCheckout( checkoutItemList, totalPrice );
				jsonResult.put( "success", true );
				jsonResult.put( "msg", "购买成功" );
				jsonResult.put( "recordId", recordId );
			} catch (Exception e) {
				jsonResult.put( "success", false );
				jsonResult.put( "msg", e.getMessage() );
				LOG.info( e );
			}
		}
		*/
		return SUCCESS;
	}

	public List<ShowCartItemValue> getCartItemList() {
		return cartItemList;
	}

	/**
	 * 显示购物车数据 TODO 要不要按某些字段排序?
	 * 
	 * @return
	 */
	public String listUI() {
		cartItemList = new ArrayList<ShowCartItemValue>();

		List<CartItem> cis = cartService.list();
		for (CartItem ci : cis) {
			ShowCartItemValue i = new ShowCartItemValue();
			i.id = ci.item.getId();
			i.name = ci.item.getName();
			i.price = ci.item.getPrice();
			i.quantity = ci.quantity;
			i.imgPath = ci.item.getImgPath();
			cartItemList.add( i );
		}

		/*= new ArrayList<ShowCartItemValue>();
		List<CartItem> cis = cartService.getCart().getCartItemList();
		for (CartItem ci : cis) {
			ShowCartItemValue i = new ShowCartItemValue();
			i.id = ci.item.getId();
			i.name = ci.item.getName();
			i.price = ci.item.getPrice();
			i.quantity = ci.quantity;
			i.imgPath = ci.item.getImgPath();
			cartItemList.add( i );
		}*/
//		总价格

		return SUCCESS;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	/*
		@Deprecated
		public String removeItemAjax() {
			if (ids.isEmpty()) {
				jsonResult.put( "success", false );
				jsonResult.put( "msg", "id不可为空" );
			} else {
				for (Integer i : ids)
					if (i != null)
						cartService.delete( i );
				jsonResult.put( "success", true );
				jsonResult.put( "msg", "删除成功" );
			}
			return SUCCESS;
		}
	*/
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public void setQuantities(List<Integer> quantities) {
		this.quantities = quantities;
	}

	/**
	 * ids和quantities必须要暴露出来 否则struts2没办法自动生成...
	 * 
	 * @return
	 */
	public List<Integer> getIds() {
		return ids;
	}
	/**
	 * ids和quantities必须要暴露出来 否则struts2没办法自动生成...
	 * 
	 * @return
	 */
	public List<Integer> getQuantities() {
		return quantities;
	}

}
