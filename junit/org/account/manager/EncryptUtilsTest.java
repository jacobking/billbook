package org.account.manager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.account.entity.Account;
import org.account.service.AccountService;
import org.base.db.DBUtils;
import org.base.security.EncryptUtil;
import org.base.security.SecurityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EncryptUtilsTest {
	private PreparedStatement ps = null;
	private Connection conn = null;
	private ResultSet rs = null;
	private List<Account> aList = null;
	private AccountService as;

	@Before
	public void init() {
		conn = DBUtils.getInstance().getConn();
		as = new AccountService();
		aList = new ArrayList<Account>();
	}

	@Test
	public void encrypt() {
		byte[] bt = EncryptUtil.Encrypt("hello,world!!@$%^%&&^()_|");
		System.out.println(bt);
		bt = "[B@2c79809".getBytes();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try {
			bout.write(bt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String words = EncryptUtil.Decrypt(bout);
		System.out.println(words);
	}

	@Test
	public void loadHisData() {
		try {
			ps = conn
					.prepareStatement("select name,aid,pwd,url from t_sys_account ");
			rs = ps.executeQuery();
			// aList = new ArrayList<Account>();
			while (rs.next()) {
				// aList.add(new Account(rs.getString(1), rs.getString(2), rs
				// .getString(3), rs.getString(4)));
				System.out.println(rs.getString(1) + "," + rs.getString(2)
						+ "," + rs.getString(3) + "," + rs.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void initUser() {
		try {
			Statement stat = conn.createStatement();
			stat.executeUpdate("drop table if exists user;");
			stat.executeUpdate("create table user (id varchar(32) primary key not null,username varchar(32),pwd varchar(32));");
			ps = conn.prepareStatement("insert into user values(?,?,?)");
			ps.setString(1, "1");
			ps.setString(2, "admin");
			ps.setString(3, SecurityUtils.md5("admin"));
			ps.execute();
			stat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void initAccount() {
		try {
			Statement stat = conn.createStatement();
			stat.executeUpdate("drop table if exists account;");
			stat.executeUpdate("create table account (id varchar(32) primary key not null,type varchar(32),username varchar(32),url varchar(200),timestamp varchar(32));");
			stat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void saveData() {
		try {
			initAccount();
			// loadHisData();
			reader();
			if (as.saveBatchAccount(aList)) {
				System.out.println("save over!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从文件读取账户信息
	 */
	private void reader() {
		try {
			File in = new File("f://pwd.txt");
			BufferedReader br = new BufferedReader(new FileReader(in));
			String str = "";
			while ((str = br.readLine()) != null) {
				String[] s = str.split(",");
				// System.out.println(s.length);
				aList.add(new Account(s[0], s[1], s[2], s[3]));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void isExists() {
		try {
			ps = conn
					.prepareStatement("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='account';");
			rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getInt(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void destory() {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			DBUtils.getInstance().closeConn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
