package org.xzc.sshb.interceptor;

import org.xzc.sshb.captcha.Captcha;
import org.xzc.sshb.captcha.ICaptchaService;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 验证码拦截器
 * 
 * @author xzchaoo
 * 
 */
public class CaptchaInterceptor extends AbstractInterceptor {
	private ICaptchaService captchaService;

	public void setCaptchaService(ICaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	@Override
	public String intercept(ActionInvocation ai) throws Exception {
		Object a = ai.getProxy().getAction();
		String m = ai.getProxy().getMethod();
		Captcha c = a.getClass().getMethod( m ).getAnnotation( Captcha.class );
		// TODO 这里硬编码
		Object obj = ai.getInvocationContext().getParameters().get( "captcha" );
		if (c != null) {
			if (obj != null) {
				String userAnswer = ( (Object[]) obj )[0].toString();
				if (captchaService.validate( userAnswer ))
					return ai.invoke();
			}
			return c.value();
		} else
			return ai.invoke();
	}
}
