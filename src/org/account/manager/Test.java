package org.account.manager;

import org.base.security.SecurityUtils;

public class Test {
	public static void main(String[] args) {
		// System.out.println(exit());
		System.out.println(SecurityUtils.encrypt("md5",
				SecurityUtils.md5("helloworld")));
		String a = SecurityUtils.encrypt("jacobking", "helloworld");
		String b = SecurityUtils.decrypt("jacobking", a);
		System.out.println(a);
		System.out.println(b);
	}

	public static boolean exit() {
		int a = 0;
		do {
			System.out.println("----------" + a);
			a++;
			if (a == 3)
				return true;
		} while (a <= 10);
		return false;
	}
}
