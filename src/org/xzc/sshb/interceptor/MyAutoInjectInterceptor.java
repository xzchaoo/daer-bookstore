package org.xzc.sshb.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.xzc.sshb.constant.Constants;
import org.xzc.sshb.domain.State;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.service.ICartService;
import org.xzc.sshb.service.IUserService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

/**
 * 统一注入到ai变量里 logined 表示是否已经登陆了 即 user!=null user 当前用户(在可以为空) isAdmin 是否是管理员 ==
 * logined?user.isAdmin():false name 名字 未登录的话就为 游客 ? 历史浏览页面 打算做一个可以记录用户最近浏览过的若干个页面的功能 然后让用户可以比较方便的后退
 * 顺便注入一下用户的喜好设置
 */
public class MyAutoInjectInterceptor extends AbstractInterceptor {

	// TODO 需要处理chain的问题
	private final class MyAutoInjectPreResultListener implements PreResultListener {
		public void beforeResult(ActionInvocation invocation, String resultCode) {
			ActionContext ac = invocation.getInvocationContext();
			Map<String, Object> m = new HashMap<String, Object>();
			
			User user = userService.getCurrentUser();
			m.put( "user", user );
			boolean logined = user != null;
			m.put( "logined", logined );
			m.put( "admin", logined ? user.isAdmin() : false );
			m.put( "name", logined ? user.getName() : "游客" );
			m.put( "state", State.DESCRIPTION );
			m.put( "pre", ac.getSession().get( Constants.PREFERENCE ) );
			ac.put( "ai", m );
			
			Object a = invocation.getAction();

			// 处理错误问题
			if (a instanceof ValidationAware && (resultCode.endsWith( "Input" )||resultCode.equals( "input" ))) {
				ValidationAware va = (ValidationAware) a;
					try {
						String fieldErrorsString = JSONUtil.serialize( va.getFieldErrors() );
						String actionErrorsString = JSONUtil.serialize( va.getActionErrors() );
						m.put( "fieldErrorsString", fieldErrorsString );
						m.put( "actionErrorsString", actionErrorsString );
					} catch (JSONException e) {
					}
			}

		}
	}

	private ICartService cartService;
	private MyAutoInjectPreResultListener listener = new MyAutoInjectPreResultListener();
	private IUserService userService;
	
	public String intercept(ActionInvocation invocation) throws Exception {
		// 先更新一下用户
		if (userService.getCurrentUser() != null){
			userService.updateAndGetCurrentUser();
			//TODO
			//cartService.update();
		}
		invocation.addPreResultListener( listener );
		return invocation.invoke();
	}

	public void setCartService(ICartService cartService) {
		this.cartService = cartService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	

}
