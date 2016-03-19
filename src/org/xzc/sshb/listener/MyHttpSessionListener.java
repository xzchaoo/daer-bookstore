package org.xzc.sshb.listener;

import java.util.LinkedList;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 显示简单的监听一下有多少session 后期考虑写一个定时器 定期扫描session,排除无效的session(过时的session)
 * TODO
 * @author xzchaoo
 * 
 */
public class MyHttpSessionListener implements HttpSessionListener {
	private static final LinkedList<HttpSession> sessions = new LinkedList<HttpSession>();
	public static final LinkedList<HttpSession> SESSIONS = sessions;

	public static void add(HttpSession s) {
		synchronized (sessions) {
			sessions.add( s );
		}
	}

	public static void remove(HttpSession s) {
		synchronized (sessions) {
			sessions.remove( s );
		}
	}

	public void sessionCreated(HttpSessionEvent e) {
		//System.out.println( "sessionCreated" );
		add( e.getSession() );
	}

	/**
	 * 一般来说我不会用到这个方法 因为调用这个方法的时候它肯定不在session里了!
	 */
	public void sessionDestroyed(HttpSessionEvent e) {
	}

}
