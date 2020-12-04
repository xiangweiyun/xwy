package com.xwy.auth.validator.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.xwy.auth.validator.IReqValidator;
import com.xwy.auth.vo.AuthRequestVo;
import com.xwy.common.constant.XwyRspCon;
import com.xwy.common.utils.DataformResult;

/**
 * 直接验证账号密码是不是admin
 * 
 * @author xiangwy
 * @date: 2020-12-03 11:17:27
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
@Service
public class SimpleValidator implements IReqValidator {

	private static String USER_NAME = "admin";

	private static String PASSWORD = "admin";

	@Override
	public DataformResult<Map<String, Object>> validate(AuthRequestVo authRequestVo) {
		String userName = authRequestVo.getUserName();
		String password = authRequestVo.getPassword();
		if (USER_NAME.equals(userName) && PASSWORD.equals(password)) {
			return DataformResult.success();
		} else {
			return DataformResult.failure(XwyRspCon.FLAG_LOGIN_ERROR);
		}
	}
}
