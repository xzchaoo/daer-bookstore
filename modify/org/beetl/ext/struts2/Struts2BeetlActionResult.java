/*
[The "BSD license"]
Copyright (c) 2011-2013 Joel Li (李家智)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:
1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.beetl.ext.struts2;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.apache.struts2.views.util.ResourceUtil;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.web.WebRender;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.ExceptionHolder;
import com.opensymphony.xwork2.util.reflection.ReflectionProvider;

public class Struts2BeetlActionResult extends StrutsResultSupport {

	ReflectionProvider reflectionProvider = null;
	public static GroupTemplate groupTemplate;
	static {
		Configuration cfg;
		try {
			cfg = Configuration.defaultConfiguration();
			WebAppResourceLoader resourceLoader = new WebAppResourceLoader();
			groupTemplate = new GroupTemplate( resourceLoader, cfg );

		} catch (IOException e) {
			throw new RuntimeException( "加载GroupTemplate失败", e );
		}
	}

	@Inject
	public void setReflectionProvider(ReflectionProvider prov) {
		this.reflectionProvider = prov;
	}

	protected void doExecute(String locationArg, ActionInvocation invocation) throws Exception {
		ActionContext ctx = invocation.getInvocationContext();
		// 自动设置ajax标签
		String ajax = invocation.getStack().findString( "ajax" );
		if (ajax != null)
			locationArg += "#" + ajax;

		HttpServletRequest req = (HttpServletRequest) ctx.get( ServletActionContext.HTTP_REQUEST );
		HttpServletResponse rsp = (HttpServletResponse) ctx.get( ServletActionContext.HTTP_RESPONSE );
		if (!locationArg.startsWith( "/" )) {
			String base = ResourceUtil.getResourceBase( req );
			locationArg = base + "/" + locationArg;
		}

		Template template = groupTemplate.getTemplate( locationArg );

		Object action = invocation.getAction();
		Map<String, Object> values = reflectionProvider.getBeanMap( action );

		String url = invocation.getProxy().getNamespace() + "/" + invocation.getProxy().getActionName();
		String absurl = req.getContextPath() + url;

		WebRender render = new WebRender( groupTemplate ) {
			protected void modifyTemplate(Template template, String key, HttpServletRequest request,
					HttpServletResponse response, Object... args) {
				Map model = (Map) args[0];

				for (Object o : model.entrySet()) {
					Entry entry = (Entry) o;
					String name = (String) entry.getKey();
					Object value = entry.getValue();
					template.binding( name, value );
				}
				ActionContext ac = (ActionContext) args[1];
				Object obj = ac.getValueStack().peek();
				if (obj instanceof ExceptionHolder) {
					ac.put( "exception", ( (ExceptionHolder) obj ).getException() );
				}

				Map<String, String> params = new HashMap<String, String>();
				Enumeration<String> e = request.getParameterNames();
				while (e.hasMoreElements()) {
					String name = e.nextElement();
					params.put( name, request.getParameter( name ) );
				}

				// 注入params
				template.binding( "params", params );
				template.binding( "params2", ac.getParameters() );

				// 注入值栈
				// 没有必要注入 因为值栈的严肃默认在最顶层
				// template.binding( "vs", ac.getValueStack().getContext() );
				// System.out.println( "值栈大小" + ac.getValueStack().size() );
				// 注入
				// 反而是ac要注入如果你想要的话
				template.binding( "ac", ac.getContextMap() );
				// 注入自己常用的东西
				// 占用名字ai
				template.binding( "ai", ac.get( "ai" ) );
				template.binding( "_url", args[2] );
				template.binding( "_absurl", args[3] );

			}
		};

		render.render( locationArg, req, rsp, values, ctx, url, absurl );
	}

}
