package org.xzc.sshb.interceptor;

import java.util.HashSet;
import java.util.Set;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

//规定返回值的组织形式
//调用 abc 方法 如果返回success 那么拼凑成 abcSuccess
//以此类推
//以后考虑添加一些排除方法
//因为并不是所有方法都需要这样的处理(或者说有时候这样处理不好!)
//需要定义一些排除方法,我暂时称他们为全局结果
//全局结果(globalResultCode)不会被处理,ajax也不会被处理

public class ResultCodeInterceptor extends AbstractInterceptor {
	private Set<String> globalResultCode = new HashSet<String>();

	public ResultCodeInterceptor() {
		a("shouldLogin","preventLogin","isNotAdmin","exceptionUI");
	}

	private ResultCodeInterceptor a(String... ss) {
		for (String s : ss)
			globalResultCode.add( s );
		return this;
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		Object a = invocation.getAction();
		if (a instanceof IResultCodeReplace) {
			invocation.addPreResultListener( new PreResultListener() {
				public void beforeResult(ActionInvocation invocation, String resultCode) {
					String method = invocation.getProxy().getMethod();
					if (!method.endsWith( "Ajax" ) && !globalResultCode.contains( resultCode )) {
						resultCode = method + resultCode.substring( 0, 1 ).toUpperCase() + resultCode.substring( 1 );
						invocation.setResultCode( resultCode );
					}
				}
			} );
		}
		return invocation.invoke();
	}

}
