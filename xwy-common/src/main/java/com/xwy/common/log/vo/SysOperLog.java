package com.xwy.common.log.vo;

import javax.persistence.Column;

import com.xwy.common.base.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 操作日志记录表 oper_log
 * 
 * @author xiangwy
 * @date: 2020-10-17 10:18:11
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SysOperLog对象", description = "操作日志记录表")
public class SysOperLog extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 操作模块 */
	@Column(name = "TITLE")
	@ApiModelProperty(value = "操作模块")
	private String title;

	/** 业务类型（0其它 1新增 2修改 3删除） */
	@Column(name = "BUSINESS_TYPE")
	@ApiModelProperty(value = "业务类型:0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据")
	private Integer businessType;

	/** 业务类型数组 */
	@Column(name = "BUSINESS_TYPES")
	@ApiModelProperty(value = "业务类型:0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据")
	private Integer[] businessTypes;

	/** 请求方法 */
	@Column(name = "METHOD")
	@ApiModelProperty(value = "请求方法")
	private String method;

	/** 请求方式 */
	@Column(name = "REQUEST_METHOD")
	@ApiModelProperty(value = "请求方式")
	private String requestMethod;

	/** 操作类别（0其它 1后台用户 2手机端用户） */
	@Column(name = "OPERATOR_TYPE")
	@ApiModelProperty(value = "操作类别:0=其它,1=后台用户,2=手机端用户")
	private Integer operatorType;

	/** 请求url */
	@Column(name = "OPER_URL")
	@ApiModelProperty(value = "请求地址")
	private String operUrl;

	/** 操作地址 */
	@Column(name = "OPER_IP")
	@ApiModelProperty(value = "操作地址")
	private String operIp;

	/** 请求参数 */
	@Column(name = "OPER_PARAM")
	@ApiModelProperty(value = "请求参数")
	private String operParam;

	/** 返回参数 */
	@Column(name = "JSON_RESULT")
	@ApiModelProperty(value = "返回参数")
	private String jsonResult;

	/** 操作状态（0正常 1异常） */
	@Column(name = "STATUS")
	@ApiModelProperty(value = "状态:0=正常,1=异常")
	private Integer status;

	/** 错误消息 */
	@Column(name = "ERROR_MSG")
	@ApiModelProperty(value = "错误消息")
	private String errorMsg;

}
