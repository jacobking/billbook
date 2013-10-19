package org.base.io;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * 文件操作工具类
 * 
 * JacobKing 2013-10-17 下午3:23:05
 */
public class FileUtils {
	static Logger log = Logger.getLogger(FileUtils.class);

	/**
	 * 创建文件夹
	 * 
	 * @param filepath
	 *            文件路径
	 */
	public static String createFile(String filepath) {
		if (filepath != null) {
			File f = new File(filepath);
			if (!f.exists()) {
				f.mkdirs();
				if (f.isDirectory())
					log.info("创建文件目录" + f.getParent());
				f.delete();
			}
			return filepath;
		}
		log.error(new IOException("文件路径不存在！"));
		return null;
	}

	/**
	 * 删除文件
	 * 
	 * @param filename
	 *            文件路径
	 */
	public static void removeFile(String filename) {
		File f = new File(filename);
		if (f.exists()) {
			log.info("删除文件：" + filename);
			f.delete();
		} else {
			log.error("删除失败！未找到文件：" + filename);
		}
	}

}
