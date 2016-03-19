package org.xzc.sshb.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xzc.sshb.dao.IItemDao;
import org.xzc.sshb.domain.Item;
import org.xzc.sshb.service.IItemService;
import org.xzc.sshb.utils.DateUtils;
import org.xzc.sshb.value.RankItemValue;

@Service
public class ItemService extends BaseService<Item> implements IItemService {
	private IItemDao itemDao;

	@Resource
	public void setItemDao(IItemDao itemDao) {
		this.itemDao = itemDao;
		baseDao = itemDao;
	}

	@Override
	public List<Item> listByNameKeyword(String name) {
		return itemDao.listByNameKeyword( name );
	}

	@Override
	public List<Item> listByNameKeyword(int category, String name, int offset, int count) {
		return itemDao.listByNameKeyword( category, name, offset, count );
	}

	@Override
	public int listByNameKeywordCount(int category, String name) {
		return itemDao.listByNameKeywordCount( category, name );
	}

	// private GetRankItemValue ret;

	public static final class RankItemsValue {
		public List<Integer> itemIds;
		public List<String> names;
		public List<Integer> quantities;
	}

	/**
	 * TODO 月排行榜数据不可修改
	 */
	private List<RankItemValue> listSaleRank = Collections.unmodifiableList( Collections.EMPTY_LIST );
	private Date lastUpdateSaleRankDate = new Date();

	@Override
	public List<RankItemValue> getMonthRankItems() {
		return Collections.unmodifiableList( listSaleRank );
	}

	/**
	 * 更新每月的销量排行榜
	 */
	@Override
	public void updateMonthRankItems() {
		// 不用锁也行
		listSaleRank = Collections.unmodifiableList( itemDao.listSaleRank( DateUtils.getCurrentMonth(), 10 ) );
		lastUpdateSaleRankDate = new Date();
	}

	@Override
	public Date getLastUpdateSaleRankDate() {
		return lastUpdateSaleRankDate;
	}

	@Override
	public List<Item> listByCategory(int category, int offset, int count) {
		return itemDao.listByCategory( category, offset, count );
	}

	@Override
	public int listByCategoryCount(int category) {
		return itemDao.listByCategoryCount( category );
	}

	@Override
	public List<RankItemValue> listBySaleVolume(int count) {
		return itemDao.listSaleRank( count );
	}

}
