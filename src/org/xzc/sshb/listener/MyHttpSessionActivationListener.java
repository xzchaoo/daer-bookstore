package org.xzc.sshb.listener;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

//TODO
public class MyHttpSessionActivationListener implements HttpSessionActivationListener {
	// 钝化了的session
	public static final Set<HttpSession> SESSION_PASSIVATED = new HashSet<HttpSession>();

	// 活化了的session
	public static final Set<HttpSession> SESSION_ACVITIVED = new HashSet<HttpSession>();

	// session钝化
	// 因为不知道这个函数是不是安全的 所以...
	public void sessionWillPassivate(HttpSessionEvent se) {
		synchronized (SESSION_PASSIVATED) {
			SESSION_PASSIVATED.add( se.getSession() );
		}
	}

	// session活化
	public synchronized void sessionDidActivate(HttpSessionEvent se) {
		synchronized (SESSION_ACVITIVED) {
			SESSION_ACVITIVED.remove( se.getSession() );
		}
	}

}
