package com.xwy.gateway.fegin.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.xwy.gateway.fegin.AuthService;
import com.xwy.gateway.util.DataformResult;

/**
 * jwt 认证服务的fallback
 * 
 * @author xiangwy
 * @date: 2020-12-01 17:35:26
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
@Service
public class AuthServiceFallbackImpl implements AuthService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public DataformResult<String> checkToken(@RequestParam(value = "authToken") String authToken) {
		logger.error("调用{}异常:{}", "checkToken", authToken);
		return DataformResult.failure();
	}

}
