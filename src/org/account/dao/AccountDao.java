package org.account.dao;

import java.util.List;

import org.account.entity.Account;

public interface AccountDao {

	/**
	 * 保存数据
	 * 
	 * @param c
	 *            实体对象
	 * @return true操作成功，false操作失败
	 */
	boolean save(Account c);

	/**
	 * 批量保存
	 * 
	 * @param aList
	 *            实体对象列表
	 * @return true操作成功，false操作失败
	 */
	boolean save(List<Account> aList);

	/**
	 * 创建表
	 * 
	 * @param tbName
	 *            表名
	 * @param sql
	 *            创建表的sql语句
	 * @return true操作成功，false操作失败
	 */
	boolean createTable(String tbName, String sql);

}
