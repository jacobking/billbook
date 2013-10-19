package org.account.entity;

import java.io.Serializable;

/**
 * 账户实体类
 * 
 * JacobKing 2013-10-16 下午4:03:38
 */
@SuppressWarnings("serial")
public class Account implements Serializable {
	private String id;
	private String type;
	private String userName;
	private String pwd;
	private String url;
	private String timestamp;

	public Account(String type, String timestamp) {
		this.type = type;
		this.timestamp = timestamp;
	}

	public Account(String id, String type, String username, String url,
			String timestamp) {
		this.id = id;
		this.type = type;
		this.userName = username;
		this.url = url;
		this.timestamp = timestamp;
	}

	public Account(String type, String username, String pwd, String url) {
		this.type = type;
		this.userName = username;
		this.pwd = pwd;
		this.url = url;
	}

	public Account(String id, String type, String username, String pwd,
			String url, String timestamp) {
		this.id = id;
		this.type = type;
		this.userName = username;
		this.pwd = pwd;
		this.url = url;
		this.timestamp = timestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String name) {
		this.userName = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String name) {
		this.type = name;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
