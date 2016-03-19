package org.xzc.sshb.action;

import org.xzc.sshb.constant.Constants;
import org.xzc.sshb.interceptor.LastURLOutputFlag;

import com.opensymphony.xwork2.ActionSupport;

public class NavIndexAction extends ActionSupport implements LastURLOutputFlag {
	private boolean toLastURL = false;
	private String lastURL;

	public String navIndexUI() {
		return SUCCESS;
	}

	public String toBack() {
		toLastURL = true;
		return SUCCESS;
	}

	public boolean isToLastURL() {
		return toLastURL;
	}

	public String getLastURL() {
		return lastURL;
	}

	public void setLastURL(String lastURL) {
		this.lastURL = lastURL;
	}

}
