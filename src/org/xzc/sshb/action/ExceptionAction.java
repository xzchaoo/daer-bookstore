package org.xzc.sshb.action;

import org.xzc.sshb.constant.Constants;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ExceptionAction extends ActionSupport {

	public String exceptionUI() throws Exception {
		return Constants.EXCEPTION_ACTION_EXCEPTION_UI;
	}

	public String toExceptionUI() throws Exception {
		return Constants.EXCEPTION_ACTION_TO_EXCEPTION_UI;
	}
}
