package com.xwy.auth.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xwy.auth.feign.SysLoginLogService;
import com.xwy.auth.manager.TokenManager;
import com.xwy.auth.properties.JwtProperties;
import com.xwy.auth.util.JwtTokenUtil;
import com.xwy.auth.validator.IReqValidator;
import com.xwy.auth.vo.AuthRequestVo;
import com.xwy.common.utils.BlankUtils;
import com.xwy.common.utils.jwt.TokenUtil;
import com.xwy.framework.constant.XwyRspCon;
import com.xwy.framework.utils.DataformResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 请求验证
 * 
 * @author xiangwy
 * @date: 2020-12-03 11:39:33
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
@RestController
@RequestMapping("/auth")
@Api(value = "登录验证接口", tags = "登录验证接口")
public class AuthController {
	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	// 注解切换验证方式 （1，simpleValidator为写死的验证；2，dbValidator为数据库中进行验证）
	@Resource(name = "dbValidator")
	private IReqValidator reqValidator;

	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private JwtProperties jwtProperties;

	@Autowired
	private SysLoginLogService sysLoginLogService;// 系统用户登录日志接口

	@SuppressWarnings("unchecked")
	@PostMapping(value = "auth")
	@ApiOperation(value = "登录验证", notes = "登录验证  <br>" + " 1，登录参数 <br>"
			+ " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;logintype 登录类型: 1 为后台系统用户认证登录 ；username：用户名；password：密码；capture：验证码；captchaId：验证码ID）<br>"
			+ " 2，fans后台系统用户认证 返回值描述如下：<br>"
			+ " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{\"code\":返回状态码,\"msg\":返回消息,\"datas(返回数据)\":{\"menuData(菜单数据)\":[],\"userData(用户数据)\":{},\"permsData(按钮权限数据)\":[],\"authData(认证信息，token以及过期时间等数据)\":{}}}<br>", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "authRequestVo", dataType = "authRequestVo", value = "authRequestVo实体的json类型( fans后台系统用户认证方式)<br>{\"userName\":\"admin\",\"password\":\"123456\",\"logintype\":\"1\",\"captcha\":\"9aqd\",\"captchaId\":\"4a9cd3a2-ab45-46c6-9870-b91659c88fd51579329664992\"}") })
	public DataformResult<Map<String, Object>> auth(@RequestBody AuthRequestVo authRequestVo) {
		try {
			DataformResult<Map<String, Object>> auth = reqValidator.validate(authRequestVo);
			final String randomKey = jwtTokenUtil.getRandomKey();
			if (XwyRspCon.SUCCESS.getCode() == auth.getStatusCode()) {// 验证成功
				String loginType = authRequestVo.getLogintype();
				String userName = authRequestVo.getUserName();
				Map<String, Object> datas = (Map<String, Object>) auth.getObject();
				Map<String, Object> userInfo = (Map<String, Object>) datas.get("userInfo");
				String id = "";
				if ("1".equals(loginType)) {// 系统用户登录
					id = String.valueOf(userInfo.get("id"));
				}
				String userInfoStr = id + ";;" + userName + ";;" + loginType;
				final String token = jwtTokenUtil.generateToken(userInfoStr, randomKey);
				// 存储到redis或者db中
				tokenManager.createRelationship(authRequestVo.getUserName(), token);
				// 设置认证信息
				Map<String, Object> authData = new HashMap<>();
				// 设置过期时间
				authData.put("expiration", jwtProperties.getExpiration());
				authData.put("token", token);
				authData.put("randomKey", randomKey);
				datas.put("authData", authData);
			}
			return auth;
		} catch (Exception e) {
			logger.error("系统异常(检查 redis 或 mysql 是否正常 )！" + e);
			return DataformResult.failure();
		}
	}

	/**
	 * 注销
	 *
	 * @return
	 */
	@PostMapping(value = "/logout")
	@ResponseBody
	@ApiOperation(value = "用户注销", notes = "用户注销")
	public DataformResult<String> logout() {
		// TODO 操蛋的JWT不能从服务端destroy token， logout目前只能在客户端的cookie 或
		// localStorage/sessionStorage remove token
		// TODO
		// 准备用jwt生成永久的token，再结合redis来实现Logout。具体是把token的生命周期交给redis来管理，jwt只负责生成token
		int code = 1;
		String msg = "用户退出失败！";
		try {
			// 多端登录，会有多个同一用户名但token不一样的键值对在redis中存在，所以只能通过token删除
			// tokenManager.delRelationshipByKey(user.getUsername());
			String authToken = TokenUtil.getToken();
			if (BlankUtils.isNotBlank(authToken)) {
				String userName = TokenUtil.getUsernameFromToken(authToken);
				tokenManager.delRelationshipByToken(authToken);// 注销成功
				logger.info("用户退出登录成功！");
				sysLoginLogService.saveLoginLog(userName, "2", "1", "用户退出登录成功！");
				return DataformResult.success();
			} else {
				logger.info("用户退出登录失败！");
				return DataformResult.failure(code, msg);
			}
		} catch (Exception e) {
			msg = "服务内部异常！";
			code = 2;
			logger.error("服务内部异常！" + e);
			return DataformResult.failure(code, msg);
		}
	}

	/**
	 * token 校验
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-03 11:36:31
	 * @param authToken
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/checkToken")
	@ApiOperation(value = "token 校验", notes = "token 校验")
	public DataformResult<String> checkToken(String authToken) {
		int code = 701;
		String msg = "token未进行认证！";
		if (BlankUtils.isNotBlank(authToken)) {
			try {
				String key = tokenManager.getKey(authToken);
				// 是否进行了认证操作
				if (BlankUtils.isNotBlank(key)) {// 不为空的时候则进行了验证操作
					boolean flag = jwtTokenUtil.isTokenExpired(authToken);
					if (flag) {// 过期则返回为true
						code = 700;
						msg = "token已过期！";
					} else {// 未过期则返回为false(认证通过)
						String userInfoStrFromToken = jwtTokenUtil.getUserInfoStrFromToken(authToken);
						return DataformResult.success(userInfoStrFromToken);
					}
				}
			} catch (Exception e) {
				logger.error("token失败认证！", e);
			}
		}
		return DataformResult.failure(code, msg);

	}

	/**
	 * 验证是否登录过
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-03 11:38:39
	 * @param request
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/isLogined")
	@ApiOperation(value = "是否登录", notes = "是否登录")
	public DataformResult<String> isLogined(HttpServletRequest request) {
		int code = 705;
		String msg = "未登录！";
		// 从头部获取token信息
		String requestHeader = request.getHeader("Authorization");
		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			String authToken = requestHeader.substring(7);
			if (BlankUtils.isNotBlank(authToken)) {
				try {
					String key = tokenManager.getKey(authToken);
					// 是否进行了认证操作
					if (BlankUtils.isNotBlank(key)) {// 不为空的时候则进行了验证操作
						boolean flag = jwtTokenUtil.isTokenExpired(authToken);
						if (!flag) {// 过期则返回为true
							String userInfoStrFromToken = jwtTokenUtil.getUserInfoStrFromToken(authToken);
							return DataformResult.success(userInfoStrFromToken);
						}
					}
				} catch (Exception e) {
					logger.error("token失败认证！", e);
				}
			}
		}
		return DataformResult.failure(code, msg);
	}

}
