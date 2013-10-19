package org.account.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.account.dao.BillBookDao;
import org.account.entity.Account;
import org.apache.log4j.Logger;
import org.base.dao.BaseDao;
import org.base.date.DateUtils;
import org.base.io.FileUtils;
import org.base.property.PropertyUtils;
import org.base.result.ResultMap;
import org.base.security.EncryptUtil;

public class AccountDaoImpl extends BaseDao implements BillBookDao<Account> {
	private Logger log = Logger.getLogger(AccountDaoImpl.class);

	@Override
	public ResultMap saveBatch(List<Account> aList) {
		try {
			if (aList == null)
				return new ResultMap(102, "数据不得为空！");
			initConn(log);
			// 获取最大编号
			ps = conn.prepareStatement("select max(id) from account ");
			rs = ps.executeQuery();
			int maxid = 0;
			while (rs.next()) {
				maxid = rs.getInt(1);
			}
			// 批量添加账户信息
			ps = conn
					.prepareStatement("insert into account values (?,?,?,?,?);");
			conn.setAutoCommit(false);
			int size = aList.size();
			for (int i = 0; i < size; i++) {
				// 编号递增
				maxid++;
				Account c = aList.get(i);
				ps.setString(1, maxid + "");
				ps.setString(2, c.getType());
				ps.setString(3, c.getUserName());
				ps.setString(4, c.getUrl());
				String timestamp = DateUtils.getNowTime(2);
				ps.setString(5, timestamp);
				aList.get(i).setTimestamp(timestamp);
				ps.addBatch();
			}
			ps.executeBatch();
			for (int i = 0; i < size; i++) {
				Account c = aList.get(i);
				EncryptUtil.Encrypt2File(c.getType() + "_" + c.getTimestamp(),
						c.getPwd());
			}
			conn.setAutoCommit(true);
			return new ResultMap(100);
		} catch (Exception e) {
			log.error(e);
			return new ResultMap(103, e.getMessage());
		} finally {
			destoryConn(log);
		}
	}

	@Override
	public ResultMap saveOrUpdate(Account c) {
		try {
			if (c == null)
				return new ResultMap(102, "数据不得为空！");
			initConn(log);
			conn.setAutoCommit(false);
			String timestamp = DateUtils.getNowTime(2);
			if (c.getId() != null) {
				ps = conn
						.prepareStatement("select type,timestamp from account where id=?");
				ps.setString(1, c.getId());
				rs = ps.executeQuery();
				// 不存在直接返回
				if (!rs.next())
					return new ResultMap(105, "该记录已被删除！");
				Account a = new Account(rs.getString(1), rs.getString(2));
				// 删除历史加密文件
				PropertyUtils pu = new PropertyUtils();
				FileUtils.removeFile(pu.getProperty("filepath") + ""
						+ a.getType() + "_" + a.getTimestamp() + "."
						+ pu.getProperty("keySuffix"));
				FileUtils.removeFile(pu.getProperty("filepath") + ""
						+ a.getType() + "_" + a.getTimestamp() + "."
						+ pu.getProperty("dataSuffix"));
				// 更新
				ps = conn
						.prepareStatement("update account set type=? ,username=? ,url=?,timestamp=? where id=?");
				ps.setString(1, c.getType());
				ps.setString(2, c.getUserName());
				ps.setString(3, c.getUrl());
				ps.setString(4, timestamp);
				ps.setString(5, c.getId());
			} else {
				// 新增
				ps = conn.prepareStatement("select max(id) from account ");
				rs = ps.executeQuery();
				int maxid = 0;
				while (rs.next()) {
					maxid = rs.getInt(1);
				}
				ps = conn
						.prepareStatement("insert into account values (?,?,?,?,?);");
				ps.setString(1, (maxid + 1) + "");
				ps.setString(2, c.getType());
				ps.setString(3, c.getUserName());
				ps.setString(4, c.getUrl());
				ps.setString(5, timestamp);
			}
			ps.execute();
			// 创建加密文件
			EncryptUtil.Encrypt2File(c.getType() + "_" + timestamp, c.getPwd());
			conn.setAutoCommit(true);
			return new ResultMap(100);
		} catch (Exception e) {
			log.error(e);
			return new ResultMap(103, e.getMessage());
		} finally {
			destoryConn(log);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ResultMap search(Map<String, Object> condition) {
		try {
			initConn(log);
			StringBuffer sql = new StringBuffer(
					"select id,type,username,url,timestamp from account where 1=1");
			if (condition != null) {
				if (null != condition.get("id")) {
					sql.append(String.format(" and id ='%s'",
							condition.get("id")));
				}
				if (null != condition.get("ids")) {
					sql.append(" and id in (" + condition.get("ids") + ")");
				}
				if (null != condition.get("type")) {
					sql.append(" and type like '%" + condition.get("type")
							+ "%'");
				}
				if (null != condition.get("username")) {
					sql.append(" and username like '%"
							+ condition.get("username") + "%'");
				}
				if (null != condition.get("url")) {
					sql.append(" and url like '%" + condition.get("url") + "%'");
				}
			}
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			List uList = new ArrayList<Account>();
			while (rs.next()) {
				uList.add(new Account(rs.getString(1), rs.getString(2), rs
						.getString(3), rs.getString(4), rs.getString(5)));
			}
			if (uList.isEmpty())
				return new ResultMap(104, "无查询结果！");
			return new ResultMap(100, uList);
		} catch (Exception e) {
			log.error(e);
			return new ResultMap(103, e.getMessage());
		} finally {
			destoryConn(log);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ResultMap isExists(Map<String, Object> condition) {
		try {
			initConn(log);
			StringBuffer sql = new StringBuffer(
					"select id,type,username,url,timestamp from account where 1=1");
			if (condition != null) {
				if (null != condition.get("id")) {
					sql.append(" and id in (" + condition.get("id") + ")");
				}
				if (null != condition.get("type")) {
					sql.append(" and type ='" + condition.get("type") + "'");
				}
				if (null != condition.get("username")) {
					sql.append(" and username = '" + condition.get("username")
							+ "'");
				}
				if (null != condition.get("url")) {
					sql.append(" and url = '" + condition.get("url") + "'");
				}
			}
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			List uList = new ArrayList<Account>();
			while (rs.next()) {
				uList.add(new Account(rs.getString(1), rs.getString(2), rs
						.getString(3), rs.getString(4), rs.getString(5)));
				break;
			}
			if (uList.isEmpty())
				return new ResultMap(104);
			return new ResultMap(100, uList.get(0));
		} catch (Exception e) {
			log.error(e);
			return new ResultMap(103, e.getMessage());
		} finally {
			destoryConn(log);
		}
	}

	@Override
	public ResultMap remove(Map<String, Object> condition) {
		try {
			if (condition == null)
				return new ResultMap(102, "删除条件不得为空！");
			if (condition.get("id") == null)
				return new ResultMap(102, "账户编号不得为空！");
			String id = condition.get("id").toString();
			initConn(log);
			// 根据编号查询该数据是否存在
			ps = conn
					.prepareStatement("select type,timestamp from account where id in("
							+ id + ")");
			rs = ps.executeQuery();
			boolean flag = true;
			while (rs.next()) {
				flag = false;
				break;
			}
			// 不存在返回
			if (flag)
				return new ResultMap(105);
			Account a = new Account(rs.getString(1), rs.getString(2));
			conn.setAutoCommit(false);
			// 删除数据库记录
			ps = conn.prepareStatement("delete from account where id in(" + id
					+ ")");
			ps.execute();
			PropertyUtils pu = new PropertyUtils();
			// 删除秘钥
			FileUtils.removeFile(pu.getProperty("filepath") + "" + a.getType()
					+ "_" + a.getTimestamp() + "."
					+ pu.getProperty("keySuffix"));
			// 删除加密文件
			FileUtils.removeFile(pu.getProperty("filepath") + "" + a.getType()
					+ "_" + a.getTimestamp() + "."
					+ pu.getProperty("dataSuffix"));
			conn.setAutoCommit(true);
			return new ResultMap(100);
		} catch (Exception e) {
			log.error(e);
			return new ResultMap(103, e.getMessage());
		} finally {
			destoryConn(log);
		}
	}

	@Override
	public ResultMap initData() {
		try {
			String sql = "create table account (id varchar(32) primary key not null,type varchar(32),username varchar(32),url varchar(200),timestamp varchar(32))";
			return createTable("account", sql, log);
		} catch (Exception e) {
			log.error(e);
			return new ResultMap(103, e.getMessage());
		}

	}
}
