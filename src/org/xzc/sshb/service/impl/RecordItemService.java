package org.xzc.sshb.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.xzc.sshb.action.ItemEvaluationValue;
import org.xzc.sshb.dao.IRecordItemDao;
import org.xzc.sshb.domain.Item;
import org.xzc.sshb.domain.Record;
import org.xzc.sshb.domain.RecordItem;
import org.xzc.sshb.domain.State;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.service.IRecordItemService;
import org.xzc.sshb.service.IStateService;
import org.xzc.sshb.service.IUserService;
import org.xzc.sshb.service.ServiceException;
import org.xzc.sshb.value.ItemViewDetailSalesVolumeValue;

@Service
public class RecordItemService extends BaseService<RecordItem> implements IRecordItemService {
	private IRecordItemDao recordItemDao;
	private IStateService stateService;
	private IUserService userService;

	/**
	 * 对订单项进行评价
	 */
	@Override
	public void evaluate(RecordItem recordItem) {
		User user = userService.getCurrentUser();
		RecordItem ri = this.get( recordItem.getId() );

		if (ri.getRecord().getUser().getId() != user.getId())
			throw new ServiceException( "你不能评价别人的订单项" );

		if (ri.getState().getId() != State.RECORDITEM_RECEIVED)
			throw new ServiceException( "你不能修改当前的订单状态: " + ri.getState().getId() );

		// 转义 TODO 长度记得截断
		ri.setEvaluation( StringEscapeUtils.escapeHtml4( recordItem.getEvaluation() ) );
		// 设置时间
		ri.setEvaluationTime( new Date() );
		// 设置打分
		if (recordItem.getStar() < 0 || recordItem.getStar() > 5)
			throw new ServiceException( "打分必须介于0-5" );

		ri.setStar( recordItem.getStar() );

		// 修改为已评价
		ri.setState( stateService.get( State.RECORDITEM_EVALUATED ) );

		// 增加订单的已经评价数
		Record r = ri.getRecord();
		r.setEvaluatedCount( r.getEvaluatedCount() + 1 );
		// 如果订单的所有项都完成了评价
		if (r.isFinished()) {
			r.setState( stateService.get( State.RECORD_FINISHED ) );
		}

	}

	@Override
	public int listByItemCount(Item item) {
		return recordItemDao.listByItemCount( item );
	}

	@Override
	public List<ItemEvaluationValue> listEvaluations(Item item) {
		return this.listEvaluations( item, -1, -1 );
	}

	@Override
	public List<ItemEvaluationValue> listEvaluations(Item item, int offset, int count) {
		List<ItemEvaluationValue> ret = new ArrayList<ItemEvaluationValue>();
		List<RecordItem> list = recordItemDao.listByItemByStateOrderBy( item,
				stateService.get( State.RECORDITEM_EVALUATED ), offset, count, "id desc" );
		for (RecordItem ri : list) {
			ItemEvaluationValue v = new ItemEvaluationValue();
			v.evaluation = ri.getEvaluation();
			v.evaluationTime = ri.getEvaluationTime();
			v.star = ri.getStar();
			v.username = ri.getRecord().getUser().getName();
			ret.add( v );
		}
		return ret;
	}

	// TODO等下根据State进行修改
	@Override
	public int listEvaluationsCount(Item item) {
		return recordItemDao.listByItemByStateCount( item,
				stateService.get( State.RECORDITEM_EVALUATED ) );
	}

	@Override
	public List<ItemViewDetailSalesVolumeValue> listSalesVolume(Item item, int offset, int count) {
		List<ItemViewDetailSalesVolumeValue> ret = new ArrayList<ItemViewDetailSalesVolumeValue>();
		List<RecordItem> list = recordItemDao.listByItemOrderBy( item, offset, count, "id desc" );
		for (RecordItem ri : list) {
			ItemViewDetailSalesVolumeValue v = new ItemViewDetailSalesVolumeValue();
			v.buyTime = ri.getRecord().getBuyTime();
			v.quantity = ri.getQuantity();
			v.username = ri.getRecord().getUser().getName();
			ret.add( v );
		}
		return ret;
	}

	@Resource
	public void setRecordItemDao(IRecordItemDao recordItemDao) {
		this.recordItemDao = recordItemDao;
		this.baseDao = recordItemDao;
	}

	@Resource
	public void setStateService(IStateService stateService) {
		this.stateService = stateService;
	}

	@Resource
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
}
