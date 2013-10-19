package org.account.entity;

public class User {
	private String id;
	private String userName;
	private String pwd;

	public User() {

	}

	public User(String id, String userName) {
		this.id = id;
		this.userName = userName;
	}

	public User(String id, String userName, String pwd) {
		this.id = id;
		this.userName = userName;
		this.pwd = pwd;
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

	public void setUserName(String username) {
		this.userName = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
