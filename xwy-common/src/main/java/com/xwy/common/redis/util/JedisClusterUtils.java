package com.xwy.common.redis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

/**
 * Jedis 工具类
 * 
 * @author xiangwy
 * @date: 2020-12-02 10:24:12
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
@Component
public class JedisClusterUtils {

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	private static JedisClusterUtils cacheUtils;

	@PostConstruct
	public void init() {
		cacheUtils = this;
		cacheUtils.redisTemplate = this.redisTemplate;
	}

	// =============================common============================
	/**
	 * 指定缓存失效时间
	 * 
	 * @param key  键
	 * @param time 时间(秒)
	 * @return
	 */
	public static boolean expire(String key, long time) {
		try {
			if (time > 0) {
				cacheUtils.redisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 指定缓存失效时间
	 * 
	 * @param key  键
	 * @param time 时间(秒)
	 * @return
	 */
	public static boolean expire(final byte[] key, final long time) {
		try {
			if (time > 0) {
				boolean result = cacheUtils.redisTemplate.execute(new RedisCallback<Boolean>() {
					@Override
					public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {

						return redisConnection.expire(key, time);
					}
				});

				return result;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void setex(final String key, final int seconds, final String value) {
		cacheUtils.redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {

				redisConnection.setEx(key.getBytes(), seconds, value.getBytes());
				return true;
			}
		});

	}

	/**
	 * 根据key 获取过期时间
	 * 
	 * @param key 键 不能为null
	 * @return 时间(秒) 返回0代表为永久有效
	 */
	public static long getExpire(String key) {
		return cacheUtils.redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * 判断key是否存在
	 * 
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public static boolean hasKey(String key) {
		try {
			return cacheUtils.redisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断key是否存在
	 * 
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public static boolean hasKey(final byte[] key) {
		try {
			boolean result = cacheUtils.redisTemplate.execute(new RedisCallback<Boolean>() {
				@Override
				public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {

					return redisConnection.exists(key);
				}
			});

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除缓存
	 * 
	 * @param key 可以传一个值 或多个
	 */
	@SuppressWarnings("unchecked")
	public static void del(String... key) {
		if (key != null && key.length > 0) {
			if (key.length == 1) {
				cacheUtils.redisTemplate.delete(key[0]);
			} else {
				cacheUtils.redisTemplate.delete(CollectionUtils.arrayToList(key));
			}
		}
	}

	/*
	 * 删除缓存
	 * 
	 * @param key 可以传一个值
	 */
	public static void del(String key) {
		cacheUtils.redisTemplate.delete(key);

	}

	/*
	 * 删除缓存
	 * 
	 * @param key 可以传一个值
	 */
	public static long del(final byte[] keys) {
		long result = cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
				// RedisSerializer<String> redisSerializer =
				// cacheUtils.redisTemplate .getStringSerializer();
				return redisConnection.del(keys);
			}
		});
		return result;
	}

	public static boolean setnx(final byte[] key, final byte[] value) {
		boolean result = cacheUtils.redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
				// RedisSerializer<String> redisSerializer =
				// cacheUtils.redisTemplate .getStringSerializer();
				return redisConnection.setNX(key, value);
			}
		});
		return result;
	}

	public static long ttl(final byte[] key) {
		long result = cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
				// RedisSerializer<String> redisSerializer =
				// cacheUtils.redisTemplate .getStringSerializer();
				return redisConnection.ttl(key);
			}
		});
		return result;
	}

	/**
	 * 将 key的值保存为 value ，当且仅当 key 不存在。 若给定的 key 已经存在，则 SETNX 不做任何动作。 SETNX 是『SET if
	 * Not eXists』(如果不存在，则 SET)的简写。 <br>
	 * 保存成功，返回 true <br>
	 * 保存失败，返回 false
	 */
	public static boolean setNX(final String key, final String val) {
		return setnx(key.getBytes(), val.getBytes());
	}

	// ============================String=============================
	/**
	 * 普通缓存获取
	 * 
	 * @param key 键
	 * @return 值
	 */
	public static Object get(String key) {
		return key == null ? null : cacheUtils.redisTemplate.opsForValue().get(key);
	}

	/**
	 * 普通缓存获取
	 * 
	 * @param key 键
	 * @return 值
	 */
	public static byte[] get(final byte[] key) {
		byte[] result = cacheUtils.redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection redisConnection) throws DataAccessException {
				// RedisSerializer<String> redisSerializer =
				// cacheUtils.redisTemplate .getStringSerializer();
				return redisConnection.get(key);
			}
		});
		return result;
	}

	/**
	 * 普通缓存放入
	 * 
	 * @param key   键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public static boolean set(String key, String value) {
		try {
			cacheUtils.redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 普通缓存放入
	 * 
	 * @param key   键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public static boolean set(final byte[] key, final byte[] value) {
		boolean result = cacheUtils.redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
				// RedisSerializer<String> redisSerializer =
				// cacheUtils.redisTemplate .getStringSerializer();
				redisConnection.set(key, value);
				return true;
			}
		});
		return result;
	}

	/**
	 * 普通缓存放入并设置时间
	 * 
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public static boolean set(String key, String value, long time) {
		try {
			if (time > 0) {
				cacheUtils.redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			} else {
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 递增
	 * 
	 * @param key 键
	 * @param by  要增加几(大于0)
	 * @return
	 */
	public static long incr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递增因子必须大于0");
		}
		return cacheUtils.redisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * 递增
	 * 
	 * @param key 键
	 * @param by  要增加几(大于0)
	 * @return
	 */
	public static long incr(final byte[] key) {
		long result = cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
				// RedisSerializer<String> redisSerializer =
				// cacheUtils.redisTemplate .getStringSerializer();
				return redisConnection.incr(key);
			}
		});
		return result;
	}

	/**
	 * 递减
	 * 
	 * @param key 键
	 * @param by  要减少几(小于0)
	 * @return
	 */
	public static long decr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递减因子必须大于0");
		}
		return cacheUtils.redisTemplate.opsForValue().increment(key, -delta);
	}

	// ================================Map=================================
	/**
	 * HashGet
	 * 
	 * @param key  键 不能为null
	 * @param item 项 不能为null
	 * @return 值
	 */
	public static Object hget(String key, String item) {
		return cacheUtils.redisTemplate.opsForHash().get(key, item);
	}

	/**
	 * 获取hashKey对应的所有键值
	 * 
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public static Map<Object, Object> hmget(String key) {
		return cacheUtils.redisTemplate.opsForHash().entries(key);
	}

	/**
	 * HashSet
	 * 
	 * @param key 键
	 * @param map 对应多个键值
	 * @return true 成功 false 失败
	 */
	public static boolean hmset(String key, Map<String, Object> map) {
		try {
			cacheUtils.redisTemplate.opsForHash().putAll(key, map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * HashSet 并设置时间
	 * 
	 * @param key  键
	 * @param map  对应多个键值
	 * @param time 时间(秒)
	 * @return true成功 false失败
	 */
	public static boolean hmset(String key, Map<String, Object> map, long time) {
		try {
			cacheUtils.redisTemplate.opsForHash().putAll(key, map);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String hmset(final byte[] key, final Map<byte[], byte[]> hashes) {
		String result = cacheUtils.redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
				redisConnection.hMSet(key, hashes);
				return "1";
			}
		});
		return result;
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * 
	 * @param key   键
	 * @param item  项
	 * @param value 值
	 * @return true 成功 false失败
	 */
	public static boolean hset(String key, String item, Object value) {
		try {
			cacheUtils.redisTemplate.opsForHash().put(key, item, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * 
	 * @param key   键
	 * @param item  项
	 * @param value 值
	 * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return true 成功 false失败
	 */
	public static boolean hset(String key, String item, Object value, long time) {
		try {
			cacheUtils.redisTemplate.opsForHash().put(key, item, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除hash表中的值
	 * 
	 * @param key  键 不能为null
	 * @param item 项 可以使多个 不能为null
	 */
	public static void hdel(String key, Object... item) {
		cacheUtils.redisTemplate.opsForHash().delete(key, item);
	}

	/**
	 * 判断hash表中是否有该项的值
	 * 
	 * @param key  键 不能为null
	 * @param item 项 不能为null
	 * @return true 存在 false不存在
	 */
	public static boolean hHasKey(String key, String item) {
		return cacheUtils.redisTemplate.opsForHash().hasKey(key, item);
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 * 
	 * @param key  键
	 * @param item 项
	 * @param by   要增加几(大于0)
	 * @return
	 */
	public static double hincr(String key, String item, double by) {
		return cacheUtils.redisTemplate.opsForHash().increment(key, item, by);
	}

	/**
	 * hash递减
	 * 
	 * @param key  键
	 * @param item 项
	 * @param by   要减少记(小于0)
	 * @return
	 */
	public static double hdecr(String key, String item, double by) {
		return cacheUtils.redisTemplate.opsForHash().increment(key, item, -by);
	}

	// ============================set=============================
	/**
	 * 根据key获取Set中的所有值
	 * 
	 * @param key 键
	 * @return
	 */
	public static Set<String> sGet(String key) {
		try {
			return cacheUtils.redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 * 
	 * @param key   键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public static boolean sHasKey(String key, Object value) {
		try {
			return cacheUtils.redisTemplate.opsForSet().isMember(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将数据放入set缓存
	 * 
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public static long sSet(String key, String... values) {
		try {
			return cacheUtils.redisTemplate.opsForSet().add(key, values);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 将set数据放入缓存
	 * 
	 * @param key    键
	 * @param time   时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long sSetAndTime(String key, long time, String... values) {
		try {
			Long count = cacheUtils.redisTemplate.opsForSet().add(key, values);
			if (time > 0) {
				expire(key, time);
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取set缓存的长度
	 * 
	 * @param key 键
	 * @return
	 */
	public long sGetSetSize(String key) {
		try {
			return cacheUtils.redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 移除值为value的
	 * 
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public long setRemove(String key, Object... values) {
		try {
			Long count = cacheUtils.redisTemplate.opsForSet().remove(key, values);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	// ===============================list=================================

	/**
	 * 获取list缓存的内容
	 * 
	 * @param key   键
	 * @param start 开始
	 * @param end   结束 0 到 -1代表所有值
	 * @return
	 */
	public List<String> lGet(String key, long start, long end) {
		try {
			return cacheUtils.redisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取list缓存的长度
	 * 
	 * @param key 键
	 * @return
	 */
	public long lGetListSize(String key) {
		try {
			return cacheUtils.redisTemplate.opsForList().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 通过索引 获取list中的值
	 * 
	 * @param key   键
	 * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return
	 */
	public Object lGetIndex(String key, long index) {
		try {
			return cacheUtils.redisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将list放入缓存
	 * 
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lSet(String key, String value) {
		try {
			cacheUtils.redisTemplate.opsForList().rightPush(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * 
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lSet(String key, String value, long time) {
		try {
			cacheUtils.redisTemplate.opsForList().rightPush(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * 
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lSet(String key, List<String> value) {
		try {
			cacheUtils.redisTemplate.opsForList().rightPushAll(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * 
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lSet(String key, List<String> value, long time) {
		try {
			cacheUtils.redisTemplate.opsForList().rightPushAll(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据索引修改list中的某条数据
	 * 
	 * @param key   键
	 * @param index 索引
	 * @param value 值
	 * @return
	 */
	public boolean lUpdateIndex(String key, long index, String value) {
		try {
			cacheUtils.redisTemplate.opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 移除N个值为value
	 * 
	 * @param key   键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 移除的个数
	 */
	public static long lRemove(String key, long count, Object value) {
		try {
			Long remove = cacheUtils.redisTemplate.opsForList().remove(key, count, value);
			return remove;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static Set<String> keys(String key) {
		Set<String> set = null;
		try {

			set = cacheUtils.redisTemplate.keys(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return set;

	}

	/**
	 * 将数据存入缓存
	 *
	 * @param key
	 * @param val
	 * @return
	 */
	public static void saveString(String key, String val) {

		ValueOperations<String, String> vo = cacheUtils.redisTemplate.opsForValue();
		vo.set(key, val);
	}

	/**
	 * 将数据存入缓存的集合中
	 *
	 * @param key
	 * @param val
	 * @return
	 */
	public static void saveToSet(String key, String val) {

		SetOperations<String, String> so = cacheUtils.redisTemplate.opsForSet();

		so.add(key, val);
	}

	/**
	 *
	 *
	 * @param key 缓存Key
	 * @return keyValue
	 * @author:mijp
	 * @since:2017/1/16 13:23
	 */
	public static String getFromSet(String key) {
		return cacheUtils.redisTemplate.opsForSet().pop(key);
	}

	/**
	 * 将 key的值保存为 value ，当且仅当 key 不存在。 若给定的 key 已经存在，则 SETNX 不做任何动作。 SETNX 是『SET if
	 * Not eXists』(如果不存在，则 SET)的简写。 <br>
	 * 保存成功，返回 true <br>
	 * 保存失败，返回 false
	 */
	public static boolean saveNX(final String key, final String val) {

		/** 设置成功，返回 1 设置失败，返回 0 **/
		return cacheUtils.redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
				return redisConnection.setNX(key.getBytes(), val.getBytes());
			}
		});

	}

	/**
	 * 将 key的值保存为 value ，当且仅当 key 不存在。 若给定的 key 已经存在，则 SETNX 不做任何动作。 SETNX 是『SET if
	 * Not eXists』(如果不存在，则 SET)的简写。 <br>
	 * 保存成功，返回 true <br>
	 * 保存失败，返回 false
	 *
	 * @param key
	 * @param val
	 * @param expire 超时时间
	 * @return 保存成功，返回 true 否则返回 false
	 */
	public static boolean saveNX(String key, String val, int expire) {

		boolean ret = saveNX(key, val);
		if (ret) {
			cacheUtils.redisTemplate.expire(key, expire, TimeUnit.SECONDS);
		}
		return ret;
	}

	/**
	 * 将数据存入缓存（并设置失效时间）
	 *
	 * @param key
	 * @param val
	 * @param seconds
	 * @return
	 */
	public static void saveString(String key, String val, int seconds) {

		cacheUtils.redisTemplate.opsForValue().set(key, val, seconds, TimeUnit.SECONDS);
	}

	/**
	 * 将自增变量存入缓存
	 */
	public static void saveSeq(String key, long seqNo) {

		cacheUtils.redisTemplate.delete(key);
		cacheUtils.redisTemplate.opsForValue().increment(key, seqNo);
	}

	/**
	 * 将递增浮点数存入缓存
	 */
	public static void saveFloat(String key, float data) {

		cacheUtils.redisTemplate.delete(key);
		cacheUtils.redisTemplate.opsForValue().increment(key, data);
	}

	/**
	 * 保存复杂类型数据到缓存
	 *
	 * @param key
	 * @param obj
	 * @return
	 */
	public static void saveBean(String key, Object obj) {

		cacheUtils.redisTemplate.opsForValue().set(key, JSON.toJSONString(obj));
	}

	/**
	 * 保存复杂类型数据到缓存（并设置失效时间）
	 *
	 * @param key
	 * @param Object
	 * @param seconds
	 * @return
	 */
	public static void saveBean(String key, Object obj, int seconds) {

		cacheUtils.redisTemplate.opsForValue().set(key, JSON.toJSONString(obj), seconds, TimeUnit.SECONDS);
	}

	/**
	 * 功能: 存到指定的队列中<br />
	 * 左近右出<br\> 作者: 耿建委
	 *
	 * @param key
	 * @param val
	 * @param size 队列大小限制 0：不限制
	 */
	public static void saveToQueue(String key, String val, long size) {

		ListOperations<String, String> lo = cacheUtils.redisTemplate.opsForList();

		if (size > 0 && lo.size(key) >= size) {
			lo.rightPop(key);
		}
		lo.leftPush(key, val);
	}

	/**
	 * 保存到hash集合中
	 *
	 * @param hName 集合名
	 * @param key
	 * @param val
	 */
	public static void hashSet(String hName, String key, String value) {

		cacheUtils.redisTemplate.opsForHash().put(hName, key, value);
	}

	/**
	 * 根据key获取所以值
	 * 
	 * @param key
	 * @return
	 */
	public static Map<Object, Object> hgetAll(String key) {

		return cacheUtils.redisTemplate.opsForHash().entries(key);
	}

	/**
	 * 根据key获取所以值
	 * 
	 * @param key
	 * @return
	 */
	public static Map<byte[], byte[]> hgetAll(final byte[] key) {

		Map<byte[], byte[]> result = cacheUtils.redisTemplate.execute(new RedisCallback<Map<byte[], byte[]>>() {
			@Override
			public Map<byte[], byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {

				return redisConnection.hGetAll(key);
			}
		});

		return result;
	}

	/**
	 * 保存到hash集合中
	 *
	 * @param <T>
	 *
	 * @param hName 集合名
	 * @param key
	 * @param val
	 */
	public static <T> void hashSet(String hName, String key, T t) {

		hashSet(hName, key, JSON.toJSONString(t));
	}

	/**
	 * 取得复杂类型数据
	 *
	 * @param key
	 * @param obj
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(String key, Class<T> clazz) {

		String value = cacheUtils.redisTemplate.opsForValue().get(key);
		if (value == null) {
			return null;
		}
		return JSON.parseObject(value, clazz);
	}

	/**
	 * 从缓存中取得字符串数据
	 *
	 * @param key
	 * @return 数据
	 */
	public static String getString(String key) {
		cacheUtils.redisTemplate.opsForValue().get(key);

		return cacheUtils.redisTemplate.opsForValue().get(key);
	}

	/**
	 *
	 * 功能: 从指定队列里取得数据<br />
	 * 作者: 耿建委
	 *
	 * @param key
	 * @param size 数据长度
	 * @return
	 */
	public static List<String> getFromQueue(final String key, long size) {

		boolean flag = cacheUtils.redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
				return redisConnection.exists(key.getBytes());
			}
		});

		if (flag) {
			return new ArrayList<>();
		}
		ListOperations<String, String> lo = cacheUtils.redisTemplate.opsForList();
		if (size > 0) {
			return lo.range(key, 0, size - 1);
		} else {
			return lo.range(key, 0, lo.size(key) - 1);
		}
	}

	/**
	 *
	 * 功能: 从指定队列里取得数据<br />
	 * 作者: 耿建委
	 *
	 * @param key
	 * @return
	 */
	public static String popQueue(String key) {

		return cacheUtils.redisTemplate.opsForList().rightPop(key);

	}

	/**
	 * 取得序列值的下一个
	 *
	 * @param key
	 * @return
	 */
	public static Long getSeqNext(final String key) {

		return cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {

				return redisConnection.incr(key.getBytes());

			}
		});
	}

	/**
	 * 取得序列值的下一个
	 *
	 * @param key
	 * @return
	 */
	public static Long getSeqNext(final String key, final long value) {

		return cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {

				return redisConnection.incrBy(key.getBytes(), value);

			}
		});

	}

	/**
	 * 将序列值回退一个
	 *
	 * @param key
	 * @return
	 */
	public static void getSeqBack(final String key) {

		cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {

				return redisConnection.decr(key.getBytes());

			}
		});

	}

	/**
	 * 从hash集合里取得
	 *
	 * @param hName
	 * @param key
	 * @return
	 */
	public static Object hashGet(String hName, String key) {

		return cacheUtils.redisTemplate.opsForHash().get(hName, key);
	}

	public static <T> T hashGet(String hName, String key, Class<T> clazz) {

		return JSON.parseObject((String) hashGet(hName, key), clazz);
	}

	/**
	 * 增加浮点数的值
	 *
	 * @param key
	 * @return
	 */
	public static Double incrFloat(final String key, final double incrBy) {

		return cacheUtils.redisTemplate.execute(new RedisCallback<Double>() {
			@Override
			public Double doInRedis(RedisConnection redisConnection) throws DataAccessException {

				return redisConnection.incrBy(key.getBytes(), incrBy);

			}
		});
	}

	/**
	 * 增加long
	 *
	 * @param key
	 * @return
	 */
	public static Long incrLong(final String key, final Long incrBy) {

		return cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
				return redisConnection.incrBy(key.getBytes(), incrBy);

			}
		});
	}

	/**
	 * 判断是否缓存了数据
	 *
	 * @param key 数据KEY
	 * @return 判断是否缓存了
	 */
	public static boolean isCached(final String key) {

		return cacheUtils.redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {

				return redisConnection.exists(key.getBytes());

			}
		});
	}

	/**
	 * 判断hash集合中是否缓存了数据
	 *
	 * @param hName
	 * @param key   数据KEY
	 * @return 判断是否缓存了
	 */
	public static boolean hashCached(final String hName, final String key) {

		return cacheUtils.redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {

				return redisConnection.hExists(hName.getBytes(), key.getBytes());

			}
		});
	}

	/**
	 * 判断是否缓存在指定的集合中
	 *
	 * @param key 数据KEY
	 * @param val 数据
	 * @return 判断是否缓存了
	 */
	public static boolean isMember(final String key, final String val) {

		return cacheUtils.redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {

				return redisConnection.sIsMember(key.getBytes(), val.getBytes());

			}
		});
	}

	/**
	 * 从缓存中删除数据
	 *
	 * @param string
	 * @return
	 */
	public static void delKey(final String key) {

		cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {

				return redisConnection.del(key.getBytes());

			}
		});
	}

	/**
	 * 设置超时时间
	 *
	 * @param key
	 * @param seconds
	 */
	public static void expire(final String key, final int seconds) {

		cacheUtils.redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {

				return redisConnection.expire(key.getBytes(), seconds);

			}
		});

	}

	/**
	 * 列出set中所有成员
	 *
	 * @param setName set名
	 * @return
	 */
	public static Set<Object> listSet(String setName) {

		return cacheUtils.redisTemplate.opsForHash().keys(setName);

	}

	/**
	 * 向set中追加一个值
	 *
	 * @param setName set名
	 * @param value
	 */
	public static void setSave(final String setName, final String value) {

		cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.sAdd(setName.getBytes(), value.getBytes());
			}
		});

	}

	/**
	 * 逆序列出sorted set包括分数的set列表
	 *
	 * @param key   set名
	 * @param start 开始位置
	 * @param end   结束位置
	 * @return 列表
	 */
	public static Set<Tuple> listSortedsetRev(final String key, final int start, final int end) {

		return cacheUtils.redisTemplate.execute(new RedisCallback<Set<Tuple>>() {
			@Override
			public Set<Tuple> doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.zRevRangeWithScores(key.getBytes(), start, end);
			}
		});

	}

	/**
	 * 逆序取得sorted sort排名
	 *
	 * @param key    set名
	 * @param member 成员名
	 * @return 排名
	 */
	public static Long getRankRev(final String key, final String member) {

		return cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.zRevRank(key.getBytes(), member.getBytes());
			}
		});

	}

	/**
	 * 根据成员名取得sorted sort分数
	 *
	 * @param key    set名
	 * @param member 成员名
	 * @return 分数
	 */
	public static Double getMemberScore(final String key, final String member) {

		return cacheUtils.redisTemplate.execute(new RedisCallback<Double>() {
			@Override
			public Double doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.zScore(key.getBytes(), member.getBytes());
			}
		});

	}

	/**
	 * 向sorted set中追加一个值
	 *
	 * @param key    set名
	 * @param score  分数
	 * @param member 成员名称
	 */
	public static void saveToSortedset(final String key, final Double score, final String member) {

		cacheUtils.redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.zAdd(key.getBytes(), score, member.getBytes());
			}
		});
	}

	/**
	 * 从sorted set删除一个值
	 *
	 * @param key    set名
	 * @param member 成员名称
	 */
	public static void delFromSortedset(final String key, final String member) {

		cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.zRem(key.getBytes(), member.getBytes());
			}
		});

	}

	/**
	 * 从hash map中取得复杂类型数据
	 *
	 * @param key
	 * @param field
	 * @param clazz
	 */
	public static <T> T getBeanFromMap(final String key, final String field, Class<T> clazz) {

		byte[] input = cacheUtils.redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.hGet(key.getBytes(), field.getBytes());
			}
		});

		return JSON.parseObject(input, clazz, Feature.AutoCloseSource);
	}

	/**
	 * 从hashmap中删除一个值
	 *
	 * @param key   map名
	 * @param field 成员名称
	 */
	public static void delFromMap(final String key, final String field) {

		cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.hDel(key.getBytes(), field.getBytes());
			}
		});

	}

	/**
	 *
	 * @Description: 根据key增长 ，计数器
	 * @author clg
	 * @date 2016年6月30日 下午2:37:52
	 *
	 * @param key
	 * @return
	 */
	public static long incr(final String key) {

		return cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.incr(key.getBytes());
			}
		});
	}

	/**
	 *
	 * @Description: 根据key获取当前计数结果
	 * @author clg
	 * @date 2016年6月30日 下午2:38:20
	 *
	 * @param key
	 * @return
	 */
	public static String getCount(String key) {

		return cacheUtils.redisTemplate.opsForValue().get(key);
	}

	/**
	 * 将所有指定的值插入到存于 key 的列表的头部。如果 key 不存在，那么在进行 push 操作前会创建一个空列表
	 *
	 * @param <T>
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static <T> Long lpush(String key, T value) {

		return cacheUtils.redisTemplate.opsForList().leftPush(key, JSON.toJSONString(value));
	}

	public static Long lpush(final byte[] key, final byte[]... strings) {
		return cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.lPush(key, strings);
			}
		});
	}

	public static byte[] rpop(final byte[] key) {
		return cacheUtils.redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.rPop(key);
			}
		});
	}

	/**
	 * 只有当 key 已经存在并且存着一个 list 的时候，在这个 key 下面的 list 的头部插入 value。 与 LPUSH 相反，当 key
	 * 不存在的时候不会进行任何操作
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static <T> Long lpushx(String key, T value) {

		return cacheUtils.redisTemplate.opsForList().leftPushIfPresent(key, JSON.toJSONString(value));
	}

	/**
	 * 返回存储在 key 里的list的长度。 如果 key 不存在，那么就被看作是空list，并且返回长度为 0
	 *
	 * @param key
	 * @return
	 */
	public static Long llen(String key) {

		return cacheUtils.redisTemplate.opsForList().size(key);
	}

	/**
	 * 返回存储在 key 的列表里指定范围内的元素。 start 和 end
	 * 偏移量都是基于0的下标，即list的第一个元素下标是0（list的表头），第二个元素下标是1，以此类推
	 *
	 * @param key
	 * @return
	 */
	public static List<String> lrange(String key, long start, long end) {

		return cacheUtils.redisTemplate.opsForList().range(key, start, end);
	}

	/**
	 * 返回存储在 key 的列表里指定范围内的元素。 start 和 end
	 * 偏移量都是基于0的下标，即list的第一个元素下标是0（list的表头），第二个元素下标是1，以此类推
	 *
	 * @param key
	 * @return
	 */
	public static List<byte[]> lrange(final byte[] key, final long start, final long end) {
		List<byte[]> result = cacheUtils.redisTemplate.execute(new RedisCallback<List<byte[]>>() {
			@Override
			public List<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {

				return redisConnection.lRange(key, start, end);
			}
		});

		return result;

	}

	public static long rpush(final byte[] key, final byte[]... values) {
		long result = cacheUtils.redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {

				return redisConnection.rPush(key, values);
			}
		});

		return result;
	}

	/**
	 * 移除并且返回 key 对应的 list 的第一个元素
	 *
	 * @param key
	 * @return
	 */
	public static String lpop(String key) {

		return cacheUtils.redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 保存到hash集合中 只在 key 指定的哈希集中不存在指定的字段时，设置字段的值。如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key
	 * 关联。如果字段已存在，该操作无效果。
	 *
	 * @param hName 集合名
	 * @param key
	 * @param val
	 */
	public static void hsetnx(final String hName, final String key, final String value) {

		cacheUtils.redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {

				return redisConnection.hSetNX(key.getBytes(), key.getBytes(), value.getBytes());
			}
		});

	}

	/**
	 * 保存到hash集合中 只在 key 指定的哈希集中不存在指定的字段时，设置字段的值。如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key
	 * 关联。如果字段已存在，该操作无效果。
	 *
	 * @param <T>
	 *
	 * @param hName 集合名
	 * @param key
	 * @param val
	 */
	public static <T> void hsetnx(String hName, String key, T t) {
		hsetnx(hName, key, JSON.toJSONString(t));
	}

}