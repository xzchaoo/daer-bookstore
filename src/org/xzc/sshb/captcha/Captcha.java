package org.xzc.sshb.captcha;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记一个方法是否需要先检查验证码,value的值用于表示如果验证失败struts2的返回值
 * 比如
 * @Captcha("vc_fail")
 * public String doSomething(){
 * 	...
 * }
 * 表明这个方法需要检查验证码,那么拦截器在这之间就会对验证码进行检查,如果验证失败了,那么就会
 * 直接返回resultCode="vc_fail"
 * 
 * @author xzchaoo
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Captcha {
	String value() default "vc";
}
