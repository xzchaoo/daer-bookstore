package org.xzc.sshb.interceptor;

import org.xzc.sshb.constant.Constants;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.service.IUserService;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 登陆拦截器
 * 
 * @author xzchaoo
 * 
 */
public class LoginInterceptor extends AbstractInterceptor {
	private IUserService userService;

	public String intercept(ActionInvocation ai) throws Exception {
		User user = (User) userService.getCurrentUser();
		if (user == null) {
			return Constants.SHOULD_LOGIN;
		}
		return ai.invoke();
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

}
