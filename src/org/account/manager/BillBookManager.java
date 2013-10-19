package org.account.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.account.entity.Account;
import org.account.entity.User;
import org.account.service.AccountService;
import org.account.service.UserService;
import org.base.security.EncryptUtil;

/**
 * 账户管理
 * 
 * JacobKing 2013-10-16 下午4:03:52
 */
public class BillBookManager {
	private AccountService as = new AccountService();
	private UserService us = new UserService();
	private Map<String, Object> condition;
	private Account ac;
	private User u;
	private BufferedReader br;

	public void run() {
		System.out
				.println("**********************欢迎进入账户管理系统**********************");
		try {
			initData();
			br = new BufferedReader(new InputStreamReader(System.in, "gbk"));
			// 登录提示
			loginTips();
			int code = 0;
			// 登录
			while ((code = login()) > 0) {
				if (code == 1) {
					loginPwdTips();
				} else {
					loginTips();
				}
			}
			// 菜单操作
			menuTips();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		as.initAccountData();
		us.initUserData();
	}

	/**
	 * 菜单选择
	 */
	private void menuTips() {
		// 菜单列表
		System.out.println("********功能菜单********");
		StringBuffer menu = new StringBuffer();
		menu.append("1:新增账户\t");
		menu.append("2:查询账户\t");
		menu.append("3:新增用户\t");
		menu.append("4:查询用户\t");
		menu.append("0:退出系统\t");
		menu.append("-1:主菜单(通用)");
		try {
			boolean flag = true;
			do {
				System.out.println("*****请选择操作类型*****");
				System.out.println(menu);
				String targets = br.readLine();
				if (targets.matches("[0-9]+")) {
					int target = Integer.parseInt(targets);
					boolean flag2 = true;
					do {
						switch (target) {
						case -1:
							flag2 = false;
							break;
						case 0:
							System.exit(0);
							flag2 = false;
							flag = false;
							break;
						case 1:
							if (addAccountTips()) {
								target = 2;
							}
							break;
						case 2:
							searchAccount(null, null);
							StringBuffer menu2 = new StringBuffer();
							System.out.println("***请选择操作类型***");
							menu2.append("1:修改\t");
							menu2.append("2:查看\t");
							menu2.append("3:删除（批量用逗号分隔）\t");
							menu2.append("4:返回上一步\t");
							System.out.println(menu2);
							String er = br.readLine();
							boolean b2 = true;
							do {
								if (er.matches("[0-9]+")) {
									int ier = Integer.parseInt(er);
									switch (ier) {
									case 1:
										if (modifyAccountTips()) {
											b2 = false;
											// flag2 = false;
										}
										break;
									case 2:
										detailAccountTips();
										b2 = false;
										flag2 = false;
										break;
									case 3:
										if (delAccountTips()) {
											b2 = false;
											flag2 = false;
										}
										break;
									case 4:
										b2 = false;
										flag2 = false;
										break;
									default:
										System.out.println("无效指令！");
										break;
									}
								}
							} while (b2);
							break;
						case 3:
							if (addUserTips()) {
								flag2 = false;
							}
							break;
						case 4:
							searchUser(null);
							StringBuffer menu4 = new StringBuffer();
							System.out.println("***请选择操作类型***");
							menu4.append("1:新增\t");
							menu4.append("2:重置密码\t");
							menu4.append("3:删除（批量用逗号分隔）\t");
							menu4.append("4:返回上一步\t");
							System.out.println(menu4);
							String er4 = br.readLine();
							boolean b4 = true;
							do {
								if (er4.matches("[0-9]+")) {
									int ier = Integer.parseInt(er4);
									switch (ier) {
									case 1:
										if (addUserTips()) {
											b4 = false;
										}
										break;
									case 2:
										if (modifyUserTips()) {
											b4 = false;
										}
										break;
									case 3:
										if (delUserTips()) {
											b4 = false;
											flag2 = false;
										}
										break;
									case 4:
										b4 = false;
										flag2 = false;
										break;
									default:
										System.out.println("无效指令！");
										break;
									}
								}
							} while (b4);
							break;
						default:
							System.out.println("无效指令！");
							flag2 = false;
							break;
						}
					} while (flag2);
				}
			} while (flag);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 新增账户
	 * 
	 */
	private boolean addAccountTips() {
		try {
			String type = "";
			System.out.println("提示：【*：必填】");
			do {
				System.out.println("——————请输入账户类型——————(*)");
				type = br.readLine();
			} while (type == null || type.trim().length() == 0);
			if (exit(type))
				return true;
			String username = "";
			do {
				System.out.println("——————请输入账号——————(*)");
				username = br.readLine();
			} while (username == null || username.trim().length() == 0);
			if (exit(username))
				return true;
			String pwd = "";
			do {
				System.out.println("——————请输入密码——————(*)");
				pwd = br.readLine();
			} while (pwd == null || pwd.trim().length() == 0);
			if (exit(pwd))
				return true;
			System.out.println("——————请输入URL——————");
			String url = br.readLine();
			if (exit(url))
				return true;
			ac = new Account(type, username, pwd, url);
			return saveOrUpdateAccount();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 快速退出
	 * 
	 * @param s
	 *            字符(-1退出)
	 * @return true正确退出，false强制退出
	 */
	private boolean exit(String s) {
		if (s != null && s.length() > 0 && "-1".equals(s))
			return true;
		return false;
	}

	/**
	 * 修改账户
	 * 
	 */
	private boolean modifyAccountTips() {
		try {
			String id = "";
			condition = new HashMap<String, Object>();
			System.out.println("提示：【*：必填，不填则不做修改】");
			do {
				System.out.println("——————请输入账户编号——————(*)");
				id = br.readLine();
				if (exit(id))
					return true;
				if (id != null && id.trim().length() > 0) {
					id = "'" + id + "'";
					condition.put("id", id);
					ac = as.isExists(condition);
				}
				if (ac == null)
					System.out.println("该账户编号不存在！请确认后重新输入！");
			} while (id == null || id.trim().length() == 0 || ac == null);
			System.out.println("——————请输入账户类型——————");
			String type = br.readLine();
			if (type != null && type.trim().length() > 0) {
				ac.setType(type);
			}
			if (exit(type))
				return true;
			System.out.println("——————请输入账号——————");
			String username = br.readLine();
			if (username != null && username.trim().length() > 0) {
				ac.setUserName(username);
			}
			if (exit(username))
				return true;
			System.out.println("——————请输入密码——————");
			String pwd = br.readLine();
			if (pwd != null && pwd.trim().length() > 0) {
				ac.setPwd(pwd);
			}
			if (exit(pwd))
				return true;
			System.out.println("——————请输入URL——————");
			String url = br.readLine();
			if (url != null && url.trim().length() > 0) {
				ac.setUrl(url);
			}
			if (exit(url))
				return true;
			return saveOrUpdateAccount();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查看账户
	 * 
	 */
	private boolean detailAccountTips() {
		try {
			String id = "";
			condition = new HashMap<String, Object>();
			do {
				System.out.println("——————请输入账户编号——————");
				id = br.readLine();
				if (exit(id))
					return true;
				if (id != null && id.trim().length() > 0) {
					condition.put("id", id);
					ac = as.isExists(condition);
				}
				if (ac == null)
					System.out.println("该编号不存在！请确认后重新输入！");
			} while (id == null || id.trim().length() == 0 || ac == null);
			System.out.println("账户编号\t账户类型\t用户名\t链接\t操作时间\t密码");
			printAc(ac, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除账户
	 * 
	 */
	private boolean delAccountTips() {
		try {
			String id = "";
			condition = new HashMap<String, Object>();
			do {
				System.out.println("——————请输入账户编号——————");
				id = br.readLine();
				if (exit(id))
					return true;
				if (id != null && id.trim().length() > 0) {
					if (id.indexOf(",") > 0) {
						String[] ids = id.split(",");
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i < ids.length; i++) {
							if (i > 0)
								sb.append(",");
							sb.append(String.format("%s", ids[i]));
						}
						id = sb.toString();
					}
					condition.put("id", id);
					ac = as.isExists(condition);
				}
				if (ac == null)
					System.out.println("该编号不存在！请确认后重新输入！");
			} while (id == null || id.trim().length() == 0 || ac == null);
			return delAccount(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查询账户对应的密码
	 * 
	 * @param field
	 *            查询字段
	 * @param keyWorkds
	 *            关键字
	 * @return 密码
	 */
	public void searchAccount(String field, String keyWords) {
		condition = new HashMap<String, Object>();
		condition.put(field, keyWords);
		List<Account> aList = as.findAccount(condition);
		if (aList != null) {
			int size = aList.size();
			System.out.println("账户编号\t账户类型\t用户名\t链接\t操作时间");
			for (int i = 0; i < size; i++) {
				printAc(aList.get(i), false);
			}
		}
	}

	/**
	 * 新增或修改账户信息
	 * 
	 * @param ac
	 */
	public boolean saveOrUpdateAccount() {
		if (as.saveOrUpdateAccount(ac)) {
			System.out.println("操作成功！");
			return true;
		}
		return false;
	}

	/**
	 * 根据账户编号删除账户信息及文件
	 * 
	 * @param id
	 *            账户编号
	 */
	public boolean delAccount(String id) {
		condition = new HashMap<String, Object>();
		condition.put("id", id);
		if (as.delAccount(condition)) {
			System.out.println("操作成功！");
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 根据账户名称验证该账户是否存在
	 * 
	 * @param ac
	 *            账户对象
	 * @return true存在，false不存在
	 */
	public boolean isAccountExists(Account a) {
		condition = new HashMap<String, Object>();
		if (a == null) {
			System.out.println("账户信息不能为空！");
			return false;
		}
		if (a.getType() == null) {
			System.out.println("账户类型不能为空！");
			return false;
		} else {
			condition.put("type", a.getType());
		}
		if (a.getUserName() == null) {
			System.out.println("账户名不能为空！");
			return false;
		} else {
			condition.put("username", a.getUserName());
		}
		ac = as.isExists(condition);
		return ac != null;
	}

	/**
	 * 用户登录验证
	 * 
	 * @return true验证通过，false验证失败
	 */
	public boolean loginValidate() {
		if (u == null) {
			System.out.println("用户信息不能为空！");
			return false;
		}
		if (u.getUserName() == null) {
			System.out.println("用户名不能为空！");
			return false;
		}
		if (u.getPwd() == null) {
			System.out.println("密码不能为空！");
			return false;
		}
		return true;
	}

	/**
	 * 用户登录
	 * 
	 * @return true登录成功，false登录失败
	 */
	public int login() {
		if (loginValidate()) {
			int code = us.login(u);
			if (code == 0)
				System.out.println("登录成功!欢迎您：" + u.getUserName());
			return code;
		}
		return 2;
	}

	/**
	 * 新增用户
	 * 
	 * @return 操作成功，操作失败
	 */
	public boolean saveOrUpdateUser() {
		if (us.addUser(u)) {
			System.out.println("操作成功！");
			return true;
		}
		return false;
	}

	/**
	 * 查询用户信息
	 * 
	 * @param keyWorkds
	 *            关键字
	 */
	public void searchUser(String keyWords) {
		condition = new HashMap<String, Object>();
		condition.put("username", keyWords);
		List<User> aList = us.findUser(condition);
		if (aList != null) {
			System.out.println("用户编号\t用户名");
			int size = aList.size();
			for (int i = 0; i < size; i++) {
				printU(aList.get(i));
			}
		}
	}

	/**
	 * 根据用户编号删除用户信息
	 * 
	 * @param id
	 *            用户编号
	 */
	public boolean delUser(String id) {
		condition = new HashMap<String, Object>();
		condition.put("id", id);
		if (us.delUser(condition)) {
			System.out.println("操作成功！");
			return true;
		}
		return false;
	}

	/**
	 * 登录提示
	 */
	private void loginTips() {
		try {
			System.out.println("*********用户登录**********");
			u = new User();
			loginUsernameTips();
			loginPwdTips();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用户名输入提示
	 */
	private void loginUsernameTips() {
		try {
			String username = "";
			do {
				System.out.println("——————请输入用户名——————");
				username = br.readLine();
			} while (username == null || username.trim().length() == 0);
			u.setUserName(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 密码输入提示
	 */
	private void loginPwdTips() {
		try {
			String pwd = "";
			do {
				System.out.println("——————请输入密码——————");
				pwd = br.readLine();
			} while (pwd == null || pwd.trim().length() == 0);
			u.setPwd(pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 新增用户
	 * 
	 */
	private boolean addUserTips() {
		try {
			System.out.println("提示：【*：必填】");
			String username = "";
			do {
				System.out.println("——————请输入用户名——————(*)");
				username = br.readLine();
			} while (username == null || username.trim().length() == 0);
			if (exit(username))
				return true;
			String pwd = "";
			do {
				System.out.println("——————请输入密码——————(*)");
				pwd = br.readLine();
			} while (pwd == null || pwd.trim().length() == 0);
			if (exit(pwd))
				return true;
			u = new User(null, username, pwd);
			return saveOrUpdateUser();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 修改密码
	 * 
	 */
	private boolean modifyUserTips() {
		try {
			String id;
			condition = new HashMap<String, Object>();
			System.out.println("提示：【*：必填，不填则不做修改】");
			do {
				System.out.println("——————请输入用户编号——————(*)");
				id = br.readLine();
				if (exit(id))
					return true;
				if (id != null && id.trim().length() > 0) {
					id = "'" + id + "'";
					condition.put("id", id);
					u = us.isExists(condition);
				}
				if (u == null)
					System.out.println("该用户编号不存在！请确认后重新输入！");
			} while (id == null || id.trim().length() == 0 || u == null);
			String pwd;
			do {
				System.out.println("——————请输入密码——————(*)");
				pwd = br.readLine();
				if (exit(pwd))
					return true;
			} while (pwd == null || pwd.trim().length() == 0);
			u.setPwd(pwd);
			return saveOrUpdateUser();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除用户信息
	 * 
	 */
	private boolean delUserTips() {
		try {
			String id = "";
			condition = new HashMap<String, Object>();
			do {
				System.out.println("——————请输入用户编号——————");
				id = br.readLine();
				if (exit(id))
					return true;
				if (id != null && id.trim().length() > 0) {
					if (id.indexOf(",") > 0) {
						String[] ids = id.split(",");
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i < ids.length; i++) {
							if (i > 0)
								sb.append(",");
							sb.append(String.format("%s", ids[i]));
						}
						id = sb.toString();
					}
					condition.put("id", id);
					u = us.isExists(condition);
				}
				if (us == null)
					System.out.println("该用户编号不存在！请确认后重新输入！");
			} while (id == null || id.trim().length() == 0 || us == null);
			return delUser(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 打印账户信息
	 * 
	 * @param bo
	 *            true显示密码，false不显示
	 */
	private void printAc(Account ac, boolean bo) {
		System.out.print(ac.getId() + "\t" + ac.getType() + "\t"
				+ ac.getUserName() + "\t" + ac.getUrl() + "\t"
				+ ac.getTimestamp());
		if (bo) {
			System.out.println("\t"
					+ EncryptUtil.DecryptFromFile(ac.getType() + "_"
							+ ac.getTimestamp()));
		} else {
			System.out.println();
		}
	}

	/**
	 * 打印用户信息
	 * 
	 * @param bo
	 *            true显示密码，false不显示
	 */
	private void printU(User u) {
		System.out.println(u.getId() + "\t" + u.getUserName());
	}
}
