package com.ylink.inetpay.cbs.util;

/**
 * Http服务运行时异常
 * @author lhui
 * @date 2016-10-22
 */
public class HttpRuntimeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;

	public HttpRuntimeException() {
		super();
	}

	public HttpRuntimeException(String code, String message) {
		super(message);
		this.code = code;
	}

	public HttpRuntimeException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
