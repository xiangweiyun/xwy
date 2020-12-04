package com.xwy.auth.validator.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwy.auth.feign.SysLoginLogService;
import com.xwy.auth.feign.SysUserService;
import com.xwy.auth.validator.IReqValidator;
import com.xwy.auth.vo.AuthRequestVo;
import com.xwy.common.constant.SysConstant;
import com.xwy.common.constant.XwyRspCon;
import com.xwy.common.redis.util.JedisUtils;
import com.xwy.common.utils.BlankUtils;
import com.xwy.common.utils.DataformResult;

/**
 * 账号密码验证
 * 
 * @author xiangwy
 * @date: 2020-12-03 11:16:49
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
@Service
public class DbValidator implements IReqValidator {

	private static Logger logger = LoggerFactory.getLogger(DbValidator.class);

	@Autowired
	private SysUserService sysUserGssService; // 系统用户信息接口

	@Autowired
	private SysLoginLogService sysLoginLogService;// 系统用户登录日志接口

	@SuppressWarnings("unchecked")
	@Override
	public DataformResult<Map<String, Object>> validate(AuthRequestVo authRequestVo) {
		String loginType = authRequestVo.getLogintype();
		if (BlankUtils.isNotBlank(loginType)) {// 登录类型 1 后台系统用户登录
			String userName = authRequestVo.getUserName();// 用户名
			String password = authRequestVo.getPassword();// 密码
			if ("1".equals(loginType)) { // 1 后台系统用户登录
				String captcha = authRequestVo.getCaptcha();// 验证码
				String captchaId = authRequestVo.getCaptchaId();// 验证码id
				if (BlankUtils.isNotBlank(captcha) && BlankUtils.isNotBlank(captchaId)) {
					String key = SysConstant.CAPTCHA_KEY + captchaId;
					String captchaCache = (String) JedisUtils.getObject(key);
					if (captcha.equalsIgnoreCase(captchaCache)) {
						JedisUtils.deleteKeys(key);
						DataformResult<Map<String, Object>> login = sysUserGssService.getByLoginInfo(userName,
								password);
						Map<String, Object> loginDatas = (Map<String, Object>) login.getObject();
						// 查看登录用户和密码是否正确
						if (BlankUtils.isNotBlank(loginDatas)) {// 用户名和密码正确,登录成功
							Map<String, Object> userInfo = (Map<String, Object>) loginDatas.get("userInfo");
							if (BlankUtils.isNotBlank(userInfo)) {
								if (BlankUtils.isNotBlank(userInfo.get("password"))) {// 密码注释掉
									userInfo.put("password", "***");
								}
							}
							loginDatas.put("userInfo", userInfo);
							sysLoginLogService.saveLoginLog(userName, "1", "1", "登录成功！！");
							logger.info(userName + "登录成功！");
							return DataformResult.success(loginDatas);
						} else {// 登录失败
							sysLoginLogService.saveLoginLog(userName, "1", "2", "登录失败，用户名或密码错误！");
							logger.info(XwyRspCon.FLAG_LOGIN_ERROR.getMsg());
							return DataformResult.failure(XwyRspCon.FLAG_LOGIN_ERROR);
						}
					} else {
						sysLoginLogService.saveLoginLog(userName, "1", "2", "验证码错误！");
						return DataformResult.failure(XwyRspCon.FLAG_LOGIN_ERROR);
					}
				} else {
					sysLoginLogService.saveLoginLog(userName, "1", "2", "验证码不能为空！");
					return DataformResult.failure(XwyRspCon.FLAG_LOGIN_ERROR);
				}
			} else {// 登录类型错误
				logger.info(XwyRspCon.FLAG_LOGIN_TYPE_ERROR.getMsg());
				return DataformResult.failure(XwyRspCon.FLAG_LOGIN_TYPE_ERROR);
			}
		} else {// 登录类型不能为空
			logger.info(XwyRspCon.FLAG_LOGIN_TYPE_NOTNULL.getMsg());
			return DataformResult.failure(XwyRspCon.FLAG_LOGIN_TYPE_NOTNULL);
		}
	}
}
