package org.xzc.sshb.service;

import java.util.List;

import org.xzc.sshb.domain.Category;

public interface ICategoryService {
	Category get(int id);

	List<Category> list();
}
