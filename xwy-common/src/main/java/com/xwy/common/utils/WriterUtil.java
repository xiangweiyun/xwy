package com.xwy.common.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 把对象转成json格式字符串返回
 * 
 * @author xiangwy
 * @date: 2020-12-02 09:20:11
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
public class WriterUtil {

	/**
	 * 客户端返回JSON字符串
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:20:36
	 * @param response
	 * @param object
	 * @return
	 */
	public static String renderString(HttpServletResponse response, Object object) {
		return renderString(response, JsonMapper.toJsonString(object), "application/json");
	}

	/**
	 * 空值时也返回属性
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:20:45
	 * @param response
	 * @param object
	 * @return
	 */
	public static String renderStringDealNull(HttpServletResponse response, Object object) {
		return renderString(response,
				JSONObject.toJSONString(object, SerializerFeature.WriteMapNullValue,
						SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullNumberAsZero),
				"application/json");
	}

	/**
	 * 客户端返回字符串 1、 普通文本：text/plain <br/>
	 * 2 、HTML: text/html <br/>
	 * 3 、XML: text/xml <br/>
	 * 4 、javascript: text/javascript <br/>
	 * 5、 json: application/json
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:22:25
	 * @param response
	 * @param string
	 * @param type
	 * @return
	 */
	public static String renderString(HttpServletResponse response, String string, String type) {
		try {
			response.setContentType(type);
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	public static Object renderScript(HttpServletResponse response, String script) {
		try {
			response.reset();
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			script = "<script type=\"text/javascript\">" + script + "</script>";
			response.getWriter().print(script);
			return null;
		} catch (IOException e) {
			return null;
		}
	}
}
