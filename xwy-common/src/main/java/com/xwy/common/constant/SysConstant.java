package com.xwy.common.constant;

/**
 * 系统常量
 * 
 * @author xiangwy
 * @date: 2020-12-03 09:25:24
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
public class SysConstant {
	public static final String AUTH_KEY = "Authorization";
	public static final String AUTH_PRE_B = "Bearer ";
	public static final String AUTH_PRE_S = "bearer ";
	public static final String SQL_FILTER = "sql_filter"; // 数据权限过滤
	public final static String CAPTCHA_KEY = "CAPTCHA:";// 保存到Redis中的验证码key
	public final static String NAME2ID_KEY = "sys:user:name2id:";// 保存到Redis中的系统用户name对于的id key
	public final static String ID2NAME_KEY = "sys:user:id2name:";// 保存到Redis中的系统用户id对应的mame key
	public final static String ID2USER_KEY = "sys:user:id2user:";// 保存到Redis中的系统用户id对应的用户信息 key
	public final static String BASE_ROLE = "ROLE_USER";// 基础角色
	public final static String INIT_PWD = "000000";// 初始密码
	public final static String PERMS_MENU_DATA = "sys:perms:menu:data:";// 按钮权限数据
	public static final String LOG_TYPE_NORMAL = "1";// 日志类型（1：正常日志；2：异常日志）
	public static final String LOG_TYPE_EXCEPTION = "2";// 日志类型（1：正常日志；2：异常日志）
	/**
	 * UTF-8 字符集
	 */
	public static final String UTF8 = "UTF-8";

	/**
	 * 自动去除表前缀
	 */
	public static String AUTO_REOMVE_PRE = "true";

	/**
	 * 每页显示记录数
	 */
	public static String PAGE_SIZE = "pageSize";

	/**
	 * 当前记录第几页
	 */
	public static String PAGE = "page";

	/**
	 * 分页simple
	 */
	public static String SIMPLE = "simple";

	/**
	 * 排序列
	 */
	public static String SORT_FIELD = "sortField";

	/**
	 * 排序的方向 "desc" 或者 "asc".
	 */
	public static String SORT_ORDER = "sortOrder";

	/**
	 * 查询现在条件
	 */
	public static String CONDITION = "condition";

}
