package org.account.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.account.dao.BillBookDao;
import org.account.entity.User;
import org.apache.log4j.Logger;
import org.base.dao.BaseDao;
import org.base.result.ResultMap;
import org.base.security.SecurityUtils;

public class UserDaoImpl extends BaseDao implements BillBookDao<User> {
	private static Logger log = Logger.getLogger(UserDaoImpl.class);

	@Override
	public ResultMap saveBatch(List<User> aList) {
		try {
			if (aList == null)
				return new ResultMap(102, "数据不得为空！");
			initConn(log);
			ps = conn.prepareStatement("select max(id) from user ");
			rs = ps.executeQuery();
			int maxid = 0;
			while (rs.next()) {
				maxid = rs.getInt(1);
			}
			ps = conn.prepareStatement("insert into user values (?,?,?);");
			conn.setAutoCommit(false);
			int size = aList.size();
			for (int i = 0; i < size; i++) {
				maxid++;
				User c = aList.get(i);
				ps.setString(1, maxid + "");
				ps.setString(2, c.getUserName());
				ps.setString(3, SecurityUtils.md5(c.getPwd()));
				ps.addBatch();
			}
			ps.executeBatch();
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
	public ResultMap saveOrUpdate(User c) {
		try {
			if (c == null)
				return new ResultMap(102, "数据不得为空！");
			initConn(log);
			conn.setAutoCommit(false);
			if (c.getId() != null) {
				ps = conn
						.prepareStatement("update user  set pwd =? where id=?");
				ps.setString(1, SecurityUtils.md5(c.getPwd()));
				ps.setString(2, c.getId());
			} else {
				ps = conn.prepareStatement("select max(id) from user ");
				rs = ps.executeQuery();
				int maxid = 0;
				while (rs.next()) {
					maxid = rs.getInt(1);
				}
				ps = conn.prepareStatement("insert into user values (?,?,?);");
				ps.setString(1, (maxid + 1) + "");
				ps.setString(2, c.getUserName());
				ps.setString(3, SecurityUtils.md5(c.getPwd()));
			}
			ps.executeUpdate();
			conn.setAutoCommit(true);
			return new ResultMap(100);
		} catch (Exception e) {
			e.printStackTrace();
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
					"select id,username,pwd from user where 1=1");
			if (condition != null) {
				if (null != condition.get("id")) {
					sql.append(" and id in (" + condition.get("id") + ")");
				}
				if (null != condition.get("username")) {
					sql.append(" and username like '%"
							+ condition.get("username") + "%'");
				}
			}
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			List uList = new ArrayList<User>();
			while (rs.next()) {
				uList.add(new User(rs.getString(1), rs.getString(2), rs
						.getString(3)));
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
					"select id,username,pwd from user where 1=1");
			if (condition != null) {

				if (null != condition.get("id")) {
					sql.append(" and id in (" + condition.get("id") + ")");
				}
				if (null != condition.get("username")) {
					sql.append(" and username like '%"
							+ condition.get("username") + "%'");
				}
			}
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			List uList = new ArrayList<User>();
			while (rs.next()) {
				uList.add(new User(rs.getString(1), rs.getString(2), rs
						.getString(3)));
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
				return new ResultMap(102, "用户编号不得为空！");
			initConn(log);
			conn.setAutoCommit(false);
			// 删除数据库记录
			ps = conn.prepareStatement("delete from user where id in ("
					+ condition.get("id") + ")");
			ps.execute();
			conn.setAutoCommit(true);
			return new ResultMap(100);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMap(103, e.getMessage());
		} finally {
			destoryConn(log);
		}
	}

	@Override
	public ResultMap initData() {
		try {
			String sql = "create table user (id varchar(32) primary key not null,username varchar(32),pwd varchar(32));";
			ResultMap rm = createTable("user", sql, log);
			if (!rm.isError()) {
				User u = new User("1", "admin", "admin");
				return saveOrUpdate(u);
			}
			return rm;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMap(103, e.getMessage());
		}
	}
}
