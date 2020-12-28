package com.xwy.auth.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 认证的请求dto
 * 
 * @author xiangwy
 * @date: 2020-12-01 17:57:41
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
@Setter
@Getter
@ApiModel(value = "认证的请求dto", description = "认证的请求dto")
public class AuthRequestVo {
	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	private String userName;
	/**
	 * 密码
	 */
	@ApiModelProperty("密码")
	private String password;

	/**
	 * 验证码
	 */
	@ApiModelProperty("验证码")
	private String captcha;

	/**
	 * 验证码id
	 */
	@ApiModelProperty("验证码id")
	private String captchaId;

	/**
	 * 登录类型 1admin 后台登录
	 */
	@ApiModelProperty("登录类型  1admin 后台登录")
	private String logintype;

}
