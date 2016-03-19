package org.xzc.sshb.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xzc.sshb.dao.IItemDao;
import org.xzc.sshb.dao.IRecordDao;
import org.xzc.sshb.domain.Record;
import org.xzc.sshb.domain.RecordItem;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.service.IStatisticsService;
import org.xzc.sshb.service.IUserService;

@Service
public class StatisticsService implements IStatisticsService {
	@Resource
	private IUserService userService;
	@Resource
	private IItemDao itemDao;

	@Resource
	private IRecordDao recordDao;

	@Override
	public List<Record> listRecord(User user, Date beginTime, Date endTime) {
		return recordDao.list( user, beginTime, endTime );
	}

	@Override
	public List<RecordItem> listRecordItem(User user, int category, Date beginTime, Date endTime) {
		return null;
	}

	@Override
	public List<User> listUserByExpense(Date beginTime, Date endTime, int count) {
		return null;
	}

}
