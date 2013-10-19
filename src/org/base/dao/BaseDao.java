package org.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.base.db.DBUtils;
import org.base.result.ResultMap;

public abstract class BaseDao {
	Logger log = Logger.getLogger(BaseDao.class);
	public PreparedStatement ps = null;
	public Connection conn = null;
	public ResultSet rs = null;

	/**
	 * 初始化连接
	 */
	public void initConn(Logger log) {
		conn = DBUtils.getInstance().getConn();
	}

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
	public ResultMap createTable(String tbName, String sql, Logger log) {
		try {
			initConn(log);
			ps = conn
					.prepareStatement(String
							.format("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='%s';",
									tbName));
			rs = ps.executeQuery();
			int count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			// 当表不存在时执行
			if (count == 0) {
				ps = conn.prepareStatement(sql);
				ps.execute();
			}
			return new ResultMap(100);
		} catch (Exception e) {
			log.error(e);
			return new ResultMap(103, e.getMessage());
		} finally {
			destoryConn(log);
		}
	}

	/**
	 * 关闭连接
	 */
	protected void destoryConn(Logger log) {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			log.error("关闭连接失败：" + e);
		}
	}
}
