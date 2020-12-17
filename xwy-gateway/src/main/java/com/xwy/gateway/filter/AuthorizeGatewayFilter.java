package com.xwy.gateway.filter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSON;
import com.xwy.gateway.fegin.AuthService;
import com.xwy.gateway.properties.IgnoreUrlsProperties;
import com.xwy.gateway.util.DataformResult;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * token校验过滤器
 * 
 * @author xiangwy
 * @date: 2020-12-03 11:42:02
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
@Component
public class AuthorizeGatewayFilter implements GlobalFilter, Ordered {
	private final Log logger = LogFactory.getLog(this.getClass());

	// 认证的头部
	private static final String AUTHORIZE_TOKEN = "Authorization";
	// UTF-8 字符集
	public static final String UTF8 = "UTF-8";

	@Autowired
	private AuthService authService;

	@Autowired
	private IgnoreUrlsProperties ignoreUrlsProperties;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		HttpHeaders headers = request.getHeaders();
		String path = request.getPath().value();
		logger.info("请求网关地址：" + path);
		try {
			if (isIgnoreHttpUrls(path)) {
				return chain.filter(exchange);
			}
			final String authorization = headers.getFirst(AUTHORIZE_TOKEN);
			if (authorization != null && authorization.startsWith("Bearer")) {
				String authToken = authorization.substring(7);
				DataformResult<String> checkToken = authService.checkToken(authToken);
				int code = checkToken.getStatusCode();
				if (code == 200) {// 认证（校验）成功的
					ServerHttpRequest.Builder builder = request.mutate();
					// 转发的请求都加上服务间认证token
					builder.header(AUTHORIZE_TOKEN, authorization);
					// 将jwt token中的用户信息传给服务
					builder.header("userInfoStr", checkToken.getObject());
					ServerWebExchange mutableExchange = exchange.mutate().request(builder.build()).build();
					return chain.filter(mutableExchange);
				} else if (code == 700) {
					logger.info("token已过期！");
					return unauthorizedResponse(exchange, DataformResult.failure(code, "token已过期！"));
				} else if (code == 701) {
					logger.info("该token未进行认证！");
					return unauthorizedResponse(exchange, DataformResult.failure(code, "该token未进行认证！"));
				} else if (code == 1) {
					logger.info("认证接口调用异常！");
					return unauthorizedResponse(exchange, DataformResult.failure(code, "认证接口调用异常！"));
				}
			} else {
				logger.info("认证信息不为空！");
				return unauthorizedResponse(exchange, DataformResult.failure(702, "请确保认证信息不为空！"));
			}
		} catch (Exception e) {
			logger.info("网关异常：" + e);
			return unauthorizedResponse(exchange, DataformResult.failure(703, "网关异常！"));
		}
		return chain.filter(exchange);
	}

	/**
	 * 请求地址是否为需要忽略token验证的地址
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-03 11:50:21
	 * @param servletPath
	 * @return
	 */
	private boolean isIgnoreHttpUrls(String servletPath) {
		boolean rt = false;
		List<String> ignoreHttpUrls = ignoreUrlsProperties.getHttpUrls();
		for (String ihu : ignoreHttpUrls) {
			if (servletPath.contains(ihu)) {
				rt = true;
				break;
			}
		}
		return rt;
	}

	/**
	 * 认证失败时，返回json数据
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-03 11:49:28
	 * @param exchange
	 * @param dataformResult
	 * @return
	 */
	private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, DataformResult<String> dataformResult) {
		ServerHttpResponse originalResponse = exchange.getResponse();
		originalResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
		originalResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
		byte[] response = null;
		try {
			response = JSON.toJSONString(dataformResult).getBytes(UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		DataBuffer buffer = originalResponse.bufferFactory().wrap(response);
		return originalResponse.writeWith(Flux.just(buffer));
	}

	@Override
	public int getOrder() {
		return -100;
	}

}