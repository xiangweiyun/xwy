package com.xwy.common.config;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xwy.common.utils.jwt.TokenUtil;

/**
 * 设置填充值
 * 
 * @author xiangwy
 * @date: 2020-12-01 15:59:52
 * @Copyright: Copyright (c) 2020
 * @Company: Xwy科技股份有限公司
 * @Version: V1.0
 */
@Component
public class MetaHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		this.setFieldValByName("createDate", LocalDateTime.now(), metaObject);
		this.setFieldValByName("createBy", Long.parseLong(TokenUtil.getUserId()), metaObject);
		this.setFieldValByName("updateDate", LocalDateTime.now(), metaObject);
		this.setFieldValByName("updateBy", Long.parseLong(TokenUtil.getUserId()), metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.setFieldValByName("updateDate", LocalDateTime.now(), metaObject);
		this.setFieldValByName("updateBy", Long.parseLong(TokenUtil.getUserId()), metaObject);
	}

}
