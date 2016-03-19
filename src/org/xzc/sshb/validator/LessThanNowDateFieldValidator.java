package org.xzc.sshb.validator;

import java.util.Date;

import com.opensymphony.xwork2.validator.validators.AbstractRangeValidator;

/**
 * 判断给出的日期小于当前时间
 * @author xzchaoo
 *
 */
public class LessThanNowDateFieldValidator extends AbstractRangeValidator<Date> {
	public LessThanNowDateFieldValidator() {
		super( Date.class );
	}

	public Date getMax() {
		return new Date();
	}
}
