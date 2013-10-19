package org.base.log;

import org.apache.log4j.Logger;

/**
 * 日志工具类
 * 
 * JacobKing 2013-10-17 下午4:30:42
 */
public class LoggerUtils extends Logger {
	static LoggerUtils loggerUtils;

	protected LoggerUtils(String name) {
		super(name);
	}

	public void info(int type, Object message) {
		String optr = "";
		switch (type) {
		case 1:
			optr = "新增\t";
			break;
		case 2:
			optr = "删除\t";
			break;
		case 3:
			optr = "修改\t";
			break;
		case 4:
			optr = "查询\t";
			break;
		}
		super.info(optr + message.toString());
	}
}
