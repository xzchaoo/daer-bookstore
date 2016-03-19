package org.xzc.sshb.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.xzc.sshb.captcha.ICaptchaService;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 用于产生验证码
 * 
 * @author xzchaoo
 * 
 */
public class CaptchaAction extends ActionSupport {
	private static final String IMAGE_FORMAT = "jpg";
	private ICaptchaService captchaService;
	private InputStream inputStream;

	public String show() throws Exception {
		BufferedImage bi = captchaService.createImage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( bi, IMAGE_FORMAT, baos );
		inputStream = new ByteArrayInputStream( baos.toByteArray() );
		return SUCCESS;
	}

	@Resource
	public void setCaptchaService(ICaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

}
