package com.xwy.common.utils;

import java.io.Serializable;

import com.xwy.common.constant.XwyRspCon;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 統一結果列表返回MODEL
 * 
 * @author xiangwy
 * @date: 2020-12-01 12:03:27
 * @Copyright: Copyright (c) 2020
 * @Company: Xwy科技股份有限公司
 * @Version: V1.0
 */
@Setter
@Getter
@ToString
public class DataformResult<T> implements Serializable {
	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 8968766969795754248L;

	/**
	 * 信息
	 */
	private String message;

	/**
	 * 状态码
	 */

	private int statusCode;

	/**
	 * 是否成功
	 */
	private boolean success;

	private T object;

	public static <T> DataformResult<T> success() {
		DataformResult<T> result = new DataformResult<>();
		result.setSuccess(true);
		result.setStatusCode(200);
		return result;
	}

	public static <T> DataformResult<T> success(T data) {
		DataformResult<T> result = new DataformResult<>();
		result.setSuccess(true);
		result.setStatusCode(200);
		result.setObject(data);
		return result;
	}

	public static <T> DataformResult<T> failure() {
		DataformResult<T> result = new DataformResult<>();
		result.setSuccess(false);
		return result;
	}

	public static <T> DataformResult<T> failure(String message) {
		DataformResult<T> result = new DataformResult<>();
		result.setSuccess(false);
		result.setMessage(message);
		return result;
	}

	public static <T> DataformResult<T> failure(XwyRspCon rspCon) {
		DataformResult<T> result = new DataformResult<>();
		result.setSuccess(false);
		result.setStatusCode(rspCon.getCode());
		result.setMessage(rspCon.getMsg());
		return result;
	}

	public static <T> DataformResult<T> failure(int statusCode, String message) {
		DataformResult<T> result = new DataformResult<>();
		result.setStatusCode(statusCode);
		result.setSuccess(false);
		result.setMessage(message);
		return result;
	}

	public static <T> DataformResult<T> failure(String message, T data) {
		DataformResult<T> result = new DataformResult<>();
		result.setSuccess(false);
		result.setMessage(message);
		result.setObject(data);
		return result;
	}
}
