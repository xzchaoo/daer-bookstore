package org.xzc.sshb.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class NameAction extends ActionSupport {
	public String execute() throws Exception {
		return ActionContext.getContext().getName();
	}
}
