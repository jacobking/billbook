package org.base.property;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 读取配置文件
 * 
 * 
 * JacobKing 2013-10-16 下午2:12:16
 */
public class PropertyUtils {
	static Logger log = Logger.getLogger(PropertyUtils.class);
	private static Properties prop = new Properties();

	// 配置文件
	private static String configFilePath = "config.properties";

	public PropertyUtils() {
		try {
			InputStream in = this.getClass().getResourceAsStream(
					"/" + configFilePath);
			prop.load(in);
			in.close();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}

	/**
	 * 获取对应键的值
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
	public String getProperty(String key) {
		return prop.getProperty(key);
	}

}
