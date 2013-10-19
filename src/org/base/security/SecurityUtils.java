package org.base.security;

import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

/**
 * 
 * 安全加密工具 <br>
 * 定义了MD5 3DES等常用加密算法
 * 
 * JacobKing 2013-10-17 上午11:48:33
 */
public class SecurityUtils {
	private static final byte salt[] = "billbook".getBytes();

	public SecurityUtils() {
	}

	public static String digest(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA");
			md5.update(salt);
			String s = Base64.encode(md5.digest(str.getBytes()));
			return s;
		} catch (NoSuchAlgorithmException ex) {
			throw new UnsupportedOperationException(ex.toString());
		}
	}

	public static void md5(byte data[], int offset, int length, byte digest[],
			int dOffset) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(data, offset, length);
			md5.digest(digest, dOffset, 16);
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (DigestException ex) {
			ex.printStackTrace();
		}
	}

	public static byte[] md5(byte data[], int offset, int length) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(data, offset, length);
			byte abyte0[] = md5.digest();
			return abyte0;
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		byte abyte1[] = null;
		return abyte1;
	}

	public static byte[] encrypt(byte key[], byte src[]) {
		try {
			byte abyte0[] = getCipher(key, 1).doFinal(src);
			return abyte0;
		} catch (BadPaddingException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (IllegalBlockSizeException ex) {
			throw new UnsupportedOperationException(ex.toString());
		}
	}

	public static String encrypt(String key, String src) {
		try {
			String s = Base64.encode(getCipher(key.getBytes("UTF8"), 1)
					.doFinal(src.getBytes("UTF8")));
			return s;
		} catch (UnsupportedEncodingException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (BadPaddingException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (IllegalBlockSizeException ex) {
			throw new UnsupportedOperationException(ex.toString());
		}
	}

	public static byte[] decrypt(byte key[], byte src[]) {
		try {
			byte abyte0[] = getCipher(key, 2).doFinal(src);
			return abyte0;
		} catch (IllegalBlockSizeException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (BadPaddingException ex) {
			throw new UnsupportedOperationException(ex.toString());
		}
	}

	public static String decrypt(String key, String src) {
		try {
			String s = new String(getCipher(key.getBytes("UTF8"), 2).doFinal(
					Base64.decode(src)), "UTF8");
			return s;
		} catch (UnsupportedEncodingException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (BadPaddingException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (IllegalBlockSizeException ex) {
			throw new UnsupportedOperationException(ex.toString());
		}
	}

	public static Cipher getCipher(byte key[], int mode) {
		try {
			if (key.length < 8) {
				byte oldkey[] = key;
				key = new byte[8];
				System.arraycopy(oldkey, 0, key, 0, oldkey.length);
			}
			SecretKeyFactory keyFactory;
			java.security.spec.KeySpec keySpec;
			Cipher c;
			if (key.length >= 24) {
				keyFactory = SecretKeyFactory.getInstance("DESede");
				keySpec = new DESedeKeySpec(key);
				c = Cipher.getInstance("DESede");
			} else {
				keyFactory = SecretKeyFactory.getInstance("DES");
				keySpec = new DESKeySpec(key);
				c = Cipher.getInstance("DES");
			}
			SecretKey k = keyFactory.generateSecret(keySpec);
			c.init(mode, k);
			Cipher cipher = c;
			return cipher;
		} catch (NoSuchAlgorithmException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (InvalidKeyException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (NoSuchPaddingException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (InvalidKeySpecException ex) {
			throw new UnsupportedOperationException(ex.toString());
		}
	}

	/**
	 * 字符串加密以及解密函数
	 * 
	 * @param string
	 *            $string 原文或者密文
	 * @param string
	 *            $operation 操作(ENCODE | DECODE), 默认为 DECODE
	 * @param string
	 *            $key 密钥
	 * @param int $expiry 密文有效期, 加密时候有效， 单位 秒，0 为永久有效
	 * @return string 处理后的 原文或者 经过 base64_encode 处理后的密文
	 * 
	 * @example
	 * 
	 *          $a = authcode('abc', 'ENCODE', 'key'); $b = authcode($a,
	 *          'DECODE', 'key'); // $b(abc)
	 * 
	 *          $a = authcode('abc', 'ENCODE', 'key', 3600); $b =
	 *          authcode('abc', 'DECODE', 'key'); // 在一个小时内，$b(abc)，否则 $b 为空
	 */
	public static String uc_authcode(String $string, String $operation) {
		return uc_authcode($string, $operation, null);
	}

	protected static String base64_decode(String input) {
		try {
			return new String(Base64Ext.decode(input.toCharArray()),
					"iso-8859-1");
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	protected static String base64_encode(String input) {
		try {
			return new String(Base64Ext.encode(input.getBytes("iso-8859-1")));
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public static String uc_authcode(String $string, String $operation,
			String $key) {
		return uc_authcode($string, $operation, $key, 0);
	}

	public static String uc_authcode(String $string, String $operation,
			String $key, int $expiry) {

		int $ckey_length = 4; // note 随机密钥长度 取值 0-32;
		// note 加入随机密钥，可以令密文无任何规律，即便是原文和密钥完全相同，加密结果也会每次不同，增大破解难度。
		// note 取值越大，密文变动规律越大，密文变化 = 16 的 $ckey_length 次方
		// note 当此值为 0 时，则不产生随机密钥

		$key = md5($key);
		String $keya = md5(substr($key, 0, 16));
		String $keyb = md5(substr($key, 16, 16));
		String $keyc = $ckey_length > 0 ? ($operation.equals("DECODE") ? substr(
				$string, 0, $ckey_length) : substr(md5(microtime()),
				-$ckey_length))
				: "";

		String $cryptkey = $keya + md5($keya + $keyc);
		int $key_length = $cryptkey.length();
		// System.out.println("input="+substr($string, $ckey_length));
		$string = $operation.equals("DECODE") ? base64_decode((substr($string,
				$ckey_length))) : sprintf("%010d", $expiry > 0 ? $expiry
				+ time() : 0)
				+ substr(md5($string + $keyb), 0, 16) + $string;
		int $string_length = $string.length();
		// System.out.println("First="+(int)$string.charAt(0));
		// System.out.println("decode="+$string);
		StringBuffer $result1 = new StringBuffer();

		int[] $box = new int[256];
		for (int i = 0; i < 256; i++) {
			$box[i] = i;
		}

		int[] $rndkey = new int[256];
		for (int $i = 0; $i <= 255; $i++) {
			$rndkey[$i] = (int) $cryptkey.charAt($i % $key_length);
			// System.out.print($rndkey[$i]+",");
		}

		int $j = 0;
		for (int $i = 0; $i < 256; $i++) {
			$j = ($j + $box[$i] + $rndkey[$i]) % 256;

			int $tmp = $box[$i];
			$box[$i] = $box[$j];
			$box[$j] = $tmp;

		}

		$j = 0;
		int $a = 0;
		for (int $i = 0; $i < $string_length; $i++) {
			$a = ($a + 1) % 256;

			$j = ($j + $box[$a]) % 256;

			int $tmp = $box[$a];
			$box[$a] = $box[$j];
			$box[$j] = $tmp;
			// System.out.print($box[($box[$a] + $box[$j]) % 256]+",");
			$result1.append((char) (((int) $string.charAt($i)) ^ ($box[($box[$a] + $box[$j]) % 256])));

		}

		if ($operation.equals("DECODE")) {
			String $result = $result1.substring(0, $result1.length());
			if ($result.length() < 25) {
				return "";
			}
			if ((Integer.parseInt(substr($result.toString(), 0, 10)) == 0 || Long
					.parseLong(substr($result.toString(), 0, 10)) - time() > 0)
					&& substr($result.toString(), 10, 16).equals(
							substr(md5(substr($result.toString(), 26) + $keyb),
									0, 16))) {
				return substr($result.toString(), 26);
			} else {
				return "";
			}
		} else {
			return $keyc
					+ base64_encode($result1.toString()).replaceAll("=", "");
		}
	}

	public static String sprintf(String format, long input) {
		String temp = "0000000000" + input;
		return temp.substring(temp.length() - 10);
	}

	public static String md5(String input) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		return byte2hex(md.digest(input.getBytes()));
	}

	public static String md5(long input) {
		return md5(String.valueOf(input));
	}

	private static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs.append("0").append(stmp);
			else
				hs.append(stmp);
		}
		return hs.toString();
	}

	private static String substr(String input, int begin, int length) {
		return input.substring(begin, begin + length);
	}

	private static String substr(String input, int begin) {
		if (begin > 0) {
			return input.substring(begin);
		} else {
			return input.substring(input.length() + begin);
		}
	}

	private static long microtime() {
		return System.currentTimeMillis();
	}

	private static long time() {
		return System.currentTimeMillis() / 1000;
	}

	public static void main(String[] args) {
		// String key =
		// "52s9Y7IdU8N4L5fbH12335V7M4h1Aeb5a45ew1j9ta72G9e7cc543cb6A6I2I1h5";
		// String input =
		// "36875DzQGDSEBYhGF9zcNfQDMEPBjzkcNGZX/sp7H78dbS7MBV1IVhWTrTm7I1krPnSNAuDlL/TZBEmAn/TXjKBDOu6svqoj8Wfr5NmpGTt9bwzpLW0HXs0VYJmCRhS+CWBqB75uMFhQ+FTRPglOOyvcVALM3kDn";
		// System.out.println(uc_authcode(input, "DECODE", key));
		System.out.println(md5("jacobking"));
	}
}
