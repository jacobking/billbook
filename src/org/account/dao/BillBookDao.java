package org.account.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.base.exception.ExceptionUtils;
import org.base.result.ResultMap;

public interface BillBookDao<T> {
	/**
	 * 创建表
	 * 
	 * @param tbName
	 *            表名
	 * @param sql
	 *            创建表的sql语句
	 * @param log
	 *            日志对象
	 * @return true操作成功，false操作失败
	 */
	ResultMap createTable(String tbName, String sql, Logger log)
			throws ExceptionUtils;

	/**
	 * 保存数据
	 * 
	 * @param t
	 *            实体对象
	 * @return 结果集
	 */
	ResultMap saveOrUpdate(T t) throws ExceptionUtils;

	/**
	 * 批量保存
	 * 
	 * @param tList
	 *            实体对象列表
	 * @return 结果集
	 */
	ResultMap saveBatch(List<T> tList) throws ExceptionUtils;

	/**
	 * 删除数据
	 * 
	 * @param condition
	 *            删除条件
	 * @return 结果集
	 */
	ResultMap remove(Map<String, Object> condition) throws ExceptionUtils;

	/**
	 * 根据条件查询对象信息
	 * 
	 * @param condition
	 *            查询条件
	 * @return 结果集
	 */
	ResultMap search(Map<String, Object> condition);

	/**
	 * 根据条件查询对象是否存在
	 * 
	 * @param condition
	 *            查询条件
	 * @return 结果集
	 */
	ResultMap isExists(Map<String, Object> condition);

	/**
	 * 初始化表数据
	 * 
	 * @return 结果集
	 */
	ResultMap initData();

}
