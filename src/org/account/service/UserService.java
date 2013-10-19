package org.account.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.account.dao.BillBookDao;
import org.account.dao.impl.UserDaoImpl;
import org.account.entity.User;
import org.base.exception.ExceptionUtils;
import org.base.result.ResultMap;
import org.base.security.SecurityUtils;

public class UserService {
	private BillBookDao<User> ud = new UserDaoImpl();
	private ResultMap result;

	/**
	 * 用户登录
	 * 
	 * @param u
	 *            用户信息
	 * @return 0:登录成功；1:密码有误；2：用户名密码有误
	 */
	@SuppressWarnings("unchecked")
	public int login(User u) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("username", u.getUserName());
		condition.put("pwd", u.getPwd());
		result = ud.search(condition);
		if (result.isError()) {
			System.out.println("用户名或密码有误！");
			return 2;
		} else {
			List<User> uList = result.getList();
			User user = (User) uList.get(0);
			if (SecurityUtils.md5(u.getPwd()).equals(user.getPwd())) {
				return 0;
			} else if (SecurityUtils.md5(u.getPwd()).equalsIgnoreCase(
					user.getPwd())) {
				System.out.println("密码有误，请注意大小写！");
			} else {
				System.out.println("密码有误，请重新输入！");
			}
		}
		return 1;
	}

	/**
	 * 查询用户
	 * 
	 * @param condition
	 *            查询条件
	 * @return 用户列表
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUser(Map<String, Object> condition) {
		result = ud.search(condition);
		if (!result.isError())
			return result.getList();
		System.out.println(result.getMsg());
		return null;
	}

	/**
	 * 新增用户
	 * 
	 * @param u
	 *            用户信息
	 * @return true操作成功，false操作失败
	 */
	public boolean addUser(User u) {
		try {
			result = ud.saveOrUpdate(u);
			if (result.isError()) {
				System.out.println(result.getMsg());
				return false;
			}
		} catch (ExceptionUtils e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 修改用户信息
	 * 
	 * @param u
	 *            用户对象
	 * @return true操作成功，false操作失败
	 * @throws ExceptionUtils
	 */
	public boolean modifyUser(User u) {
		try {
			result = ud.saveOrUpdate(u);
			if (!result.isError()) {
				return true;
			}
			System.out.println(result.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 验证该信息是否存在
	 * 
	 * @param condition
	 *            用户编号
	 * @return 用户信息
	 */
	public User isExists(Map<String, Object> condition) {
		result = ud.isExists(condition);
		if (result.isEmpty())
			return null;
		return (User) result.getObj();
	}

	/**
	 * 根据用户编号删除用户信息
	 * 
	 * @param condition
	 *            用户编号（多个用逗号分隔）
	 * @return true操作成功，false操作失败
	 */
	public boolean delUser(Map<String, Object> condition) {
		try {
			result = ud.remove(condition);
			if (result.isError()) {
				System.out.println(result.getMsg());
				return false;
			}
		} catch (ExceptionUtils e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 初始化用户表数据
	 */
	public void initUserData() {
		System.out.println("~~~~~开始初始化用户数据~~~~~");
		if (ud.initData().isError()) {
			System.out.println("~~~~~用户数据初始化失败~~~~~");
			return;
		}
		System.out.println("~~~~~用户数据初始化完毕~~~~~");
	}
}
