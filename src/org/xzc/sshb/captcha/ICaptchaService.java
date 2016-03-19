package org.xzc.sshb.captcha;

import java.awt.image.BufferedImage;

public interface ICaptchaService {
	public static final String SESSION_KEY = "org.xzc.captcha";

	/**
	 * 根据指定的text创建图像
	 * 
	 * @param text
	 * @return
	 */
	@Deprecated
	public BufferedImage createImage(String text);

	/**
	 * 创建验证码,自动设置到session里,返回它的图像
	 * 
	 * @return
	 */
	public BufferedImage createImage();

	/**
	 * 返回随机的文本 4位 大写字母
	 * 
	 * @return
	 */
	@Deprecated
	public String createText();

	/**
	 * 验证text是否符合当前session的验证码,验证后不管成功与否都要移除
	 * 
	 * @param text
	 * @return
	 */
	public boolean validate(String text);
}
