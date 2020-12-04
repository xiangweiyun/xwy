package com.xwy.common.exception;

/**
 * 自定义异常
 * 
 * @author xiangwy
 * @date: 2020-12-02 08:57:16
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
public class XwyException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String msg;
	private int code = 500;

	public XwyException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public XwyException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public XwyException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public XwyException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
