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
 * �˻�����
 * 
 * JacobKing 2013-10-16 ����4:03:52
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
				.println("**********************��ӭ�����˻�����ϵͳ**********************");
		try {
			initData();
			br = new BufferedReader(new InputStreamReader(System.in, "gbk"));
			// ��¼��ʾ
			loginTips();
			int code = 0;
			// ��¼
			while ((code = login()) > 0) {
				if (code == 1) {
					loginPwdTips();
				} else {
					loginTips();
				}
			}
			// �˵�����
			menuTips();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		as.initAccountData();
		us.initUserData();
	}

	/**
	 * �˵�ѡ��
	 */
	private void menuTips() {
		// �˵��б�
		System.out.println("********���ܲ˵�********");
		StringBuffer menu = new StringBuffer();
		menu.append("1:�����˻�\t");
		menu.append("2:��ѯ�˻�\t");
		menu.append("3:�����û�\t");
		menu.append("4:��ѯ�û�\t");
		menu.append("0:�˳�ϵͳ\t");
		menu.append("-1:���˵�(ͨ��)");
		try {
			boolean flag = true;
			do {
				System.out.println("*****��ѡ���������*****");
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
							System.out.println("***��ѡ���������***");
							menu2.append("1:�޸�\t");
							menu2.append("2:�鿴\t");
							menu2.append("3:ɾ���������ö��ŷָ���\t");
							menu2.append("4:������һ��\t");
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
										System.out.println("��Чָ�");
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
							System.out.println("***��ѡ���������***");
							menu4.append("1:����\t");
							menu4.append("2:��������\t");
							menu4.append("3:ɾ���������ö��ŷָ���\t");
							menu4.append("4:������һ��\t");
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
										System.out.println("��Чָ�");
										break;
									}
								}
							} while (b4);
							break;
						default:
							System.out.println("��Чָ�");
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
	 * �����˻�
	 * 
	 */
	private boolean addAccountTips() {
		try {
			String type = "";
			System.out.println("��ʾ����*�����");
			do {
				System.out.println("�������������������˻����͡�����������(*)");
				type = br.readLine();
			} while (type == null || type.trim().length() == 0);
			if (exit(type))
				return true;
			String username = "";
			do {
				System.out.println("�������������������˺š�����������(*)");
				username = br.readLine();
			} while (username == null || username.trim().length() == 0);
			if (exit(username))
				return true;
			String pwd = "";
			do {
				System.out.println("���������������������롪����������(*)");
				pwd = br.readLine();
			} while (pwd == null || pwd.trim().length() == 0);
			if (exit(pwd))
				return true;
			System.out.println("������������������URL������������");
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
	 * �����˳�
	 * 
	 * @param s
	 *            �ַ�(-1�˳�)
	 * @return true��ȷ�˳���falseǿ���˳�
	 */
	private boolean exit(String s) {
		if (s != null && s.length() > 0 && "-1".equals(s))
			return true;
		return false;
	}

	/**
	 * �޸��˻�
	 * 
	 */
	private boolean modifyAccountTips() {
		try {
			String id = "";
			condition = new HashMap<String, Object>();
			System.out.println("��ʾ����*��������������޸ġ�");
			do {
				System.out.println("�������������������˻���š�����������(*)");
				id = br.readLine();
				if (exit(id))
					return true;
				if (id != null && id.trim().length() > 0) {
					id = "'" + id + "'";
					condition.put("id", id);
					ac = as.isExists(condition);
				}
				if (ac == null)
					System.out.println("���˻���Ų����ڣ���ȷ�Ϻ��������룡");
			} while (id == null || id.trim().length() == 0 || ac == null);
			System.out.println("�������������������˻����͡�����������");
			String type = br.readLine();
			if (type != null && type.trim().length() > 0) {
				ac.setType(type);
			}
			if (exit(type))
				return true;
			System.out.println("�������������������˺š�����������");
			String username = br.readLine();
			if (username != null && username.trim().length() > 0) {
				ac.setUserName(username);
			}
			if (exit(username))
				return true;
			System.out.println("���������������������롪����������");
			String pwd = br.readLine();
			if (pwd != null && pwd.trim().length() > 0) {
				ac.setPwd(pwd);
			}
			if (exit(pwd))
				return true;
			System.out.println("������������������URL������������");
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
	 * �鿴�˻�
	 * 
	 */
	private boolean detailAccountTips() {
		try {
			String id = "";
			condition = new HashMap<String, Object>();
			do {
				System.out.println("�������������������˻���š�����������");
				id = br.readLine();
				if (exit(id))
					return true;
				if (id != null && id.trim().length() > 0) {
					condition.put("id", id);
					ac = as.isExists(condition);
				}
				if (ac == null)
					System.out.println("�ñ�Ų����ڣ���ȷ�Ϻ��������룡");
			} while (id == null || id.trim().length() == 0 || ac == null);
			System.out.println("�˻����\t�˻�����\t�û���\t����\t����ʱ��\t����");
			printAc(ac, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ɾ���˻�
	 * 
	 */
	private boolean delAccountTips() {
		try {
			String id = "";
			condition = new HashMap<String, Object>();
			do {
				System.out.println("�������������������˻���š�����������");
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
					System.out.println("�ñ�Ų����ڣ���ȷ�Ϻ��������룡");
			} while (id == null || id.trim().length() == 0 || ac == null);
			return delAccount(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��ѯ�˻���Ӧ������
	 * 
	 * @param field
	 *            ��ѯ�ֶ�
	 * @param keyWorkds
	 *            �ؼ���
	 * @return ����
	 */
	public void searchAccount(String field, String keyWords) {
		condition = new HashMap<String, Object>();
		condition.put(field, keyWords);
		List<Account> aList = as.findAccount(condition);
		if (aList != null) {
			int size = aList.size();
			System.out.println("�˻����\t�˻�����\t�û���\t����\t����ʱ��");
			for (int i = 0; i < size; i++) {
				printAc(aList.get(i), false);
			}
		}
	}

	/**
	 * �������޸��˻���Ϣ
	 * 
	 * @param ac
	 */
	public boolean saveOrUpdateAccount() {
		if (as.saveOrUpdateAccount(ac)) {
			System.out.println("�����ɹ���");
			return true;
		}
		return false;
	}

	/**
	 * �����˻����ɾ���˻���Ϣ���ļ�
	 * 
	 * @param id
	 *            �˻����
	 */
	public boolean delAccount(String id) {
		condition = new HashMap<String, Object>();
		condition.put("id", id);
		if (as.delAccount(condition)) {
			System.out.println("�����ɹ���");
			return true;
		}
		return false;
	}

	/**
	 * 
	 * �����˻�������֤���˻��Ƿ����
	 * 
	 * @param ac
	 *            �˻�����
	 * @return true���ڣ�false������
	 */
	public boolean isAccountExists(Account a) {
		condition = new HashMap<String, Object>();
		if (a == null) {
			System.out.println("�˻���Ϣ����Ϊ�գ�");
			return false;
		}
		if (a.getType() == null) {
			System.out.println("�˻����Ͳ���Ϊ�գ�");
			return false;
		} else {
			condition.put("type", a.getType());
		}
		if (a.getUserName() == null) {
			System.out.println("�˻�������Ϊ�գ�");
			return false;
		} else {
			condition.put("username", a.getUserName());
		}
		ac = as.isExists(condition);
		return ac != null;
	}

	/**
	 * �û���¼��֤
	 * 
	 * @return true��֤ͨ����false��֤ʧ��
	 */
	public boolean loginValidate() {
		if (u == null) {
			System.out.println("�û���Ϣ����Ϊ�գ�");
			return false;
		}
		if (u.getUserName() == null) {
			System.out.println("�û�������Ϊ�գ�");
			return false;
		}
		if (u.getPwd() == null) {
			System.out.println("���벻��Ϊ�գ�");
			return false;
		}
		return true;
	}

	/**
	 * �û���¼
	 * 
	 * @return true��¼�ɹ���false��¼ʧ��
	 */
	public int login() {
		if (loginValidate()) {
			int code = us.login(u);
			if (code == 0)
				System.out.println("��¼�ɹ�!��ӭ����" + u.getUserName());
			return code;
		}
		return 2;
	}

	/**
	 * �����û�
	 * 
	 * @return �����ɹ�������ʧ��
	 */
	public boolean saveOrUpdateUser() {
		if (us.addUser(u)) {
			System.out.println("�����ɹ���");
			return true;
		}
		return false;
	}

	/**
	 * ��ѯ�û���Ϣ
	 * 
	 * @param keyWorkds
	 *            �ؼ���
	 */
	public void searchUser(String keyWords) {
		condition = new HashMap<String, Object>();
		condition.put("username", keyWords);
		List<User> aList = us.findUser(condition);
		if (aList != null) {
			System.out.println("�û����\t�û���");
			int size = aList.size();
			for (int i = 0; i < size; i++) {
				printU(aList.get(i));
			}
		}
	}

	/**
	 * �����û����ɾ���û���Ϣ
	 * 
	 * @param id
	 *            �û����
	 */
	public boolean delUser(String id) {
		condition = new HashMap<String, Object>();
		condition.put("id", id);
		if (us.delUser(condition)) {
			System.out.println("�����ɹ���");
			return true;
		}
		return false;
	}

	/**
	 * ��¼��ʾ
	 */
	private void loginTips() {
		try {
			System.out.println("*********�û���¼**********");
			u = new User();
			loginUsernameTips();
			loginPwdTips();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �û���������ʾ
	 */
	private void loginUsernameTips() {
		try {
			String username = "";
			do {
				System.out.println("�������������������û���������������");
				username = br.readLine();
			} while (username == null || username.trim().length() == 0);
			u.setUserName(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����������ʾ
	 */
	private void loginPwdTips() {
		try {
			String pwd = "";
			do {
				System.out.println("���������������������롪����������");
				pwd = br.readLine();
			} while (pwd == null || pwd.trim().length() == 0);
			u.setPwd(pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����û�
	 * 
	 */
	private boolean addUserTips() {
		try {
			System.out.println("��ʾ����*�����");
			String username = "";
			do {
				System.out.println("�������������������û���������������(*)");
				username = br.readLine();
			} while (username == null || username.trim().length() == 0);
			if (exit(username))
				return true;
			String pwd = "";
			do {
				System.out.println("���������������������롪����������(*)");
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
	 * �޸�����
	 * 
	 */
	private boolean modifyUserTips() {
		try {
			String id;
			condition = new HashMap<String, Object>();
			System.out.println("��ʾ����*��������������޸ġ�");
			do {
				System.out.println("�������������������û���š�����������(*)");
				id = br.readLine();
				if (exit(id))
					return true;
				if (id != null && id.trim().length() > 0) {
					id = "'" + id + "'";
					condition.put("id", id);
					u = us.isExists(condition);
				}
				if (u == null)
					System.out.println("���û���Ų����ڣ���ȷ�Ϻ��������룡");
			} while (id == null || id.trim().length() == 0 || u == null);
			String pwd;
			do {
				System.out.println("���������������������롪����������(*)");
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
	 * ɾ���û���Ϣ
	 * 
	 */
	private boolean delUserTips() {
		try {
			String id = "";
			condition = new HashMap<String, Object>();
			do {
				System.out.println("�������������������û���š�����������");
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
					System.out.println("���û���Ų����ڣ���ȷ�Ϻ��������룡");
			} while (id == null || id.trim().length() == 0 || us == null);
			return delUser(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��ӡ�˻���Ϣ
	 * 
	 * @param bo
	 *            true��ʾ���룬false����ʾ
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
	 * ��ӡ�û���Ϣ
	 * 
	 * @param bo
	 *            true��ʾ���룬false����ʾ
	 */
	private void printU(User u) {
		System.out.println(u.getId() + "\t" + u.getUserName());
	}
}
