package org.base.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * 
 * JacobKing 2013-10-16 下午4:04:30
 */
public class DateUtils {
	/**
	 * 获取当前时间
	 * 
	 * @type <p>
	 *       1:（返回格式：yyyy-MM-dd HH:mm:ss）
	 *       </p>
	 *       <p>
	 *       2:（返回格式：yyyyMMddHHmmss）
	 *       </p>
	 * 
	 * @return 当前时间
	 */
	public static String getNowTime(int type) {
		String format = "yyyy-MM-dd HH:mm:ss";
		switch (type) {
		case 1:
			format = "yyyy-MM-dd HH:mm:ss";
			break;
		case 2:
			format = "yyyyMMddHHmmss";
		}
		SimpleDateFormat s = new SimpleDateFormat(format);
		return s.format(new Date());
	}
}
