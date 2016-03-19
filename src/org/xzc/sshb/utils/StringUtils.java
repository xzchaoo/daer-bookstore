package org.xzc.sshb.utils;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringEscapeUtils;

public class StringUtils {

	/**
	 * // 对%和_进行转义 如果text为空 返回null
	 * 
	 * @param text
	 * @return
	 */
	public static String escapeSqlLike(String text) {
		if (text == null)
			return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); ++i) {
			char c = text.charAt( i );
			if (c == '_' || c == '%')
				sb.append( '\\' );
			sb.append( c );
		}
		return sb.toString();
	}

	public static void escapeHtml(Object obj, String... properties) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		if (properties != null) {
			for (String p : properties) {
				String value = BeanUtils.getProperty( obj, p );
				value = StringEscapeUtils.escapeHtml4( value );
				BeanUtils.setProperty( obj, p, value );
			}
		}
	}

}
