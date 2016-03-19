package org.xzc.sshb.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 表示一个订单
 * 
 * @author xzchaoo
 * 
 */
public class Record implements Serializable {
	/**
	 * 订单id
	 */
	private int id;
	/**
	 * 下单的用户
	 */
	private User user;
	/**
	 * 一个订单里的订单项
	 */
	private Set<RecordItem> recordItems;
	/**
	 * 总价
	 */
	private double totalPrice;
	/**
	 * 下单时间
	 */
	private Date buyTime;
	/**
	 * 物品总量
	 */
	private int itemCount;
	/**
	 * 已评价的物品总量
	 */
	private int evaluatedCount;
	/**
	 * 订单状态
	 */
	private State state;

	/**
	 * 用于描述该订单,可能用于取消订单时的理由
	 */
	private String description;

	private Chargeback chargeback;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<RecordItem> getRecordItems() {
		return recordItems;
	}

	public void setRecordItems(Set<RecordItem> recordItems) {
		this.recordItems = recordItems;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}

	public boolean isEvaluated() {
		return itemCount == evaluatedCount;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getEvaluatedCount() {
		return evaluatedCount;
	}

	public void setEvaluatedCount(int evaluatedCount) {
		this.evaluatedCount = evaluatedCount;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	/**
	 * 虚拟属性,表示该订单是否已经评价完毕
	 */
	public boolean isFinished() {
		return this.evaluatedCount == this.itemCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Record other = (Record) obj;
		if (getId() != other.getId())
			return false;
		return true;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Chargeback getChargeback() {
		return chargeback;
	}

	public void setChargeback(Chargeback chargeback) {
		this.chargeback = chargeback;
	}
}