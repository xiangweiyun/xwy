package com.xwy.auth.feign.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xwy.auth.feign.SysLoginLogService;

/**
 * 用户登录日志数据服务的fallback
 * 
 * @author xiangwy
 * @date: 2020-12-03 10:36:51
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
@Service
public class SysLoginLogServiceFallbackImpl implements SysLoginLogService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void saveLoginLog(String logonId, String operType, String resultType, String msg) {
		logger.error("用户登录日志保存{}异常，参数:logonId:{},operType:{},resultType:{},msg:{}", "saveLoginLog", logonId, operType,
				resultType, msg);
	}

}
