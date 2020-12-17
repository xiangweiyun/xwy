package com.xwy.gateway.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xwy.gateway.fegin.fallback.AuthServiceFallbackImpl;
import com.xwy.gateway.util.DataformResult;

/**
 * jwt 认证
 * 
 * @author xiangwy
 * @date: 2020-12-01 17:34:49
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
@FeignClient(name = "xwy-jwt-auth", fallback = AuthServiceFallbackImpl.class)
public interface AuthService {
	/**
	 * token 校验
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-01 17:38:43
	 * @param authToken
	 * @return
	 */
	@PostMapping(value = "/auth/checkToken")
	public DataformResult<String> checkToken(@RequestParam(value = "authToken") String authToken);

}
