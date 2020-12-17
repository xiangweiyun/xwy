package com.xwy.common.log.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.xwy.common.log.vo.SysOperLog;

/**
 * 异步调用日志服务
 * 
 * @author xiangwy
 * @date: 2020-10-17 10:16:49
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
@Service
public class AsyncLogService {

	/**
	 * 保存系统日志记录
	 */
	@Async
	public void saveSysLog(SysOperLog sysOperLog) {

	}
}
