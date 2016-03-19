package org.xzc.sshb.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

//Interceptor都是单例
//有比较大的问题
//如果之前来的操作是一个删除操作
//那么返回后可能又执行删除操作 有点危险
/**
 * 没写成,感觉用途也不多
 * @author xzchaoo
 *
 */
@Deprecated
public class LastURLInterceptor extends AbstractInterceptor {
	public static final String LAST_URL_KEY = "lastURL";
	public static final String TO_LAST_URL = "toLastURL";

	// 如果
	// 如果你invoke了 那么你只能通过addPreResultListener来修改resultcode
	// 不能用其他方法
	public String intercept(ActionInvocation ai) throws Exception {
		ai.addPreResultListener( new PreResultListener() {
			public void beforeResult(ActionInvocation invocation, String resultCode) {
				Object a = invocation.getAction();
				if (a instanceof LastURLOutputFlag) {
					boolean to = ( (LastURLOutputFlag) a ).isToLastURL();
					if (to) {
						invocation.getStack().findString( LAST_URL_KEY, true );
						invocation.setResultCode( TO_LAST_URL );
					}
				}
			}
		} );
		return ai.invoke();
	}
}
