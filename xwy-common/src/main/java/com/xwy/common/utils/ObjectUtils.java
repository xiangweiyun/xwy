package com.xwy.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对象操作工具类, 继承org.apache.commons.lang3.ObjectUtils类
 * 
 * @author xiangwy
 * @date: 2020-12-02 09:33:21
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {
	private static final Logger log = LoggerFactory.getLogger(ObjectUtils.class);

	/**
	 * 注解到对象复制，只复制能匹配上的方法。
	 * 
	 * @param annotation
	 * @param object
	 */
	public static void annotationToObject(Object annotation, Object object) {
		if (annotation != null) {
			Class<?> annotationClass = annotation.getClass();
			Class<?> objectClass = object.getClass();
			for (Method m : objectClass.getMethods()) {
				if (StringUtils.startsWith(m.getName(), "set")) {
					try {
						String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
						Object obj = annotationClass.getMethod(s).invoke(annotation);
						if (obj != null && !"".equals(obj.toString())) {
							if (object == null) {
								object = objectClass.newInstance();
							}
							m.invoke(object, obj);
						}
					} catch (Exception e) {
						// 忽略所有设置失败方法
						log.error("注解到对象复制异常", e);
					}
				}
			}
		}
	}

	/**
	 * 序列化对象
	 * 
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			if (object != null) {
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(object);
				return baos.toByteArray();
			}
		} catch (Exception e) {
			log.error("序列化对象异常", e);
		}
		return null;
	}

	/**
	 * 反序列化对象
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			if (bytes != null && bytes.length > 0) {
				bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);
				return ois.readObject();
			}
		} catch (Exception e) {
			log.error("反序列化对象异常", e);
		}
		return null;
	}

	/**
	 * 对象转Map（父属性不转）
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:33:39
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> ConvertObjToMap(Object obj) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		if (obj == null) {
			return reMap;
		}
		Field[] fields = obj.getClass().getDeclaredFields();
		try {
			for (int i = 0; i < fields.length; i++) {
				try {
					Field f = obj.getClass().getDeclaredField(fields[i].getName());
					f.setAccessible(true);
					Object o = f.get(obj);
					reMap.put(fields[i].getName(), o);
				} catch (NoSuchFieldException e) {
					log.error("对象转Map（父属性不转）异常", e);
				} catch (IllegalArgumentException e) {
					log.error("对象转Map（父属性不转）异常", e);
				} catch (IllegalAccessException e) {
					log.error("对象转Map（父属性不转）异常", e);
				}
			}
		} catch (SecurityException e) {
			log.error("对象转Map（父属性不转）异常", e);
		}
		return reMap;
	}

	/**
	 * 对象转map（父属性也转）
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:35:14
	 * @param javaBean
	 * @return
	 */
	public static Map<String, Object> javaBean2Map(Object javaBean) {
		Map<String, Object> map = new HashMap<>();
		Method[] methods = javaBean.getClass().getMethods(); // 获取所有方法
		try {
			for (Method method : methods) {
				if (method.getName().startsWith("get")) {
					String field = method.getName(); // 拼接属性名
					field = field.substring(field.indexOf("get") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					Object value = method.invoke(javaBean, (Object[]) null); // 执行方法
					map.put(field, value);
				}
			}
		} catch (IllegalAccessException e) {
			log.error("对象转Map（父属性也转）异常", e);
		} catch (IllegalArgumentException e) {
			log.error("对象转Map（父属性也转）异常", e);
		} catch (InvocationTargetException e) {
			log.error("对象转Map（父属性也转）异常", e);
		}
		return map;
	}

}
