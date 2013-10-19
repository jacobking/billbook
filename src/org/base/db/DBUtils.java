package org.base.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.base.property.PropertyUtils;
import org.base.security.EncryptUtil;

/**
 * 数据连接工具类
 * 
 * JacobKing 2013-10-16 下午4:09:53
 */
public class DBUtils {
	private static Logger log = Logger.getLogger(EncryptUtil.class);
	private static Connection con = null;
	private String ver;
	private String driver;
	private String url;
	private String username;
	private String password;

	public DBUtils() {
		readConfig();
	}

	public static DBUtils getInstance() {
		return new DBUtils();
	}

	/**
	 * 从配置文件中读取连接数据库参数
	 */
	private void readConfig() {
		PropertyUtils p = new PropertyUtils();
		ver = p.getProperty("ver");
		driver = p.getProperty("driver");
		url = p.getProperty("url");
		username = p.getProperty("username");
		password = p.getProperty("password");
	}

	/**
	 * 获取连接
	 * 
	 * @return Connection
	 * @throws Exception
	 */
	public Connection getConn() {
		try {
			// if (con != null)
			// return con;
			Class.forName(driver);
			// 非SQLite版本
			if (!"sqlite".equalsIgnoreCase(ver)) {
				con = DriverManager.getConnection(url, username, password);
			} else {
				// SQLite版本
				con = DriverManager.getConnection(url);
			}
		} catch (Exception e) {
			log.error("获取连接失败：" + e);
		}
		return con;
	}

	/**
	 * 关闭连接
	 */
	public void closeConn() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				log.error("关闭连接失败：" + e);
			}
		}
	}

}
