package org.xzc.sshb.schedule;

import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;

import javassist.bytecode.LineNumberAttribute.Pc;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.DigestUtils;
import org.xzc.sshb.domain.PayCard;
import org.xzc.sshb.listener.MyHttpSessionActivationListener;
import org.xzc.sshb.listener.MyHttpSessionListener;
import org.xzc.sshb.service.IItemService;
import org.xzc.sshb.service.IPayCardService;
import org.xzc.sshb.service.IRecordService;
import org.xzc.sshb.service.IStateService;

public class MyJobHolder {
	private static final Log LOG = LogFactory.getLog( MyJobHolder.class );
	private static final String MONTH_RANK_TAG = "每月销量排行更新";

	private IRecordService recordService;
	private IStateService stateService;
	private IItemService itemService;
	private IPayCardService payCardService;

	@Resource
	public void setPayCardService(IPayCardService payCardService) {
		this.payCardService = payCardService;
	}

	/**
	 * 自动生成充值卡,但是保持可用的总量不超过10张
	 */
	public void generatePayCard() {
		int currentAvaiableCount = payCardService.listAvailablePayCardsCount();
		for (int i = currentAvaiableCount; i < 10; ++i) {
			PayCard pc = payCardService.generatePayCard();
			pc.setValue( 100 );
			payCardService.add( pc );
		}
		if (currentAvaiableCount < 10) {
			LOG.info( "产生了" + ( 10 - currentAvaiableCount ) + "张百元充值卡" );
		}
	}

	/**
	 * 生成销量排行
	 */
	public void saleRank() {

		// 截止到目前为止的top10, 这里即使取消订单了也会算进去
		// select i.id,(select count(*) from recorditem as ri where ri.item_id=i.id) as quantity
		// from item as i
		// order by quantity desc limit 0,10;

		// 最近10分钟内的top10
//		select i.id, (sum(ri.quantity)) as quantity from item as i natural join recorditem as ri natural join reocrd as r
//		where r.buyTime 属于某个范围
//		group by i.id
//		order by quantity desc
//		limit 0,10

		// 最近1天的top10
//		mysql> select i.id,sum(ri.quantity) as quantity
//		from item as i left join recorditem as ri on i.id=ri.item_id join record as r on ri.record_id=r.id
//		where r.buyTime > date_sub(now(),interval 1 day)
//		group by i.id order by quantity desc
//		limit 0,10;

		/*	Date d = new Date();
			d = DateUtils.addDays( d, -1 );
			List<RankItemValue> list = itemService.listSaleRank( d, 10 );
			for (RankItemValue o : list) {
				System.out.println( o );
			}*/
		try {
			LOG.info( MONTH_RANK_TAG + ": 开始" );
			itemService.updateMonthRankItems();
			LOG.info( MONTH_RANK_TAG + ": 成功" );
		} catch (Exception ex) {
			LOG.info( MONTH_RANK_TAG + ": 遇到异常" );
			throw ex;
		} finally {
			LOG.info( MONTH_RANK_TAG + ": 结束" );
		}
	}

	public void sessionScan() {
		Set<HttpSession> passivated = MyHttpSessionActivationListener.SESSION_PASSIVATED;
		Set<HttpSession> activied = MyHttpSessionActivationListener.SESSION_ACVITIVED;
		LinkedList<HttpSession> sessions = MyHttpSessionListener.SESSIONS;
		long beg = new Date().getTime();
		synchronized (sessions) {
			LOG.info( "锁住sessions" );
			synchronized (passivated) {
				LOG.info( "锁住passivated" );
				sessions.removeAll( passivated );
				passivated.clear();
			}
			synchronized (activied) {
				LOG.info( "锁住activied" );
				sessions.addAll( activied );
				activied.clear();
			}
			LOG.info( "扫描session了" + sessions.size() );

			ListIterator<HttpSession> li = sessions.listIterator();
			long now = new Date().getTime();
			while (li.hasNext()) {
				HttpSession s = li.next();
				// 获得上次访问的时间,与当前时间相比 如果...时间超过10分钟,那么久把它删掉,并且钝化数据到数据库
				if (now - s.getLastAccessedTime() > 600000) {
					s.invalidate();
					li.remove();
					LOG.info( s.getId() + "因为超时而注销掉" );
				}
			}
		}
		long end = new Date().getTime();
		LOG.info( "耗时" + ( end - beg ) );
	}

	/**
	 * 自动发货装置
	 */
	public void autoSend() {
		//LOG.info( "我是任务,现在暂时不做任何事情" );
		recordService.sendRightNow();
	}

	@Resource
	public void setRecordService(IRecordService recordService) {
		this.recordService = recordService;
	}

	@Resource
	public void setStateService(IStateService stateService) {
		this.stateService = stateService;
	}

	@Resource
	public void setItemService(IItemService itemService) {
		this.itemService = itemService;
	}

}
