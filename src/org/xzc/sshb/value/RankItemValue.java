package org.xzc.sshb.value;

import java.io.Serializable;

import org.xzc.sshb.domain.Item;

/**
 * 排行榜的数据,商品和数量
 * @author xzchaoo
 *
 */
public final class RankItemValue implements Serializable {
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
