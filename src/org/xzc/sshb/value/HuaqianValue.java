package org.xzc.sshb.value;

import java.io.Serializable;

import org.xzc.sshb.domain.User;

public class HuaqianValue implements Serializable {
	public User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double total;
}
