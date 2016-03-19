package org.xzc.sshb.action;

import org.xzc.sshb.domain.RecordItem;
import org.xzc.sshb.domain.State;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.service.IRecordItemService;
import org.xzc.sshb.service.IUserService;
import org.xzc.sshb.utils.ActionException;

public class RecordItemAction extends MyBaseAction {
	private RecordItem recordItem;

	private User user;

	// TODO 先检查一下是不是当前用户的订单
	public String evaluateUI() {
		user = userService.getCurrentUser();
		recordItem = recordItemService.get( recordItem.getId() );
		if (!recordItem.getRecord().getUser().equals( user ) && !user.isAdmin())
			throw new ActionException( "你不是管理员,不能查看别人的订单详情." );
		return SUCCESS;
	}

	// TODO 先检查一下是不是当前用户的订单
	public String evaluate() {
		recordItemService.evaluate( recordItem );
		return SUCCESS;
	}

	public String setRecordItemState() {
		State s = stateService.get( recordItem.getState().getId() );
		recordItem = recordItemService.get( recordItem.getId() );
		recordItem.setState( s );
		return SUCCESS;
	}

	public RecordItem getRecordItem() {
		return recordItem;
	}

	public void setRecordItem(RecordItem recordItem) {
		this.recordItem = recordItem;
	}

	public void setRecordItemService(IRecordItemService recordItemService) {
		this.recordItemService = recordItemService;
	}

	public User getUser() {
		return user;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
