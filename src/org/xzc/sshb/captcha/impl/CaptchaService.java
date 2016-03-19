package org.xzc.sshb.captcha.impl;

import java.awt.image.BufferedImage;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.xzc.sshb.captcha.ICaptchaService;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.util.Config;
import com.opensymphony.xwork2.ActionContext;

/**
 * 这个实现是跟struts2,spring相关的
 * 
 * @author xzchaoo
 * 
 */
@Service
public class CaptchaService implements ICaptchaService, InitializingBean {

	private Properties ps = new Properties();

	private SimpleCaptcha sc = new SimpleCaptcha();

	@Override
	public void afterPropertiesSet() throws Exception {
		if (!inited)
			setConfig( new Properties() );
		sc.setConfig( new Config( ps ) );
		ps = null;
	}

	@Override
	public BufferedImage createImage() {
		String answer = createText();
		ActionContext.getContext().getSession().put( SESSION_KEY, answer );
		return createImage( answer );
	}

	@Override
	public BufferedImage createImage(String text) {
		return sc.createImage( text );
	}

	@Override
	public String createText() {
		return RandomStringUtils.random( 4, true, false ).toLowerCase();
	}

	private boolean inited = false;

	public void setConfig(Properties p) {
		ps.put( Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, p.getProperty( "font", "微软雅黑" ) );
		ps.put( Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, p.getProperty( "color", "red" ) );
		ps.put( Constants.KAPTCHA_IMAGE_WIDTH, p.getProperty( "width", "110" ) );
		ps.put( Constants.KAPTCHA_IMAGE_HEIGHT, p.getProperty( "height", "30" ) );
		ps.put( Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, p.getProperty( "size", "25" ) );
		ps.put( Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, p.getProperty( "space", "5" ) );
		inited = true;
	}

	@Override
	public boolean validate(String text) {
		String answer = (String) ActionContext.getContext().getSession().remove( SESSION_KEY );
		return answer != null && text != null && answer.equals( text );
	}

}
