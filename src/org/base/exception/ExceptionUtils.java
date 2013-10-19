package org.base.exception;

/**
 * 系统异常工具类
 * 
 * JacobKing 2013-10-17 上午10:38:50
 */
@SuppressWarnings("serial")
public class ExceptionUtils extends Exception {

	public ExceptionUtils() {
		super();
	}

	public ExceptionUtils(String message, Throwable cause) {
		super(message, cause);
	}

	public ExceptionUtils(String message) {
		super(message);
	}

	public ExceptionUtils(Throwable cause) {
		super(cause);
	}
}
