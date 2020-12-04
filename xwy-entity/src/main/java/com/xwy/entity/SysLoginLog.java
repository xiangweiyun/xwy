package com.xwy.entity;

import com.xwy.common.base.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户登录日志
 * </p>
 *
 * @author xiangwy
 * @since 2020-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SysLoginLog对象", description = "用户登录日志")
public class SysLoginLog extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/** 操作类型（1,登录;2,登出）. */
	public static final String OPER_TYPE = "operType";
	/** 登录结果类型(1,成功；2失败). */
	public static final String RESULT_TYPE = "resultType";
	/** 操作IP地址. */
	public static final String REMOTE_ADDR = "remoteAddr";
	/** 用户代理. */
	public static final String USER_AGENT = "userAgent";
	/** 操作时间（登录/退出的时间）. */
	public static final String OPER_TIME = "operTime";
	/** 地点. */
	public static final String ADDRESS = "address";
	/** 浏览器. */
	public static final String BROWSER_TYPE = "browserType";
	/** 登录类型. */
	public static final String LOGIN_TYPE = "loginType";
	/** 设备. */
	public static final String EQUIPMENT = "equipment";
	/** 登录帐号. */
	public static final String LOGON_ID = "logonId";

	/**
	 * 操作类型（1,登录;2,登出）
	 */
	@Column(name = "OPER_TYPE")
	@ApiModelProperty(value = "操作类型（1,登录;2,登出）")
	private String operType;

	/**
	 * 登录结果类型(1,成功；2失败)
	 */
	@Column(name = "RESULT_TYPE")
	@ApiModelProperty(value = "登录结果类型(1,成功；2失败)")
	private String resultType;

	/**
	 * 操作IP地址
	 */
	@Column(name = "REMOTE_ADDR")
	@ApiModelProperty(value = "操作IP地址")
	private String remoteAddr;

	/**
	 * 用户代理
	 */
	@Column(name = "USER_AGENT")
	@ApiModelProperty(value = "用户代理")
	private String userAgent;

	/**
	 * 操作时间（登录/退出的时间）
	 */
	@Column(name = "OPER_TIME")
	@ApiModelProperty(value = "操作时间（登录/退出的时间）")
	private LocalDateTime operTime;

	/**
	 * 地点
	 */
	@Column(name = "ADDRESS")
	@ApiModelProperty(value = "地点")
	private String address;

	/**
	 * 浏览器
	 */
	@Column(name = "BROWSER_TYPE")
	@ApiModelProperty(value = "浏览器")
	private String browserType;

	/**
	 * 登录类型
	 */
	@Column(name = "LOGIN_TYPE")
	@ApiModelProperty(value = "登录类型")
	private String loginType;

	/**
	 * 设备
	 */
	@Column(name = "EQUIPMENT")
	@ApiModelProperty(value = "设备")
	private String equipment;

	/**
	 * 登录帐号
	 */
	@Column(name = "LOGON_ID")
	@ApiModelProperty(value = "登录帐号")
	private String logonId;
}
