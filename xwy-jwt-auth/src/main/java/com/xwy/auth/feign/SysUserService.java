package com.xwy.auth.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xwy.auth.feign.fallback.SysUserServiceFallbackImpl;
import com.xwy.common.utils.DataformResult;

/**
 * 系统用户信息
 * 
 * @author xiangwy
 * @date: 2020-12-03 10:41:49
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
@FeignClient(name = "xwy-boot", fallback = SysUserServiceFallbackImpl.class)
public interface SysUserService {
	/**
	 * 系统用户登录
	 * 
	 * @param username 登录账号或者手机号或者邮箱 password：登录密码
	 * @return 系统用户信息
	 */
	@GetMapping(value = "/sysUser/getByLoginInfo")
	public DataformResult<Map<String, Object>> getByLoginInfo(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password);

}
