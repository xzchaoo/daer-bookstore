package org.xzc.sshb.interceptor;

import org.xzc.sshb.constant.Constants;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.service.IUserService;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 管理员拦截器
 * 
 * @author xzchaoo
 * 
 */
public class AdministratorInterceptor extends AbstractInterceptor {
	private IUserService userService;

	public String intercept(ActionInvocation invocation) throws Exception {
		User user = (User) userService.getCurrentUser();
		if (user != null && user.isAdmin()) {
			return invocation.invoke();
		}
		// 没有权限
		return Constants.IS_NOT_ADMIN;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
}
