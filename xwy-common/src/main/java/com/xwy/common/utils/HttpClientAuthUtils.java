package com.xwy.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * httpclient 调用Auth工具包
 * 
 * @author xiangwy
 * @date: 2020-12-02 10:10:13
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
public class HttpClientAuthUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpClientAuthUtils.class);

	/**
	 * doGet方法 不带认证auth的请求
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 10:10:25
	 * @param url
	 * @param _params
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url, Map<String, Object> _params) throws Exception {
		return doGet(url, "utf-8", _params, null, null, null);
	}

	/**
	 * post 请求（不带认证信息的普通请求）
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 10:10:32
	 * @param url
	 * @param _params
	 * @return
	 */
	public static String doPost(String url, Map<String, Object> _params) {
		return doPost(url, _params, "utf-8", true, null, null, null);
	}

	/**
	 * doPostByjson 方法
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 10:10:42
	 * @param url       请求地址
	 * @param jsonParam json 格式
	 * @return
	 */
	public static String doPostByJson(String url, String jsonParam) {
		return doPostByjson(url, jsonParam, "utf-8", true, null, null, null);
	}

	/**
	 * doPostByObject 方法
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 10:11:00
	 * @param url
	 * @param obj
	 * @return
	 */
	public static String doPostByObject(String url, Object obj) {
		return doPostByjson(url, JsonMapper.toJsonString(obj), "utf-8", true, null, null, null);
	}

	/**
	 * doGetByAuth方法 带认证auth的请求
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 10:11:11
	 * @param url
	 * @param _params
	 * @param authKey
	 * @param authPre
	 * @param authV
	 * @return
	 */
	public static String doGetByAuth(String url, Map<String, Object> _params, String authKey, String authPre,
			String authV) {
		return doGet(url, "utf-8", _params, authKey, authPre, authV);
	}

	/**
	 * doPostByAuth 方法
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 10:11:22
	 * @param url
	 * @param _params
	 * @param authKey
	 * @param authPre
	 * @param authV
	 * @return
	 */
	public static String doPostByAuth(String url, Map<String, Object> _params, String authKey, String authPre,
			String authV) {
		return doPost(url, _params, "utf-8", true, authKey, authPre, authV);
	}

	/**
	 * doPostByjsonAndAuth 方法
	 * 
	 * @descript：TODO
	 * @param url     请求地址
	 * @param _params
	 * @return
	 * @return String
	 * @author xiangwy
	 * @date 2019年1月23日-下午5:59:29
	 */
	public static String doPostByJsonAndAuth(String url, String jsonParam, String authKey, String authPre,
			String authV) {
		return doPostByjson(url, jsonParam, "utf-8", true, authKey, authPre, authV);
	}

	/**
	 * doPostByObject 方法
	 * 
	 * @descript：TODO
	 * @param url     请求地址
	 * @param _params json 格式
	 * @return
	 * @return String
	 * @author xiangwy
	 * @throws PmsException
	 * @date 2019年1月23日-下午5:59:29
	 */
	public static String doPostByObjectAndAuth(String url, Object obj, String authKey, String authPre, String authV) {
		return doPostByjson(url, JsonMapper.toJsonString(obj), "utf-8", true, authKey, authPre, authV);
	}

	/**
	 * httpClient的get请求方式
	 * 
	 * @param url     请求地址
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url, String charset, Map<String, Object> _params, String authKey, String authPre,
			String authV) {
		// 在返回响应消息使用编码(utf-8或gb2312)
		String response = null;
		try {
			url = dealUrl(url, _params);
			logger.info(url);
			HttpClient client = new HttpClient();
			GetMethod method = new GetMethod(url);

			// 设置authV
			if (BlankUtils.isNotBlank(authV)) {
				method.setRequestHeader(authKey, authPre + authV);
			}

			if (null == url || !url.startsWith("http")) {
				throw new Exception("请求地址格式不对");
			}
			// 设置请求的编码方式
			if (null != charset) {
				method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
			} else {
				method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + "utf-8");
			}
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {// 打印服务器返回的状况
				System.out.println("Method failed: " + method.getStatusLine());
			}
			// 返回响应消息
			byte[] responseBody = method.getResponseBodyAsString().getBytes(method.getResponseCharSet());
			response = new String(responseBody, "utf-8");
			// 释放连接
			method.releaseConnection();
		} catch (Exception e) {
			logger.error("请求异常", e);
		}
		return response;
	}

	/**
	 * 执行HTTP POST请求，返回请求响应的HTML
	 * 
	 * @param url请求的URL地址
	 * @param params请求的查询参 ,可以为null
	 * @param charset      字符
	 * @param pretty       是否美化
	 * @return 返回请求响应的HTML
	 */
	public static String doPost(String url, Map<String, Object> _params, String charset, boolean pretty, String authKey,
			String authPre, String authV) {

		StringBuffer response = new StringBuffer();
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		// 设置authV
		if (BlankUtils.isNotBlank(authV)) {
			method.setRequestHeader(authKey, authPre + authV);
		}
		// 设置Http Post数据
		if (_params != null) {
			for (Map.Entry<String, Object> entry : _params.entrySet()) {
				method.setParameter(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(), charset));
				String line;
				while ((line = reader.readLine()) != null) {
					if (pretty) {
						response.append(line).append(System.getProperty("line.separator"));
					} else {
						response.append(line);
					}
				}
				reader.close();
			}
		} catch (IOException e) {
			logger.error("请求异常", e);
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}

	/**
	 * 执行HTTP POST请求，参数为reqestBoby,返回请求响应的HTML
	 * 
	 * @param url
	 * @param jsonParam（json格式）
	 * @param charset
	 * @param pretty
	 * @return
	 * @throws PmsException
	 */
	public static String doPostByjson(String url, String jsonParam, String charset, boolean pretty, String authKey,
			String authPre, String authV) {

		StringBuffer response = new StringBuffer();
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		// 设置authV
		if (BlankUtils.isNotBlank(authV)) {
			method.setRequestHeader(authKey, authPre + authV);
		}
		method.addRequestHeader("Content-Type", "application/json");
		method.addRequestHeader("charset", charset);
		try {
			// 设置 Post数据
			// method.setRequestBody(jsonParam);
			RequestEntity se = new StringRequestEntity(jsonParam, "application/json", "UTF-8");
			method.setRequestEntity(se);

			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(), charset));
				String line;
				while ((line = reader.readLine()) != null) {
					if (pretty) {
						response.append(line).append(System.getProperty("line.separator"));
					} else {
						response.append(line);
					}
				}
				reader.close();
			}
		} catch (Exception e) {
			logger.error("请求异常", e);
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}

	// 拼接url后面的参数
	private static String dealUrl(String url, Map<String, Object> _params) {
		if (BlankUtils.isNotBlank(_params)) {
			for (Map.Entry<String, Object> entry : _params.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (url.contains("?")) {
					url += "&" + key + "=" + value;
				} else {
					url += "?" + key + "=" + value;
				}
			}
		}
		return url;
	}
}