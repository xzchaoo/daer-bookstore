package org.xzc.sshb.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.xzc.sshb.domain.Item;
import org.xzc.sshb.domain.Record;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.interceptor.IResultCodeReplace;
import org.xzc.sshb.service.IItemService;
import org.xzc.sshb.service.IStatisticsService;
import org.xzc.sshb.service.IUserService;
import org.xzc.sshb.value.HuaqianValue;
import org.xzc.sshb.value.RankItemValue;

import com.opensymphony.xwork2.ActionSupport;

//用于一些公共的操作 目前还比较简单
public class PublicAction extends ActionSupport implements IResultCodeReplace {
	private Date beginTime;
	private Date endTime;
	// 主页要显示的商品
	private Item item;

	// ItemService
	private IItemService itemService;

	// 随机数
	private Random random = new Random();

	private List<Record> records;

	@Resource
	private IStatisticsService statisticsService;

	private String username;

	@Resource
	private IUserService userService;

	public Date getBeginTime() {
		return beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public Item getItem() {
		return item;
	}

	public String getUsername() {
		return username;
	}

	// 主页导航
	// 随机选择一个商品
	public String index() {
		// System.out.println( ServletActionContext.getServletContext().getRealPath( "." ) );
		List<Item> list = itemService.list();
		item = list.get( random.nextInt( list.size() ) );
		return SUCCESS;
	}

	public String messageUI() {
		return SUCCESS;
	}

	// msg导航
	public String msg() {
		return SUCCESS;
	}

	public String preferenceUI() {
		return SUCCESS;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setItemService(IItemService itemService) {
		this.itemService = itemService;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String statisticsQuery() {
		if (endTime != null) {
			endTime = DateUtils.truncate( endTime, Calendar.DAY_OF_MONTH );
			endTime = DateUtils.addDays( endTime, 1 );
		}
		records = statisticsService.listRecord( userService.get( username ), beginTime, endTime );
		return SUCCESS;
	}

	public List<Record> getRecords() {
		return records;
	}

	private List<User> caifu;
	private List<HuaqianValue> huaqian;
	private List<Item> zuigui;
	private List<RankItemValue> changxiao;

	public List<RankItemValue> getChangxiao() {
		return changxiao;
	}

	public List<Item> getZuigui() {
		return zuigui;
	}

	public List<User> getCaifu() {
		return caifu;
	}

	public String statisticsUI() {
		// 财富排行
		caifu = userService.listOrderBy( "money desc", 0, 3 );
		// 花钱排行
		huaqian = userService.listHuaqian( 3 );

		zuigui = itemService.listOrderBy( "price desc", 0, 3 );

		changxiao = itemService.listBySaleVolume( 3 );

		// 商品销量排行
		return SUCCESS;
	}

	public List<HuaqianValue> getHuaqian() {
		return huaqian;
	}
}
