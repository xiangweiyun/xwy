package com.xwy.auth.feign.fallback;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.xwy.auth.feign.SysUserService;
import com.xwy.framework.utils.DataformResult;

/**
 * 系统用户信息服务的fallback
 * 
 * @author xiangwy
 * @date: 2020-12-03 10:38:14
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
@Service
public class SysUserServiceFallbackImpl implements SysUserService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public DataformResult<Map<String, Object>> getByLoginInfo(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password) {
		logger.error("系统用户jwt登录调用{}异常:{}", "getByLoginInfo", username);
		return DataformResult.failure("系统用户jwt登录调用getByLoginInfo异常-" + username);
	}

}
