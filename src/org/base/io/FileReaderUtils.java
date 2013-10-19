package org.base.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

public class FileReaderUtils {
	public static void readFileByByte(String path) {
		try {
			File in = new File(path);
			FileInputStream fi = new FileInputStream(in);
			int a;
			while ((a = fi.read()) != -1) {
				System.out.write(a);
			}
			fi.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readFileByBytes(String path) {
		try {
			File in = new File(path);
			FileInputStream fi = new FileInputStream(in);
			int a;
			byte[] bt = new byte[1024];
			while ((a = fi.read(bt)) != -1) {
				System.out.write(bt, 0, a);
			}
			fi.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readFileByChar(String path) {
		try {
			File in = new File(path);
			FileInputStream fi = new FileInputStream(in);
			int a;
			while ((a = fi.read()) != -1) {
				// System.out.print(a);
				System.out.print((char) a);
			}
			fi.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readFileByLine(String path) {
		try {
			File in = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(in));
			String str = "";
			while ((str = br.readLine()) != null) {
				System.out.print(str);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String path = "c:/temp1/1368170895671.xml";
		readFileByBytes(path);
		// readFileByChar(path);
		readFileByLine(path);
	}

}
