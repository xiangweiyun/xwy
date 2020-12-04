package com.xwy.boot.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwy.boot.service.SysUserService;
import com.xwy.common.utils.BlankUtils;
import com.xwy.common.utils.DataformResult;
import com.xwy.common.utils.SecurityUtils;
import com.xwy.common.utils.jwt.TokenUtil;
import com.xwy.entity.SysUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统_用户控制器.
 * 
 * @author xiangwy
 * @date: 2020-12-01
 * @Copyright: Copyright (c) 2020
 * @Company: Xwy科技股份有限公司
 * @Version: V1.0
 */
@RestController
@RequestMapping("/sysUser")
@Api(tags = "系统_用户")
public class SysUserController {
	private final Logger logger = LoggerFactory.getLogger(SysUserController.class);
	@Autowired
	private SysUserService sysUserService;

	@ApiOperation(value = "系统_用户-新增", notes = "系统_用户-新增")
	@PostMapping("/save")
	public DataformResult<String> save(@RequestBody SysUser sysUser) {
		if (null == sysUser.getId()) {
			sysUserService.save(sysUser);
		} else {
			sysUserService.updateById(sysUser);
		}
		return DataformResult.success();
	}

	@ApiOperation(value = "系统_用户-删除", notes = "系统_用户-刪除")
	@PostMapping("/remove/{id}")
	public DataformResult<String> removeById(@PathVariable("id") Long id) {
		sysUserService.removeById(id);
		return DataformResult.success();
	}

	@ApiOperation(value = "系统_用户-根据ID获取", notes = "系统_用户-根据ID获取")
	@GetMapping("/{id}")
	public DataformResult<SysUser> getById(@PathVariable("id") Long id) {
		SysUser sysUser = sysUserService.getById(id);
		return DataformResult.success(sysUser);
	}

	/**
	 * 重置密码
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-03 09:14:58
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "系统_用户-重置密码", notes = "系统_用户-重置密码")
	@PutMapping("/resetPwd")
	public DataformResult<String> resetPwd(Long id) {
		try {
			if (BlankUtils.isNotBlank(id)) {
				sysUserService.resetPwd(id);
				return DataformResult.success();
			} else {
				return DataformResult.failure("系统_用户重置密码异常！IDS不能为空");
			}
		} catch (Exception e) {
			logger.error("系统_用户重置密码异常！", e);
			return DataformResult.failure("系统_用户重置密码异常！");
		}
	}

	/**
	 * 修改密码
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-03 09:15:53
	 * @param pwd
	 * @param newpwd
	 * @param newpwd2
	 * @return
	 */
	@ApiOperation(value = "系统_用户-修改密码", notes = "系统_用户-修改密码")
	@PutMapping("/updatePwd")
	public DataformResult<String> updatePwd(String pwd, String newpwd, String newpwd2) {
		try {
			if (BlankUtils.isBlank(pwd)) {
				return DataformResult.failure("系统_用户修改密码异常！原密码不能为空");
			}
			if (BlankUtils.isBlank(newpwd)) {
				return DataformResult.failure("系统_用户修改密码异常！新密码不能为空");
			}
			if (BlankUtils.isBlank(newpwd2)) {
				return DataformResult.failure("系统_用户修改密码异常！重复新密码不能为空");
			}
			if (!newpwd.equals(newpwd2)) {
				return DataformResult.failure("系统_用户修改密码异常！新密码和重复新密码必须一致");
			}
			Long userId = Long.parseLong(TokenUtil.getUserId());
			SysUser sysUser = sysUserService.getById(userId);
			if (SecurityUtils.MD5Encode(pwd).equals(sysUser.getPassword())) {
				sysUserService.updatePwd(userId, newpwd);
				return DataformResult.success();
			} else {
				return DataformResult.failure("系统_用户修改密码异常！原密码不正确");
			}
		} catch (Exception e) {
			logger.error("系统_用户重置密码异常！", e);
			return DataformResult.failure("系统_用户重置密码异常！");
		}
	}

	/**
	 * 根据username和password以及验证码查询用户
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-03 09:18:41
	 * @param username
	 * @param password
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "系统_用户-登录", notes = "系统_用户-登录")
	@GetMapping("/getByLoginInfo")
	public DataformResult<Map<String, Object>> getByLoginInfo(String username, String password,
			HttpServletRequest request) {
		Map<String, Object> loginDatas = new HashMap<>();
		try {
			SysUser sysUser = null;
			if (BlankUtils.isNotBlank(username) && BlankUtils.isNotBlank(password)) {
				// 根据用户名和密码查询
				sysUser = sysUserService.getByUsernameAndPassword(username, password);
				if (BlankUtils.isBlank(sysUser)) {// 根据邮箱和密码查询
					sysUser = sysUserService.getByEmailAndPassword(username, password);
				}
				if (BlankUtils.isBlank(sysUser)) {// 根据手机号和密码查询
					sysUser = sysUserService.getByMobileAndPassword(username, password);
				}
			}
			if (BlankUtils.isNotBlank(sysUser)) {// 不为空表示登录成功
				loginDatas = sysUserService.getLoginDatas(sysUser.getId(), true);
			}
		} catch (Exception e) {
			logger.error("根据username和password查询用户异常", e);
			return DataformResult.failure("根据username和password查询用户异常");
		}
		return DataformResult.success(loginDatas);
	}
}
