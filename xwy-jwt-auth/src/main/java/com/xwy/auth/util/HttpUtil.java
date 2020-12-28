package com.xwy.auth.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xwy.common.xss.XssHttpServletRequestWrapper;

/**
 * http请求工具类
 * 
 * @author xiangwy
 * @date: 2020-12-02 08:48:05
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
public class HttpUtil {
	/**
	 * 获取 HttpServletRequest
	 */
	public static HttpServletResponse getResponse() {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();
		return response;
	}

	/**
	 * 获取 包装防Xss Sql注入的 HttpServletRequest
	 * 
	 * @return request
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return new XssHttpServletRequestWrapper(request);
	}
}
