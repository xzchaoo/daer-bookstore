package org.xzc.sshb.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
	public static final class CartItem implements Serializable {
		public Item item;
		public int quantity;

		public Item getItem() {
			return item;
		}

		public void setItem(Item item) {
			this.item = item;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
	}

	private List<CartItem> cartItemList = new ArrayList<CartItem>();

	public List<CartItem> getCartItemList() {
		return cartItemList;
	}

	public double getTotalPrice() {
		double money = 0;
		for (CartItem ci : cartItemList)
			money += ci.quantity * ci.item.getPrice();
		return money;
	}

	public void clear() {
		cartItemList.clear();
	}
}
