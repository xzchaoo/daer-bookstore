package org.xzc.sshb.value;

import java.util.Date;

/**
 * 销售记录的项 某用户在某时间买了多少件
 * @author xzchaoo
 *
 */
public final class ItemViewDetailSalesVolumeValue {
	public String username;
	public int quantity;
	public Date buyTime;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}
}
