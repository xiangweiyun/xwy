package com.xwy.common.log.enums;

/**
 * 业务操作类型
 * 
 * @author xiangwy
 * @date: 2020-10-17 10:41:38
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
public enum BusinessType {
	/**
	 * 查询
	 */
	SELECT,
	/**
	 * 其它
	 */
	OTHER,

	/**
	 * 新增
	 */
	INSERT,

	/**
	 * 修改
	 */
	UPDATE,

	/**
	 * 删除
	 */
	DELETE,

	/**
	 * 授权
	 */
	GRANT,

	/**
	 * 导出
	 */
	EXPORT,

	/**
	 * 导入
	 */
	IMPORT,

	/**
	 * 强退
	 */
	FORCE,

	/**
	 * 生成代码
	 */
	GENCODE,

	/**
	 * 清空数据
	 */
	CLEAN,
}
