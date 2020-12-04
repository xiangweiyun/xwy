package com.xwy.boot.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwy.boot.service.SysLoginLogService;
import com.xwy.common.utils.DataformResult;
import com.xwy.entity.SysLoginLog;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 用户登录日志控制器.
 * 
 * @author xiangwy
 * @date: 2020-12-03
 * @Copyright: Copyright (c) 2020
 * @Company: Xwy科技股份有限公司
 * @Version: V1.0
 */
@RestController
@RequestMapping("/sysLoginLog")
@Api(tags = "用户登录日志")
public class SysLoginLogController {
	private final Logger logger = LoggerFactory.getLogger(SysLoginLogController.class);
	@Autowired
	private SysLoginLogService sysLoginLogService;

	@ApiOperation(value = "用户登录日志-新增", notes = "用户登录日志-新增")
	@PostMapping("/save")
	public DataformResult<String> save(@RequestBody SysLoginLog sysLoginLog) {
		if (null == sysLoginLog.getId()) {
			sysLoginLogService.save(sysLoginLog);
		} else {
			sysLoginLogService.updateById(sysLoginLog);
		}
		return DataformResult.success();
	}

	@ApiOperation(value = "用户登录日志-删除", notes = "用户登录日志-刪除")
	@PostMapping("/remove/{id}")
	public DataformResult<String> removeById(@PathVariable("id") Long id) {
		sysLoginLogService.removeById(id);
		return DataformResult.success();
	}

	@ApiOperation(value = "用户登录日志-根据ID获取", notes = "用户登录日志-根据ID获取")
	@GetMapping("/{id}")
	public DataformResult<SysLoginLog> getById(@PathVariable("id") Long id) {
		SysLoginLog sysLoginLog = sysLoginLogService.getById(id);
		return DataformResult.success(sysLoginLog);
	}

	/**
	 * 分页查询登录日志
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-03 10:22:43
	 * @param pageNum  开始页
	 * @param pageSize 每页数
	 * @return
	 */
	@ApiOperation(value = "用户登录日志-根据ID获取", notes = "用户登录日志-根据ID获取")
	@GetMapping("/pageSysLoginLog")
	public DataformResult<IPage<SysLoginLog>> pageSysLoginLog(int pageNum, int pageSize) {
		Page<SysLoginLog> pageParam = new Page<>(pageNum, pageSize);
		IPage<SysLoginLog> page = sysLoginLogService.page(pageParam);
		return DataformResult.success(page);
	}

	@ApiOperation(value = "用户登录日志-保存登录日志", notes = "用户登录日志-保存登录日志")
	@ApiImplicitParams({ @ApiImplicitParam(required = false, name = "logon_id", value = "登录账号", dataType = "String"),
			@ApiImplicitParam(required = false, name = "operType", value = "操作类型（1,登录;2,登出）", dataType = "String"),
			@ApiImplicitParam(required = false, name = "resultType", value = "登录结果类型(1,成功；2失败)", dataType = "String"),
			@ApiImplicitParam(required = false, name = "msg", value = "备注", dataType = "String") })
	@PostMapping("/saveLoginLog")
	public DataformResult<String> saveLoginLog(HttpServletRequest request, String logonId, String operType,
			String resultType, String msg) {
		sysLoginLogService.saveLoginLog(request, logonId, operType, resultType, msg);
		return DataformResult.success();
	}
}
