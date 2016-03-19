package org.xzc.sshb.action;

import com.opensymphony.xwork2.ActionSupport;

public class PaginationActionSupport extends ActionSupport {
	protected int page = 1;
	protected int total = 1;
	protected int rows = 5;
	protected String ajax;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotal() {
		return total;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

}
