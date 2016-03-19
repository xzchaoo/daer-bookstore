package org.xzc.sshb.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xzc.sshb.domain.Record;
import org.xzc.sshb.domain.State;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.service.IRecordService;
import org.xzc.sshb.service.IStateService;
import org.xzc.sshb.service.IUserService;
import org.xzc.sshb.utils.ActionException;

public class RecordAction extends MyBaseAction {
	/**
	 * 用于保存跟记录相关的信息 只有某些方法才会用到
	 */
	private Record record;

	/**
	 * 用于分页显示的数据, 不用user.records获取 因为它无法分页 而这个是可以分页的
	 */
	private List<Record> records;

	/**
	 * 用一个int来表示要显示哪个状态的订单, 用0表示显示所有订单, 若state非0那么只显示状态为对应的State的订单. -1表示没有赋值 此时将采用defaultRecordType
	 */
	private int state = -1;

	/**
	 * ajax方式删除订单
	 * 
	 * @return
	 */
	public String deleteRecordAjax() {
		try {
			recordService.deleteRecordAjax( record, jsonResult );
		} catch (Exception e) {
			LOG.warn( "RecordAction----Exception", e );
			jsonResult.put( "success", "false" );
			jsonResult.put( "msg", "未知原因" );
			// 记录错误
		}
		/*if (record == null) {
			jsonResult.put( "success", false );
			jsonResult.put( "msg", "没有指定参数" );
			return SUCCESS;
		}
		// 判断这个订单是否数据当前的用户 不能删除别人的订单
		record = recordService.get( record.getId() );
		if (record == null) {
			jsonResult.put( "success", false );
			jsonResult.put( "msg", "订单不存在" );
			return SUCCESS;
		}
		User cu = userService.getCurrentUser();
		if (record.getUser().getId() != cu.getId()) {
			jsonResult.put( "success", false );
			jsonResult.put( "msg", "你不能删除别人的订单" );
			return SUCCESS;
		}
		recordService.markAsDeleted( record );
		jsonResult.put( "success", true );
		jsonResult.put( "msg", "删除成功" );
		*/
		return SUCCESS;
	}

	private int defaultRecordType = 0;

	/**
	 * 显示当前账号的订单 page和rows用于分页,用state表示显示哪一种状态的订单
	 * 
	 * @return SUCCESS
	 */
	public String listUI() {
		User user = userService.getCurrentUser();
		if (state == -1)
			state = defaultRecordType;

		// 默认显示全部非删除的订单
		// TODO 要么就确定说 被删除的订单就不显示了 现在还是暂时显示的
		if (state == 0) {
			State deleted = stateService.get( State.RECORD_DELETED );
			records = recordService.getByUserByNotStateOrderBy( user, deleted, "id desc", offset, count );
			total = recordService.getByUserByNotStateCount( user, deleted );
		} else {
			State s = stateService.get( state );
			// 如果没有指定state 则用默认显示
			if (s == null) {
				s = stateService.getByClass( Record.class );
				state = s.getId();
			}
			records = recordService.getByUserByStateOrderBy( user, s, "id desc", offset, count );
			total = recordService.getByUserByStateCount( user, s );
		}
		return SUCCESS;
	}

	// 查看订单详情
	// 检查一下是不是当前用户的订单
	public String viewDetailUI() {
		User user = userService.getCurrentUser();
		record = recordService.get( record.getId() );
		if (!record.getUser().equals( user ) && !user.isAdmin())
			throw new ActionException( "你不是管理员,不能查看别人的订单详情." );
		return SUCCESS;
	}

	/**
	 * 确认收货
	 * 
	 * @return
	 */
	public String receive() {
		recordService.receive( record );
		return SUCCESS;
	}

	/**
	 * 取消订单,只有在未发货的情况下才能,取消订单的理由放在record.description里
	 * 
	 * @return
	 */
	public String cancel() {
		// 判断这个订单是你的
		// 判断这个订单可以被收货
		recordService.cancel( record );
		return SUCCESS;
	}

	/**
	 * 根据订单项的状态修复所有订单状态, 包括 订单项的数量(itemCount) 已评价的订单量(evaluatedCount) 总价格(totalPrice) 和状态(state) debug用
	 * 
	 * @return
	 */
	public String repair() {
		// recordService.repair();
		return SUCCESS;
	}

	/**
	 * 跳转到取消订单页面
	 * 
	 * @return
	 */
	public String cancelUI() {
		// 看看是不是你的订单
		record = recordService.get( record.getId() );
		User cu = userService.getCurrentUser();
		if (record.getUser().getId() != cu.getId()) {
			throw new ActionException( "你不能取消别人的订单!" );
		}
		// 判断订单状态是否可以取消 只有处于 已付款 的账单可以取消
		if (record.getState().getId() != State.RECORD_PAID)
			throw new ActionException( "你无法取消当前状态的订单" );
		return SUCCESS;
	}

	public String setRecordState() {
		State s = stateService.get( record.getState().getId() );
		record = recordService.get( record.getId() );
		if (s.getId() != State.RECORD_CANCELED)
			record.setChargeback( null );
		record.setState( s );
		return SUCCESS;
	}

	/**
	 * 立即发货 FOR DEBUG
	 * 
	 * @return
	 */
	public String sendRightNow() {
		recordService.sendRightNow();
		return SUCCESS;
	}

	//
	// 剩下的都是一堆get set方法可以不看
	//

	public Map<String, Object> getJsonResult() {
		return jsonResult;
	}

	public Record getRecord() {
		return record;
	}

	public List<Record> getRecords() {
		return records;
	}

	public int getState() {
		return state;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public void setRecordService(IRecordService recordService) {
		this.recordService = recordService;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setStateService(IStateService stateService) {
		this.stateService = stateService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setDefaultRecordType(int defaultRecordType) {
		this.defaultRecordType = defaultRecordType;
	}

}
