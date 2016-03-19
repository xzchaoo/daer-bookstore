package org.xzc.sshb.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.xzc.sshb.dao.IBaseDao;
import org.xzc.sshb.dao.IItemDao;
import org.xzc.sshb.dao.IRecordDao;
import org.xzc.sshb.dao.IRecordItemDao;
import org.xzc.sshb.dao.IStateDao;
import org.xzc.sshb.dao.IUserDao;
import org.xzc.sshb.service.ICartService;
import org.xzc.sshb.service.IItemService;
import org.xzc.sshb.service.IRecordItemService;
import org.xzc.sshb.service.IRecordService;
import org.xzc.sshb.service.IStateService;
import org.xzc.sshb.service.IUserService;

/***
 * Service的基类,用于将大部分方法直接委托给Dao
 * 
 * @author xzchaoo
 * 
 * @param <T>
 */
public abstract class BaseService<T> implements IBaseDao<T> {
	protected IBaseDao<T> baseDao;

	protected IItemDao itemDao;
	protected IUserDao userDao;
	protected IRecordDao recordDao;
	protected IRecordItemDao recordItemDao;
	protected IStateDao stateDao;

	protected IItemService itemService;
	protected IUserService userService;
	protected IRecordService recordService;
	protected IRecordItemService recordItemService;
	protected IStateService stateService;
	protected ICartService cartService;

	@Override
	public void add(T t) {
		baseDao.add( t );
	}

	@Override
	public void delete(T t) {
		baseDao.delete( t );
	}

	@Override
	public T get(int id) {
		return baseDao.get( id );
	}

	@Override
	public List<T> get(List<Integer> ids) {
		return baseDao.get( ids );
	}

	@Override
	public List<T> list() {
		return baseDao.list();
	}

	@Override
	public List<T> list(int offset, int count) {
		return baseDao.list( offset, count );
	}

	@Override
	public int listCount() {
		return baseDao.listCount();
	}

	@Override
	public List<T> listOrderBy(String orderby) {
		return baseDao.listOrderBy( orderby );
	}

	@Override
	public List<T> listOrderBy(String orderby, int offset, int count) {
		return baseDao.listOrderBy( orderby, offset, count );
	}

	@Override
	public void update(T t) {
		baseDao.update( t );
	}

	@Resource
	public void setItemDao(IItemDao itemDao) {
		this.itemDao = itemDao;
	}

	@Resource
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	@Resource
	public void setRecordDao(IRecordDao recordDao) {
		this.recordDao = recordDao;
	}

	@Resource
	public void setRecordItemDao(IRecordItemDao recordItemDao) {
		this.recordItemDao = recordItemDao;
	}

	@Resource
	public void setStateDao(IStateDao stateDao) {
		this.stateDao = stateDao;
	}

	@Resource
	public void setItemService(IItemService itemService) {
		this.itemService = itemService;
	}

	@Resource
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Resource
	public void setRecordService(IRecordService recordService) {
		this.recordService = recordService;
	}

	@Resource
	public void setRecordItemService(IRecordItemService recordItemService) {
		this.recordItemService = recordItemService;
	}

	@Resource
	public void setStateService(IStateService stateService) {
		this.stateService = stateService;
	}

	@Resource
	public void setCartService(ICartService cartService) {
		this.cartService = cartService;
	}

}
