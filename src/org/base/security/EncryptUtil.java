package org.base.security;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.log4j.Logger;
import org.base.date.DateUtils;
import org.base.io.FileUtils;
import org.base.property.PropertyUtils;

/**
 * 字符加密解密工具类
 * 
 * JacobKing 2013-10-16 下午2:20:15
 */
public class EncryptUtil {
	static Logger log = Logger.getLogger(EncryptUtil.class);
	private static String filePath = "f://encrypt//";// 文件路径
	private static String appKey = "e372ff73-481b-46ba-96d3-e72f5b9e3fd1";// 初始秘钥
	private static String alg = "PBEWithMD5AndDES";// 加密方式
	private static String keySuffix = "sk";// 秘钥文件的后缀
	private static String dataSuffix = "dt";// 加密文件的后缀
	private static SecretKey key = null;// 秘钥

	/**
	 * 初始化参数
	 */
	private static void initConfig() {
		PropertyUtils pu = new PropertyUtils();
		filePath = (pu.getProperty("filepath") != null ? pu
				.getProperty("filepath") : filePath);
		appKey = (pu.getProperty("appKey") != null ? pu.getProperty("appKey")
				: appKey);
		alg = (pu.getProperty("alg") != null ? pu.getProperty("alg") : alg);
		keySuffix = (pu.getProperty("keySuffix") != null ? pu
				.getProperty("keySuffix") : alg);
		dataSuffix = (pu.getProperty("dataSuffix") != null ? pu
				.getProperty("dataSuffix") : alg);
	}

	/**
	 * 构造函数
	 * 
	 * @param filepath
	 *            文件路径
	 * @param appkey
	 *            秘钥
	 */
	public EncryptUtil(String filepath) {
		filePath = FileUtils.createFile(filepath);
	}

	/**
	 * 加密后返回加密字符
	 * 
	 * @param ecwords
	 *            待加密字符
	 */
	public static byte[] Encrypt(String ecwords) {
		log.info("开始加密,当前时间:" + DateUtils.getNowTime(1));
		try {
			long a = System.currentTimeMillis();
			// 基于密码的加密要用PBEWithMD5AndDES算法
			Cipher cipher = Cipher.getInstance(alg);
			// 给数据加盐，使加密更可靠
			PBEParameterSpec parameterSpec = new PBEParameterSpec(new byte[] {
					1, 2, 3, 4, 5, 6, 7, 8 }, 9);
			// 创建密钥
			key = SecretKeyFactory.getInstance(alg).generateSecret(
					new PBEKeySpec(appKey.toCharArray()));
			// 用密钥和盐初始化Cipher
			cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
			// 对数据进行加密
			byte[] encryptwords = cipher.doFinal(ecwords.getBytes());
			long b = System.currentTimeMillis();
			log.info("加密完成.耗时" + (b - a) + "ms");
			return encryptwords;
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	/**
	 * 加密后生产加密文件
	 * 
	 * @param filepath
	 *            文件路径
	 * @param ecwords
	 *            待加密字符
	 */
	public static void Encrypt2File(String filepath, String ecwords) {
		try {
			initConfig();
			filePath = FileUtils.createFile(filePath + filepath);
			if (filePath == null)
				return;
			// 对数据进行加密
			byte[] result = Encrypt(ecwords);
			if (result == null)
				return;
			// 把密钥写到硬盘上
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					new FileOutputStream(new File(filePath + "." + keySuffix)));
			objectOutputStream.writeObject(key);
			log.info("create key file,path:" + filePath + "." + keySuffix);
			// 把加密后的结果存到硬盘上
			FileOutputStream outputStream = new FileOutputStream(new File(
					filePath + "." + dataSuffix));
			outputStream.write(result);
			log.info("create encryption file,path:" + filePath + "."
					+ dataSuffix);
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * 解密字符串
	 * 
	 * @param words
	 *            加密字符
	 * @return 解密字符
	 */
	public static String Decrypt(ByteArrayOutputStream words) {
		log.info("开始解密，当前时间：" + DateUtils.getNowTime(1));
		try {
			long a = System.currentTimeMillis();
			Cipher cipher = Cipher.getInstance(alg);
			PBEParameterSpec parameterSpec = new PBEParameterSpec(new byte[] {
					1, 2, 3, 4, 5, 6, 7, 8 }, 9);
			// 获取密钥
			key = SecretKeyFactory.getInstance(alg).generateSecret(
					new PBEKeySpec(appKey.toCharArray()));
			cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
			// 解密，返回解密后的结果
			byte[] data = cipher.doFinal(words.toByteArray());
			long b = System.currentTimeMillis();
			log.info("解密完成.耗时" + (b - a) + "ms");
			return new String(data);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从文件解密
	 * 
	 * @param filename
	 *            文件名称
	 * @return 解密后字符
	 */
	public static String DecryptFromFile(String filename) {
		if (filename == null)
			return null;
		initConfig();
		log.info("开始解密，当前时间：" + DateUtils.getNowTime(1));
		long a = System.currentTimeMillis();
		try {
			File keyfile = new File(filePath + filename + "." + keySuffix);
			if (!keyfile.exists() || keyfile.isDirectory()) {
				log.error("秘钥文件：" + (filePath + filename + "." + keySuffix)
						+ "不存在");
				return null;
			}
			File datafile = new File(filePath + filename + "." + dataSuffix);
			if (!datafile.exists() || datafile.isDirectory()) {
				log.error("加密文件：" + (filePath + filename + "." + dataSuffix)
						+ "不存在");
				return null;
			}
			Cipher cipher = Cipher.getInstance(alg);
			PBEParameterSpec parameterSpec = new PBEParameterSpec(new byte[] {
					1, 2, 3, 4, 5, 6, 7, 8 }, 9);
			log.info("开始读取秘钥文件");
			// 从硬盘读取key
			ObjectInputStream objectInputStream = new ObjectInputStream(
					new FileInputStream(keyfile));
			key = (SecretKey) objectInputStream.readObject();
			cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
			log.info("开始读取加密文件");
			// 从硬盘中读取数据文件
			FileInputStream fileInputStream = new FileInputStream(datafile);
			// 把加密后的文件读取到ByteArrayOutputStream中
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fileInputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
			fileInputStream.close();
			// 解密，返回解密后的结果
			byte[] data = cipher.doFinal(outputStream.toByteArray());
			outputStream.close();
			long b = System.currentTimeMillis();
			log.info("解密完成.耗时" + (b - a) + "ms");
			return new String(data);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;
	}
}
