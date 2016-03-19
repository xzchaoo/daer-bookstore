package org.xzc.sshb.domain;

import java.io.Serializable;
import java.util.Date;

public class RecordItem implements Serializable {
	private int id;
	private Record record;
	private Item item;
	private int quantity;
	private String evaluation;
	private int star;
	private Date evaluationTime;
	private State state;
	
	public int getId() {
		return id;
	}

	public RecordItem(Record record, Item item, int quantity) {
		this.record = record;
		this.item = item;
		this.quantity = quantity;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Record getRecord() {
		return record;
	}

	public RecordItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setRecord(Record record) {
		this.record = record;
	}

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

	public String getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public Date getEvaluationTime() {
		return evaluationTime;
	}

	public void setEvaluationTime(Date evaluationTime) {
		this.evaluationTime = evaluationTime;
	}

	@Override
	public String toString() {
		return "RecordItem [id=" + id + ", record=" + record + ", item=" + item + ", quantity=" + quantity + ", evaluation=" + evaluation + ", star="
				+ star + ", evaluationTime=" + evaluationTime + "]";
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
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
		RecordItem other = (RecordItem) obj;
		if (getId() != other.getId())
			return false;
		return true;
	}
}
