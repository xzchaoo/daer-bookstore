package org.xzc.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ognl.ObjectPropertyAccessor;
import ognl.Ognl;
import ognl.OgnlException;
import ognl.OgnlRuntime;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.json.JSONUtil;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.Scriptable;
import org.xzc.sshb.domain.User;

public class Test2 {
	@Test
	public void testFileName() {
		File f = new File( "abc.txt" );
	}

	public static class MyRoot {
		public User a;
		public User b;
	}

	public void test3() throws Exception {
		List<Integer> list = new ArrayList<Integer>();
		list.add( 11 );
		list.add( 33 );

		Map<String, Object> ctx = new HashMap<String, Object>();
		OgnlRuntime.setPropertyAccessor( MyRoot.class, new ObjectPropertyAccessor() {
			public Object getProperty(Map context, Object target, Object oname) throws OgnlException {
				MyRoot r = (MyRoot) target;
				return super.getProperty( context, r.a != null ? r.a : r.b, oname );
			}
		} );
		MyRoot mr = new MyRoot();
		mr.b = new User();
		mr.b.setName( "xzcb" );
		System.out.println( Ognl.getValue( "name", mr ) );
		// 我发现这个可以被当做for循环!
		// System.out.println( Ognl.getValue( "[0].{?#this>3}", list ) );

	}

	public void testOGNL() throws Exception {
		User u = new User();
		u.setName( "xzc" );
		Map<String, Object> context = new HashMap<String, Object>();
		context.put( "a", 1 );
		context.put( "b", 2 );

		Map<String, Object> context2 = new HashMap<String, Object>();
		context.put( "a", 3 );
		context.put( "b", 4 );

		// System.out.println(Ognl.getValue( Ognl.parseExpression( "#a+#b" ), context,context2 ));
		Ognl.setValue( "name", context, u, "xzc2" );

		Ognl.setValue( "#a", context, u, "xzc3" );

		System.out.println( Ognl.getValue( Ognl.parseExpression( "name+#a" ), context, u ) );

	}

	public void test1() throws Exception {
		Context cx = Context.enter();
		try {
			Scriptable scope = cx.initStandardObjects();
			cx.evaluateString( scope, FileUtils.readFileToString( new File(
					"src/org/xzc/sshb/action/UserAction-login-validation.js" ), "utf-8" ), null, 0, null );
			Object result = scope.get( "a", scope );
			System.out.println( NativeJSON.stringify( cx, scope, result, null, null ).toString() );
		} finally {
			Context.exit();
		}
	}

	public void say() throws Exception {
		String s = FileUtils.readFileToString( new File( "src/org/xzc/a.txt" ) );

		// 为key加上单引号
		// 默认key不可以由单引号
		Pattern p = Pattern.compile( "(?!<[0-9a-zA-Z\\.])([0-9a-zA-Z\\.]+)(?=\\s*:)" );
		Matcher m = p.matcher( s );
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement( sb, "'" + m.group() + "'" );
		}
		m.appendTail( sb );
		s = sb.toString();

		// 去除多余的逗号,
		p = Pattern.compile( ",(?=\\s*})" );
		m = p.matcher( s );
		sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement( sb, "" );
		}
		m.appendTail( sb );
		s = sb.toString();

		System.out.println( JSONUtil.deserialize( s ) );
	}
}
