package com.xwy.common.redis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xwy.common.utils.ObjectUtils;
import com.xwy.common.utils.StringUtils;

/**
 * Jedis Cache 工具类
 * 
 * @author xiangwy
 * @date: 2020-12-02 10:23:33
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
public class JedisUtils {

	private static Logger logger = LoggerFactory.getLogger(JedisUtils.class);

	/**
	 * 只有不存在的时候才设置
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static Boolean setNx(String key, String value) {
		try {
			return JedisClusterUtils.setnx(getBytesKey(key), toBytes(value));
		} catch (Exception e) {
			logger.warn("getObject {} = {}", key, value, e);
		}
		return false;
	}

	/**
	 * key 设置过期
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public static Boolean expire(String key, long seconds) {
		try {
			return JedisClusterUtils.expire(getBytesKey(key), (int) seconds);
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 获取当前缓存
	 * 
	 * @param key键
	 * @return 值
	 */
	public static Object getObject(String key) {
		return getGlobalObject(key);
	}

	/**
	 * 获取全局缓存
	 * 
	 * @param key键
	 * @return 值
	 */
	public static Object getGlobalObject(String key) {
		Object value = null;
		try {
			if (JedisClusterUtils.hasKey(getBytesKey(key))) {
				value = toObject(JedisClusterUtils.get(getBytesKey(key)));
				logger.debug("getObject {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObject {} = {}", key, value, e);
		}
		return value;
	}

	/**
	 * 设置缓存
	 * 
	 * @param key键
	 * @param value值
	 * @param cacheSeconds超时时间，0为不超时
	 * @return
	 */
	public static String setObject(String key, Object value, int cacheSeconds) {
		return setGlobalObject(key, value, cacheSeconds);
	}

	/**
	 * 设置全局缓存
	 * 
	 * @param key键
	 * @param value值
	 * @param cacheSeconds超时时间，0为不超时
	 * @return
	 */
	public static String setGlobalObject(String key, Object value, int cacheSeconds) {
		if (value == null) {
			return "";
		}
		String result = null;
		try {
			JedisClusterUtils.set(getBytesKey(key), toBytes(value));
			if (cacheSeconds != 0) {
				JedisClusterUtils.expire(key, cacheSeconds);
			}
			logger.debug("setObject {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObject {} = {}", key, value, e);
		}
		return result;
	}

	/**
	 * 分页获取当前List缓存
	 * 
	 * @param key键
	 * @return 值
	 */
	public static List<Object> getObjectList(String key, long start, long end) {
		List<Object> value = null;
		try {
			if (JedisClusterUtils.hasKey(getBytesKey(key))) {
				List<byte[]> list = JedisClusterUtils.lrange(getBytesKey(key), start, end);
				value = new ArrayList<Object>();
				for (byte[] bs : list) {
					value.add(toObject(bs));
				}
				logger.debug("getObjectList {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectList {} = {}", key, value, e);
		}
		return value;
	}

	/**
	 * 获取当前List缓存
	 * 
	 * @param key键
	 * @return 值
	 */
	public static List<Object> getObjectList(String key) {
		List<Object> value = null;
		try {
			if (JedisClusterUtils.hasKey(getBytesKey(key))) {
				List<byte[]> list = JedisClusterUtils.lrange(getBytesKey(key), 0, -1);
				value = new ArrayList<Object>();
				for (byte[] bs : list) {
					value.add(toObject(bs));
				}
				logger.debug("getObjectList {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectList {} = {}", key, value, e);
		}
		return value;
	}

	/**
	 * 设置当前List缓存
	 * 
	 * @param key          键
	 * @param value        值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setObjectList(String key, List<Object> value, int cacheSeconds) {
		long result = 0;
		try {
			if (JedisClusterUtils.hasKey(getBytesKey(key))) {
				JedisClusterUtils.del(key);
			}
			// List<byte[]> list = Lists.newArrayList();
			for (Object o : value) {
				// list.add(toBytes(o));
				JedisClusterUtils.rpush(getBytesKey(key), toBytes(o));
			}
			// result = jedis.rpush(getBytesKey(key), (byte[][])list.toArray());
			if (cacheSeconds != 0) {
				JedisClusterUtils.expire(key, cacheSeconds);
			}
			logger.debug("setObjectList {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectList {} = {}", key, value, e);
		}
		return result;
	}

	/**
	 * @descript：获取Map缓存 getObjectMap 方法
	 * @param key
	 * @return
	 * @return Map<String,Object>
	 * @author xiangwy
	 * @date 2017年2月24日-下午5:15:54
	 */
	public static Map<String, Object> getObjectMap(String key) {
		Map<String, Object> value = null;
		try {
			if (JedisClusterUtils.hasKey(getBytesKey(key))) {
				value = new HashMap<String, Object>();
				Map<byte[], byte[]> map = JedisClusterUtils.hgetAll(getBytesKey(key));
				for (Map.Entry<byte[], byte[]> e : map.entrySet()) {
					value.put(StringUtils.toString(e.getKey()), toObject(e.getValue()));
				}
				logger.debug("getObjectMap {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectMap {} = {}", key, value, e);
		}
		return value;
	}

	/**
	 * getListLength 方法
	 * 
	 * @param key
	 * @return
	 * @return long
	 * @author fanhaohao
	 * @date 2017年2月4日-下午2:31:33
	 */
	public static long getListLength(String key) {
		try {
			if (JedisClusterUtils.hasKey(getBytesKey(key))) {
				return JedisClusterUtils.llen(key);
			}
		} catch (Exception e) {
			logger.warn("getObjectListLength {}", key, e);
		}
		return 0;
	}

	/**
	 * 设置当前Map缓存
	 * 
	 * @param key键
	 * @param value        值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setObjectMap(String key, Map<String, Object> value, int cacheSeconds) {
		String result = null;
		try {
			if (JedisClusterUtils.hasKey(getBytesKey(key))) {
				JedisClusterUtils.del(key);
			}
			Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
			for (Map.Entry<String, Object> e : value.entrySet()) {
				map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
			}
			result = JedisClusterUtils.hmset(getBytesKey(key), map);
			if (cacheSeconds != 0) {
				JedisClusterUtils.expire(key, cacheSeconds);
			}
			logger.debug("setObjectMap {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectMap {} = {}", key, value, e);
		}
		return result;
	}

	/**
	 * 删除当前缓存
	 * 
	 * @param key键
	 * @return
	 */
	public static long delObject(String key) {
		return delGlobalObject(key);
	}

	/**
	 * 删除匹配的key<br>
	 * 如以my为前缀的则 参数为"my*"
	 * 
	 * @param key
	 */
	public synchronized static void deleteKeys(String pattern) {
		try {
			// 列出所有匹配的key
			Set<String> keySet = JedisClusterUtils.keys(pattern);
			if (keySet == null || keySet.size() <= 0) {
				return;
			}
			String keyArr[] = new String[keySet.size()];
			int i = 0;
			for (String keys : keySet) {
				keyArr[i] = keys;
				i++;
			}
			JedisClusterUtils.del(keyArr);
		} catch (Exception e) {
			logger.warn("deleteKeys {}", pattern, e);
		}
	}

	/**
	 * 删除前缀为{参数}的所有key<br>
	 * 
	 * @param prefix
	 */
	public synchronized static void deleteKeyByPrefix(String prefix) {
		deleteKeys(prefix + "*");
	}

	/**
	 * 删除全局缓存
	 * 
	 * @param key 键
	 * @return
	 */
	public static long delGlobalObject(String key) {
		long result = 0;
		try {
			if (JedisClusterUtils.hasKey(getBytesKey(key))) {
				result = JedisClusterUtils.del(getBytesKey(key));
				logger.debug("delObject {}", key);
			} else {
				logger.debug("delObject {} not exists", key);
			}
		} catch (Exception e) {
			logger.warn("delObject {}", key, e);
		}
		return result;
	}

	/**
	 * -1: 如果key没有到期超时(超时设为0时)。 -2: 如果键不存在，没过期则大于0
	 * 
	 * @param key
	 * @return
	 */
	public static long keyTll(String key) {
		long result = 0;
		try {
			if (JedisClusterUtils.hasKey(getBytesKey(key))) {
				result = JedisClusterUtils.ttl(getBytesKey(key));
				logger.debug("keyTll {}", key);
			} else {
				logger.debug("keyTll {} not exists", key);
			}
		} catch (Exception e) {
			logger.warn("keyTll {}", key, e);
		}
		return result;
	}

	/**
	 * 当前缓存是否存在
	 * 
	 * @param key键
	 * @return
	 */
	public static boolean existsObject(String key) {
		return existsGlobalObject(key);
	}

	/**
	 * 全局缓存是否存在
	 * 
	 * @param key键
	 * @return
	 */
	public static boolean existsGlobalObject(String key) {
		boolean result = false;
		try {
			result = JedisClusterUtils.hasKey(getBytesKey(key));
			logger.debug("existsObject {}", key);
		} catch (Exception e) {
			logger.warn("existsObject {}", key, e);
		}
		return result;
	}

	/**
	 * 获取byte[]类型Key
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] getBytesKey(Object object) {
		if (object instanceof String) {
			return StringUtils.getBytes((String) object);
		} else {
			return ObjectUtils.serialize(object);
		}
	}

	/**
	 * Object转换byte[]类型
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] toBytes(Object object) {
		return ObjectUtils.serialize(object);
	}

	/**
	 * byte[]型转换Object
	 * 
	 * @param key
	 * @return
	 */
	public static Object toObject(byte[] bytes) {
		return ObjectUtils.unserialize(bytes);
	}

	/**
	 * 自增,+1,返回增加后的值
	 * 
	 * @param key
	 * @return
	 */
	public static long incr(String key) {
		return getNextSN(key);
	}

	/**
	 * 返回下一系列号
	 * 
	 * @param key
	 * @return
	 */
	private static Long getNextSN(String key) {
		Long value = null;
		try {
			value = JedisClusterUtils.incr(getBytesKey(key));
			// 有效期2天
			JedisClusterUtils.expire(key, 172800);
		} catch (Exception e) {
			logger.warn("getNextIncr {} = {}", key, value, e);
		}
		return value;
	}

	/**
	 * 自增,+1,返回增加后的值
	 * 
	 * @param key
	 * @return
	 */
	public static long incrNotExpire(String key) {
		return getNextNotExpire(key);
	}

	/**
	 * 返回下一系列号
	 * 
	 * @param key
	 * @return
	 */
	private static Long getNextNotExpire(String key) {
		Long value = null;
		try {
			value = JedisClusterUtils.incr(getBytesKey(key));
		} catch (Exception e) {
			logger.warn("getNextIncr {} = {}", key, value, e);
		}
		return value;
	}

	public static byte[] getGlobalByte(String key) {
		byte[] value = null;
		try {
			if (JedisClusterUtils.hasKey(getBytesKey(key))) {
				value = JedisClusterUtils.get(getBytesKey(key));
				logger.debug("getObject {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObject {} = {}", key, value, e);
		}
		return value;
	}

	/**
	 * autoIncrBy 方法 增加 指定步骤 长
	 * 
	 * @param key
	 * @param integer
	 * @return
	 * @return Long
	 * @author xiangwy
	 * @date 2019年8月30日-上午11:57:39
	 */
	public static Long autoIncrBy(String key, long integer) {
		Long value = null;
		try {
			value = JedisClusterUtils.incrLong(key, integer);
		} catch (Exception e) {
			logger.warn("get {} = {}", key, value, e);
		} finally {

		}
		return value;
	}

	/**
	 * 递减
	 * 
	 * @param key
	 * @return
	 */
	public static Long decr(String key) {
		Long value = null;
		try {
			value = JedisClusterUtils.decr(key, 1);
		} catch (Exception e) {
			logger.warn("get {} = {}", key, value, e);
		}
		return value;
	}

}
