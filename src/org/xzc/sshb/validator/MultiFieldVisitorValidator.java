package org.xzc.sshb.validator;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.VisitorFieldValidator;

/**
 * 本来是用于扩展VisitorFieldValidator的,使之同时支持多个visitor,有了json之后它不怎么用了...
 * @author xzchaoo
 *
 */
public class MultiFieldVisitorValidator extends VisitorFieldValidator {

	public void validate(Object object) throws ValidationException {
		for (String s : myContext) {
			super.setContext( s );
			super.validate( object );
		}
	}

	private String[] myContext;

	// 用逗号隔开
	public void setContext(String context) {
		this.myContext = context.split( "," );
	}

}
