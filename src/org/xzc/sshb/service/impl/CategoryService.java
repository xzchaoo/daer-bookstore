package org.xzc.sshb.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xzc.sshb.dao.ICategoryDao;
import org.xzc.sshb.domain.Category;
import org.xzc.sshb.service.ICategoryService;

@Service
public class CategoryService implements ICategoryService {
	private ICategoryDao categoryDao;

	@Resource
	public void setCategoryDao(ICategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	@Override
	public Category get(int id) {
		return categoryDao.get( id );
	}

	@Override
	public List<Category> list() {
		return categoryDao.list();
	}

}
