package org.xzc.sshb.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PayCard implements Serializable {
	private String key;
	private String password;
	private boolean used;
	private double value;

	@Id
	@Column(name = "_key",length=32)
	public String getKey() {
		return key;
	}
	@Column(length=32)
	public String getPassword() {
		return password;
	}

	@Column(name = "_value")
	public double getValue() {
		return value;
	}

	public boolean isUsed() {
		return used;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
