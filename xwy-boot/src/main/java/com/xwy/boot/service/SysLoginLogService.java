package com.xwy.boot.service;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xwy.entity.SysLoginLog;

/**
 * <p>
 * 用户登录日志 服务类
 * </p>
 *
 * @author xiangwy
 * @since 2020-12-03
 */
public interface SysLoginLogService extends IService<SysLoginLog> {
	/**
	 * 登录日志保存
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-03 10:29:36
	 * @param request
	 * @param logonId    登录账号
	 * @param operType   操作类型（1,登录;2,登出）
	 * @param resultType 登录结果类型(1,成功；2失败)
	 * @param msg        备注
	 */
	public void saveLoginLog(HttpServletRequest request, String logonId, String operType, String resultType,
			String msg);
}
