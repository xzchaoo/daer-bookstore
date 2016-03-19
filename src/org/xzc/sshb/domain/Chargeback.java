package org.xzc.sshb.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 表示一次退单请求
 * 
 * @author xzchaoo
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Chargeback implements Serializable {
	/**
	 * 与Record主键关联的id
	 */
	private int id;
	/**
	 * 关联的record
	 */
	private Record record;
	/**
	 * 退单理由
	 */
	private String reason;
	/**
	 * 时间
	 */
	private Date time;
	/**
	 * 状态
	 */
	private State state;

	@Id
	@GenericGenerator(name = "idGenerator", strategy = "foreign", parameters = { @Parameter(name = "property", value = "record") })
	@GeneratedValue(generator = "idGenerator")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@ManyToOne
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
		Chargeback other = (Chargeback) obj;
		if (getId() != other.getId())
			return false;
		return true;
	}
}
