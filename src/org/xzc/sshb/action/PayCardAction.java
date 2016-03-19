package org.xzc.sshb.action;

import java.util.List;

import javax.annotation.Resource;

import org.xzc.sshb.captcha.Captcha;
import org.xzc.sshb.domain.PayCard;
import org.xzc.sshb.service.IPayCardService;
import org.xzc.sshb.service.IUserService;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 用于处理跟 充值卡相关的请求
 * 
 * @author xzchaoo
 * 
 */
public class PayCardAction extends ActionSupport {
	private IPayCardService payCardService;
	private List<PayCard> payCardList;
	private IUserService userService;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public String listUI() {
		payCardList = payCardService.listAvailablePayCards();
		return SUCCESS;
	}

	private double value;

	@Captcha
	public String add() {
		PayCard pc = payCardService.generatePayCard();
		pc.setValue( value );
		payCardService.add( pc );
		return SUCCESS;
	}

	@Resource
	public void setPayCardService(IPayCardService payCardService) {
		this.payCardService = payCardService;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public List<PayCard> getPayCardList() {
		return payCardList;
	}

	private String key;

	public void setKey(String key) {
		this.key = key;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String password;

	public String chongzhi() {
		PayCard pc = payCardService.getCharge( key, password );
		if (pc == null) {
			return "invalid";
			// 无效的卡
		} else if (pc.isUsed()) {
			// 被用过了
			return "used";
		} else
			payCardService.charge( pc, userService.getCurrentUser() );
		return SUCCESS;
	}
}
