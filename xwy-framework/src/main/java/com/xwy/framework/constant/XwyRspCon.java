package com.xwy.framework.constant;

/**
 * 返回信息枚举类
 * 
 * @author xiangwy
 * @date: 2020-12-02 09:07:47
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
public enum XwyRspCon {
	/**
	 * 操作成功
	 */
	SUCCESS(200, "操作成功!"),
	/**
	 * 操作异常!
	 */
	ERROR(500, "操作异常!"),
	/**
	 * 接口无访问权限！请联系管理员授权使用！
	 */
	NO_PERMISSION(501, "接口无访问权限！请联系管理员授权使用！"),
	/**
	 * 系统未知错误!
	 */
	FLAG_UNKNOW(1000, "系统未知错误!"),
	/**
	 * 用户未登录!
	 */
	FLAG_NOT_LOGIN(1001, "用户未登录!"),
	/**
	 * 登录账户不存在!
	 */
	FLAG_NOT_LOGINNAME(1002, "登录账户不存在!"),
	/**
	 * 您的电话号码不正确!
	 */
	FLAG_NOT_LOGINPHONE(1003, "您的电话号码不正确!"),
	/**
	 * 原密码错误!
	 */
	FLAG_OLDPWD_ERROR(1004, "原密码错误!"),
	/**
	 * 用户名密码错误!
	 */
	FLAG_LOGIN_ERROR(1005, "用户名密码错误!"),
	/**
	 * 用户名密码或验证码错误!
	 */
	FLAG_LOGINORCAPTCHA_ERROR(1006, "用户名密码或验证码错误!"),
	/**
	 * 登录类型不能为空!
	 */
	FLAG_LOGIN_TYPE_NOTNULL(1007, "登录类型不能为空!"),
	/**
	 * 登录类型错误!
	 */
	FLAG_LOGIN_TYPE_ERROR(1008, "登录类型错误!"),
	/**
	 * Json串解析异常!
	 */
	FLAG_JSON_FAIL(1011, "Json串解析异常!"),
	/**
	 * 数据不存在,没找到对应记录!
	 */
	FLAG_DATA_ERROR(2000, "数据不存在,没找到对应记录!"),
	/**
	 * 参数错误!
	 */
	FLAG_PARAM_ERROR(3001, "参数错误!"),
	/**
	 * 接口限流了!
	 */
	FLOWEXCEPTION(100, "接口限流了!"),
	/**
	 * 服务降级了!
	 */
	DEGRADEEXCEPTION(101, "服务降级了!"),
	/**
	 * 热点参数限流了!
	 */
	PARAMFLOWEXCEPTION(102, "热点参数限流了!"),
	/**
	 * 触发系统保护规则!
	 */
	SYSTEMBLOCKEXCEPTION(103, "触发系统保护规则!"),
	/**
	 * 授权规则不通过!
	 */
	AUTHORITYEXCEPTION(104, "授权规则不通过!"),

	/**
	 * jwt异常 token过期
	 */
	TOKEN_EXPIRED(700, "token过期"),
	/**
	 * token验证失败
	 */
	TOKEN_ERROR(700, "token验证失败"),
	/**
	 * 签名验证失败
	 */
	SIGN_ERROR(700, "签名验证失败");

	private Integer code;
	private String msg;

	private XwyRspCon(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
