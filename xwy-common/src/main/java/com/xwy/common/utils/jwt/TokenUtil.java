package com.xwy.common.utils.jwt;

import javax.servlet.http.HttpServletRequest;

import com.xwy.common.utils.BlankUtils;
import com.xwy.common.utils.ServletUtils;

/**
 * Token处理
 * 
 * @author xiangwy
 * @date: 2020-12-03 09:17:30
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
public class TokenUtil {
	/**
	 * 获取jwt authorization
	 */
	public static String getAuthorization() {
		HttpServletRequest request = ServletUtils.getRequest();
		String authorization = request.getHeader("Authorization");
		return authorization;
	}

	/**
	 * 获取jwt token
	 */
	public static String getToken() {
		String token = "";
		String authorization = getAuthorization();
		if (BlankUtils.isNotBlank(authorization) && authorization.startsWith("Bearer")) {
			token = authorization.substring(7);
		}
		return token;
	}

	/**
	 * 获取userInfoStr
	 */
	public static String getUserInfoStr() {
		HttpServletRequest request = ServletUtils.getRequest();
		String userInfoStr = request.getHeader("userInfoStr");
		return userInfoStr;
	}

	/**
	 * 获取登录用户id
	 */
	public static String getUserId() {
		String userId = "0";
		String userInfoStr = getUserInfoStr();
		if (BlankUtils.isNotBlank(userInfoStr)) {
			String[] userInfoArr = userInfoStr.split(";;");
			if (userInfoArr.length > 0) {
				userId = userInfoArr[0];
			}
		}
		return userId;
	}

	/**
	 * 获取用户登录类型
	 */
	public static String getLoginType() {
		String loginType = "";
		String userInfoStr = getUserInfoStr();
		if (BlankUtils.isNotBlank(userInfoStr)) {
			String[] userInfoArr = userInfoStr.split(";;");
			if (userInfoArr.length > 2) {
				loginType = userInfoArr[2];
			}
		}
		return loginType;
	}

	/**
	 * 获取登录用户名
	 */
	public static String getUsernameFromToken(String token) {
		String userName = "";
		String userInfoStr = getUserInfoStr();
		if (BlankUtils.isNotBlank(userInfoStr)) {
			String[] userInfoArr = userInfoStr.split(";;");
			if (userInfoArr.length > 1) {
				userName = userInfoArr[1];
			}
		}
		return userName;
	}

}
