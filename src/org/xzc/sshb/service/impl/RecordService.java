package org.xzc.sshb.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.xzc.sshb.dao.IRecordDao;
import org.xzc.sshb.domain.Chargeback;
import org.xzc.sshb.domain.Record;
import org.xzc.sshb.domain.RecordItem;
import org.xzc.sshb.domain.State;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.service.IRecordService;
import org.xzc.sshb.service.IStateService;
import org.xzc.sshb.service.IUserService;
import org.xzc.sshb.service.ServiceException;

@Service
public class RecordService extends BaseService<Record> implements IRecordService {
	private static final Logger LOG = Logger.getLogger( RecordService.class );
	protected IRecordDao recordDao;
	private IStateService stateService;
	private IUserService userService;

	@Override
	public List<Record> getByUser(User user) {
		return recordDao.getByUser( user );
	}

	@Override
	public List<Record> getByUser(User user, int offset, int count) {
		return recordDao.getByUser( user, offset, count );
	}

	@Override
	public List<Record> getByUserByNotState(User user, State state) {
		return recordDao.getByUserByNotState( user, state );
	}

	@Override
	public List<Record> getByUserByNotState(User user, State state, int offset, int count) {
		return recordDao.getByUserByNotState( user, state, offset, count );
	}

	@Override
	public int getByUserByNotStateCount(User user, State state) {
		return recordDao.getByUserByNotStateCount( user, state );
	}

	@Override
	public List<Record> getByUserByNotStateOrderBy(User user, State state, String orderby) {
		return recordDao.getByUserByNotStateOrderBy( user, state, orderby );
	}

	@Override
	public List<Record> getByUserByNotStateOrderBy(User user, State state, String orderby, int offset, int count) {
		return recordDao.getByUserByNotStateOrderBy( user, state, orderby, offset, count );
	}

	@Override
	public List<Record> getByUserByState(User user, State state) {
		return recordDao.getByUserByState( user, state );
	}

	@Override
	public List<Record> getByUserByState(User user, State state, int offset, int count) {
		return recordDao.getByUserByState( user, state, offset, count );
	}

	@Override
	public int getByUserByStateCount(User user, State state) {
		return recordDao.getByUserByStateCount( user, state );
	}

	@Override
	public List<Record> getByUserByStateOrderBy(User user, State state, String orderby) {
		return recordDao.getByUserByStateOrderBy( user, state, orderby );
	}

	@Override
	public List<Record> getByUserByStateOrderBy(User user, State state, String orderby, int offset, int count) {
		return recordDao.getByUserByStateOrderBy( user, state, orderby, offset, count );
	}

	@Override
	public int getByUserCount(User user) {
		return recordDao.getByUserCount( user );
	}

	@Override
	public List<Record> getByUserOrderBy(User user, String orderby) {
		return recordDao.getByUserOrderBy( user, orderby );
	}

	@Override
	public List<Record> getByUserOrderBy(User user, String orderby, int offset, int count) {
		return recordDao.getByUserOrderBy( user, orderby, offset, count );
	}

	@Override
	public void markAsDeleted(Record record) {
		record = get( record.getId() );
		record.setState( stateService.get( State.RECORD_DELETED ) );
	}

	/**
	 * 根据订单项的状态修复所有订单状态, 包括 订单项的数量(itemCount) 已评价的订单量(evaluatedCount) 总价格(totalPrice) 和状态(state), FOR DEBUG
	 * 没写好
	 * @return
	 */
	@Deprecated
	public void repair() {
		for (Record r : recordDao.list()) {
			Set<RecordItem> set = r.getRecordItems();
			// 修复itemCount
			r.setItemCount( set.size() );
			double totalPrice = 0;
			// TODO 物品购买时候的价格应该被记下来
			int evaluatedCount = 0;
			for (RecordItem ri : set) {
				totalPrice += ri.getQuantity() * ri.getItem().getPrice();
				if (ri.getState().getId() == State.RECORDITEM_EVALUATED)
					++evaluatedCount;
			}
			// 修复totalPrice
			r.setTotalPrice( totalPrice );

			// 修复evaluatedCount
			r.setEvaluatedCount( evaluatedCount );

			// 修复state
			if (r.isFinished())
				r.setState( stateService.get( State.RECORD_FINISHED ) );

			// update( r );

		}
	}

	@Resource
	public void setRecordDao(IRecordDao recordDao) {
		this.recordDao = recordDao;
		this.baseDao = recordDao;
	}

	public List<Record> listByState(State state) {
		return recordDao.listByState( state );
	}

	@Resource
	public void setStateService(IStateService stateService) {
		this.stateService = stateService;
	}

	/**
	 * 确认收货
	 */
	@Override
	public void receive(Record record) {
		record = this.get( record.getId() );
		User cu = userService.getCurrentUser();

		if (!record.getUser().equals( cu )) {
			throw new ServiceException( "你不能修改别人的订单" );
		}

		// 订单状态错误
		if (!record.getState().equals( stateService.get( State.RECORD_SENT ) )) {
			throw new ServiceException( "订单状态错误 " + record.getState().getId() + " " + record.getState().getName() );
		}

		// 修改订单状态
		record.setState( stateService.get( State.RECORD_RECEIVED ) );

		// 修改订单项状态
		State s = stateService.get( State.RECORDITEM_RECEIVED );
		for (RecordItem ri : record.getRecordItems()) {
			ri.setState( s );
		}

	}

	@Resource
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	private String reason;

	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * 取消订单,只有在"买家已付款"的时候才行
	 */
	@Override
	public void cancel(Record record) {
		User cu = userService.getCurrentUser();
		record = this.get( record.getId() );

		if (!record.getUser().equals( cu )) {
			throw new ServiceException( "你不能修改别人的订单" );
		}

		if (record.getState().getId() != State.RECORD_PAID) {
			throw new ServiceException( "你已经无法修改当前的订单状态 " + record.getState().getId() );
		}

		// 这里主要是debug用时,一个record可能已经有了chargeback了,
		// 不然一般情况下一个订单被取消了 就不能回复了(因此不用考虑这个问题)
		Chargeback c = record.getChargeback();
		if (c == null) {
			c = new Chargeback();
			record.setChargeback( c );
		}
		c.setReason( StringEscapeUtils.escapeHtml4( reason ) );
		c.setState( stateService.get( State.CHARGEBACK_OK ) );
		c.setTime( new Date() );
		c.setRecord( record );

		record.setDescription( "这个订单被取消了" );
		record.setState( stateService.get( State.RECORD_CANCELED ) );

		// 修改所有的订单项的状态
		State s = stateService.get( State.RECORDITEM_CANCELED );
		for (RecordItem ri : record.getRecordItems())
			ri.setState( s );
		// 钱加回去
		cu.setMoney( cu.getMoney() + record.getTotalPrice() );
		userDao.update( cu );
	}

	@Override
	public void sendRightNow() {
		State paid = stateService.get( State.RECORD_PAID );
		List<Record> list = listByState( paid );

		LOG.info( "本次找到" + list.size() + "个未发货订单" );
		// 将他们设置为已发货
		State sent = stateService.get( State.RECORD_SENT );
		for (Record r : list) {
			LOG.info( "订单编号=" + r.getId() + "发货!" );
			r.setState( sent );
		}
		LOG.info( "发货完毕" );
	}

	@Override
	public void deleteRecordAjax(Record record, Map<String, Object> jsonResult) {
		if (record == null) {
			jsonResult.put( "success", false );
			jsonResult.put( "msg", "没有指定参数" );
			return;
		}
		// 判断这个订单是否数据当前的用户 不能删除别人的订单
		record = get( record.getId() );
		if (record == null) {
			jsonResult.put( "success", false );
			jsonResult.put( "msg", "订单不存在" );
			return;
		}
		User cu = userService.getCurrentUser();
		if (record.getUser().getId() != cu.getId()) {
			jsonResult.put( "success", false );
			jsonResult.put( "msg", "你不能删除别人的订单" );
			return;
		}
		markAsDeleted( record );
		jsonResult.put( "success", true );
		jsonResult.put( "msg", "删除成功" );
	}

}
