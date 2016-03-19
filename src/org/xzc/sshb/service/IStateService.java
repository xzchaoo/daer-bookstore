package org.xzc.sshb.service;

import org.xzc.sshb.domain.State;

public interface IStateService {
	/**
	 * 根据id获得State
	 * @param id
	 * @return
	 */
	State get(int id);

	/**
	 * 根据class获得它默认的state,感觉没什么用
	 * @param clazz
	 * @return
	 */
	@Deprecated
	State getByClass(Class<?> clazz);
}
