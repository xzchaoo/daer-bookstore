package org.xzc.sshb.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.Scriptable;

public class JSONUtils {
	public static Object inputStreamToObject(InputStream is) throws JSONException, IOException {
		Context cx = Context.enter();
		try {
			Scriptable scope = cx.initStandardObjects();
			cx.evaluateString( scope, IOUtils.toString( is, "utf-8" ), null, 0, null );
			Object result = scope.get( "a", scope );
			return JSONUtil.deserialize( NativeJSON.stringify( cx, scope, result, null, null ).toString() );
		} finally {
			Context.exit();
		}
	}

	public static Object stringToObject2(String s) throws JSONException {
		// 为key加上单引号
		// 默认key不可以由单引号
		Pattern p = Pattern.compile( "(?!<[_0-9a-zA-Z\\.])([_0-9a-zA-Z\\.]+)(?=\\s*:)" );
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

		return JSONUtil.deserialize( s );
	}
}
