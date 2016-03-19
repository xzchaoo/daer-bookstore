package org.xzc.sshb.action;

import java.util.HashMap;
import java.util.Map;

import org.xzc.sshb.interceptor.IResultCodeReplace;
import org.xzc.sshb.service.ICartService;
import org.xzc.sshb.service.IItemService;
import org.xzc.sshb.service.IRecordItemService;
import org.xzc.sshb.service.IRecordService;
import org.xzc.sshb.service.IStateService;
import org.xzc.sshb.service.IUserService;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class MyBaseAction extends ActionSupport implements IResultCodeReplace, Preparable {
	protected IUserService userService;
	protected IItemService itemService;
	protected IRecordService recordService;
	protected IRecordItemService recordItemService;
	protected ICartService cartService;
	protected IStateService stateService;
	protected int page = 1;
	protected int rows = 5;
	protected int total = 0;
	protected int offset = 0;
	protected int count = rows;
	protected String ajax;
	protected String errorMsg;
	protected Map<String, Object> jsonResult = new HashMap<String, Object>();

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public IItemService getItemService() {
		return itemService;
	}

	public void setItemService(IItemService itemService) {
		this.itemService = itemService;
	}

	public IRecordService getRecordService() {
		return recordService;
	}

	public void setRecordService(IRecordService recordService) {
		this.recordService = recordService;
	}

	public IRecordItemService getRecordItemService() {
		return recordItemService;
	}

	public void setRecordItemService(IRecordItemService recordItemService) {
		this.recordItemService = recordItemService;
	}

	public ICartService getCartService() {
		return cartService;
	}

	public void setCartService(ICartService cartService) {
		this.cartService = cartService;
	}

	public IStateService getStateService() {
		return stateService;
	}

	public void setStateService(IStateService stateService) {
		this.stateService = stateService;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public Map<String, Object> getJsonResult() {
		return jsonResult;
	}

	@Override
	public void prepare() throws Exception {
		offset = ( page - 1 ) * rows;
		count = rows;
	}

}
