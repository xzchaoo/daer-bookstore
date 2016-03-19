package org.xzc.sshb.dao;

import org.xzc.sshb.domain.State;

/**
 * 只要一个驱壳就行了
 * 
 * @author xzchaoo
 * 
 */
public interface IStateDao {
	State get(int id);
}
