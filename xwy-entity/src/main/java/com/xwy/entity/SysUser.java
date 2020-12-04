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
 * 系统_用户
 * </p>
 *
 * @author xiangwy
 * @since 2020-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysUser对象", description="系统_用户")
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /** 用户名. */
    public static final String USERNAME = "username";
    /** 姓名. */
    public static final String NAME = "name";
    /** 用户状态【1启用、0禁用】. */
    public static final String STATUS = "status";
    /** 密码. */
    public static final String PASSWORD = "password";
    /** 邮箱. */
    public static final String EMAIL = "email";
    /** 手机号码. */
    public static final String MOBILE = "mobile";
    /** 是否锁定【1是、0否】. */
    public static final String IS_LOCK = "isLock";
    /** 锁定时间. */
    public static final String LOCK_TIME = "lockTime";
    /** 登录次数. */
    public static final String LOGIN_COUNT = "loginCount";
    /** 失败次数. */
    public static final String LOGIN_FAILURE_COUNT = "loginFailureCount";
    /** 登录Ip. */
    public static final String LOGIN_IP = "loginIp";
    /** 登录时间. */
    public static final String LOGIN_TIME = "loginTime";

    /**
     * 用户名
     */
    @Column(name = "USERNAME")
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 姓名
     */
    @Column(name = "NAME")
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 用户状态【1启用、0禁用】
     */
    @Column(name = "STATUS")
    @ApiModelProperty(value = "用户状态【1启用、0禁用】")
    private String status;

    /**
     * 密码
     */
    @Column(name = "PASSWORD")
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 邮箱
     */
    @Column(name = "EMAIL")
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 手机号码
     */
    @Column(name = "MOBILE")
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    /**
     * 是否锁定【1是、0否】
     */
    @Column(name = "IS_LOCK")
    @ApiModelProperty(value = "是否锁定【1是、0否】")
    private String isLock;

    /**
     * 锁定时间
     */
    @Column(name = "LOCK_TIME")
    @ApiModelProperty(value = "锁定时间")
    private LocalDateTime lockTime;

    /**
     * 登录次数
     */
    @Column(name = "LOGIN_COUNT")
    @ApiModelProperty(value = "登录次数")
    private Long loginCount;

    /**
     * 失败次数
     */
    @Column(name = "LOGIN_FAILURE_COUNT")
    @ApiModelProperty(value = "失败次数")
    private Long loginFailureCount;

    /**
     * 登录Ip
     */
    @Column(name = "LOGIN_IP")
    @ApiModelProperty(value = "登录Ip")
    private String loginIp;

    /**
     * 登录时间
     */
    @Column(name = "LOGIN_TIME")
    @ApiModelProperty(value = "登录时间")
    private String loginTime;
}
