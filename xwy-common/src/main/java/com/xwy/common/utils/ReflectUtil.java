package com.xwy.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 反射工具类
 * 
 * @author xiangwy
 * @date: 2020-12-02 09:27:10
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
public class ReflectUtil {

	/**
	 * 日志对象
	 */
	protected final static Log log = LogFactory.getLog(ReflectUtil.class);

	/**
	 * 把object对象非collection类型的属性转换成string输出
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:27:20
	 * @param object
	 * @return
	 */
	public static String convertFieldToString(Object object) {
		StringBuffer stringBuffer = new StringBuffer();
		Field[] fields = object.getClass().getDeclaredFields();
		if (fields.length > 0) {
			Field field = fields[0];
			if (!Collection.class.isAssignableFrom(field.getType())) {
				Object value = getValue(field, object);
				// 剔除是空值的属性
				if (isNeedToConvert(value)) {
					stringBuffer.append(field.getName()).append(":").append(getValue(field, object)); // 处理第一个field
				}
			}

			// 处理剩余的field
			for (int i = 1; i < fields.length; i++) {
				field = fields[i];
				// 剔除那些是数组的属性
				if (!Collection.class.isAssignableFrom(field.getType())) {
					Object value = getValue(field, object);
					// 剔除是空值的属性
					if (isNeedToConvert(value)) {
						stringBuffer.append(", ").append(field.getName()).append(":").append(value);
					}
				}
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * 判断 value是否必须转换
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:27:38
	 * @param value
	 * @return
	 */
	public static Boolean isNeedToConvert(Object value) {
		if (value instanceof String && value.toString().length() > 0) {
			return true;
		}
		if (value instanceof Double) {
			return true;
		}
		if (value instanceof Long) {
			return true;
		}
		if (value instanceof Integer) {
			return true;
		}
		if (value instanceof Boolean) {
			return true;
		}
		if (value instanceof Date) {
			return true;
		}
		return false;
	}

	/**
	 * 获取object的某一个field的值
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:27:53
	 * @param field
	 * @param object
	 * @return
	 */
	protected static Object getValue(Field field, Object object) {
		field.setAccessible(true); // 设置私有属性可访问
		try {
			return field.get(object);
		} catch (IllegalArgumentException e) {
			log.error("获取field属性值异常", e);
		} catch (IllegalAccessException e) {
			log.error("获取field属性值异常", e);
		}
		return null;
	}

	/**
	 * 收集类的所有字段
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:28:43
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<Field> getFields(Class clazz) {
		List<Field> fields = new ArrayList<Field>();
		do {
			for (Field field : clazz.getDeclaredFields()) {
				fields.add(field);
			}
			clazz = clazz.getSuperclass();

		} while (clazz.getSuperclass() == Object.class);

		return fields;
	}

	/**
	 * 收集类的所有字段名称
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:29:08
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> getFieldNameList(Class clazz) {
		List<Field> fields = getFields(clazz);
		List<String> fieldNames = new ArrayList<String>();
		for (Field field : fields) {
			fieldNames.add(field.getName());
		}
		return fieldNames;
	}

	/**
	 * 调用对象的方法, 返回-1表示出现错误
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:29:49
	 * @param object
	 * @param methodName
	 * @param args
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object invokeMethod(Object object, String methodName, Object... args) {
		Class clazz = object.getClass();
		Method method = null;
		// 有参数方法
		if (args != null && args.length > 0) {
			Class[] argsClass = new Class[args.length];
			for (int i = 0; i < args.length; i++) {
				argsClass[i] = args[i].getClass();
			}
			try {
				method = clazz.getMethod(methodName, argsClass);
				return method.invoke(object, args);
			} catch (Exception e) {
				log.error("调用对象的方法异常", e);
				return -1;
			}
		} else {
			try {
				method = clazz.getMethod(methodName, new Class[0]);
				return method.invoke(object, new Object[0]);
			} catch (Exception e) {
				log.error("调用对象的方法异常", e);
				return -1;
			}
		}
	}

	/**
	 * 获得子类的泛型父类参数的实质类型
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:30:31
	 * @param clazz
	 * @param index
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Class getSuperClassGenricType(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass(); // 得到泛型父类
		// 如果没有实现ParameterizedType, 即不支持泛型，直接返回object的class
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			throw new RuntimeException("你输入的索引:" + (index < 0 ? "不能小于0!" : "超出了参数总数"));
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class) params[index];
	}

	/**
	 * 通过反射调用实体的get方法获取值, attributeName必须和实体的属性名字一样
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:30:40
	 * @param object
	 * @param attributeName
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object invokeGetMethod(Object object, String attributeName) {
		if (StringUtils.isEmpty(attributeName) || object == null) {
			log.warn("对象或者属性名称为空!");
			return null;
		}
		Class clazz = object.getClass();
		Method method = null;
		String methodName = "get" + StringUtils.capitalize(attributeName);
		try {
			method = clazz.getMethod(methodName, new Class[0]);
			Object res = method.invoke(object, new Object[0]);
			if (res instanceof Date) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
				return sdf.format((Date) res); // 默认日期的处理
			}
			return res;
		} catch (Exception e) {
			log.error("执行：" + methodName + "() 方法时出错!");
			return null;
		}
	}

	/**
	 * 通过反射,获得Field泛型参数的实际类型. 如: public Map<String, Buyer> names;
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:31:06
	 * @param field
	 * @param index
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Class getFieldGenericType(Field field, int index) {
		Type genericFieldType = field.getGenericType();
		if (genericFieldType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) genericFieldType;
			Type[] fieldArgTypes = aType.getActualTypeArguments();
			if (index >= fieldArgTypes.length || index < 0) {
				throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
			}
			return (Class) fieldArgTypes[index];
		}
		return Object.class;
	}

	/**
	 * 获取泛型对象参数实例, 如果clazz不支持泛型，则返回null，比如aaa<T>, 获取的是T示例，参数是aaa对象的class
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 09:31:33
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings(value = "rawtypes")
	public static Object getGenericInstance(Class clazz) {
		Type genType = clazz.getGenericSuperclass();
		if (genType instanceof ParameterizedType) {
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
			try {
				return ((Class) params[0]).newInstance();
			} catch (Exception e) {
				log.debug("实例化泛型T失败!");
				return null;
			}
		}
		// 不支持泛型，返回null
		return null;
	}
}
