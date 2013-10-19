package org.account.service;

import java.util.List;
import java.util.Map;

import org.account.dao.BillBookDao;
import org.account.dao.impl.AccountDaoImpl;
import org.account.entity.Account;
import org.base.exception.ExceptionUtils;
import org.base.result.ResultMap;

public class AccountService {
	private BillBookDao<Account> ad = new AccountDaoImpl();
	private ResultMap result;

	/**
	 * 验证该信息是否存在
	 */
	public Account isExists(Map<String, Object> condition) {
		result = ad.isExists(condition);
		if (result.isEmpty())
			return null;
		return (Account) result.getObj();
	}

	/**
	 * 查询账户信息
	 * 
	 * @param condition
	 *            查询条件
	 * @return 密码
	 */
	@SuppressWarnings("unchecked")
	public List<Account> findAccount(Map<String, Object> condition) {
		result = ad.search(condition);
		if (!result.isError())
			return result.getList();
		System.out.println(result.getMsg());
		return null;
	}

	/**
	 * 根据账户编号删除账户信息及文件
	 * 
	 * @param condition
	 *            账户编号
	 * @return true操作成功，false操作失败
	 */
	public boolean delAccount(Map<String, Object> condition) {
		try {
			result = ad.remove(condition);
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
	 * 新增或修改账户信息
	 * 
	 * @param ac
	 *            账户信息
	 * @return true操作成功，false操作失败
	 */
	public boolean saveOrUpdateAccount(Account ac) {
		try {
			result = ad.saveOrUpdate(ac);
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
	 * 批量添加账户信息
	 * 
	 * @param aList
	 *            账户列表信息
	 * @return true操作成功，false操作失败
	 */
	public boolean saveBatchAccount(List<Account> aList) {
		try {
			result = ad.saveBatch(aList);
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
	public void initAccountData() {
		System.out.println("~~~~~开始初始化账户数据~~~~~");
		if (ad.initData().isError()) {
			System.out.println("~~~~~账户数据初始化失败~~~~~");
			return;
		}
		System.out.println("~~~~~账户数据初始化完毕~~~~~");
	}

}
