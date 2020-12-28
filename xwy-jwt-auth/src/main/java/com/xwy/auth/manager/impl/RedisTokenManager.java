package com.xwy.auth.manager.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.xwy.common.redis.util.JedisClusterUtils;
import com.xwy.common.utils.BlankUtils;

/**
 * 使用Redis存储Token
 * 
 * @author xiangwy
 * @date: 2020-12-02 10:18:23
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
@Component
@ConditionalOnProperty(name = "ly.token.store", havingValue = "redis")
public class RedisTokenManager extends AbstractTokenManager {
	/**
	 * Redis中Key的前缀
	 */
	private static final String REDIS_KEY_PREFIX = "jwt:AUTHORIZATION:KEY:";

	/**
	 * Redis中Token的前缀
	 */
	private static final String REDIS_TOKEN_PREFIX = "jwt:AUTHORIZATION:TOKEN:";

	/**
	 * Jedis连接池
	 */
	// protected JedisPool jedisPool;

//    public void setJedisPool(JedisPool jedisPool) {
//        this.jedisPool = jedisPool;
//    }

	@Override
	protected void delSingleRelationshipByKey(String key) {
		String token = getToken(key);
		if (token != null) {
			delete(formatKey(key), formatToken(token));
		}
	}

	@Override
	public void delRelationshipByToken(String token) {
		if (singleTokenWithUser) {
			String key = getKey(token);
			delete(formatKey(key), formatToken(token));
		} else {
			delete(formatToken(token));
		}
	}

	@Override
	protected void createSingleRelationship(String key, String token) {
		String oldToken = get(formatKey(key));
		if (oldToken != null) {
			delete(formatToken(oldToken));
		}
		set(formatToken(token), key, tokenExpireSeconds);
		set(formatKey(key), token, tokenExpireSeconds);
	}

	@Override
	protected void createMultipleRelationship(String key, String token) {
		set(formatToken(token), key, tokenExpireSeconds);
	}

	@Override
	protected String getKeyByToken(String token) {
		return get(formatToken(token));
	}

	@Override
	protected void flushExpireAfterOperation(String key, String token) {
		if (singleTokenWithUser) {
			expire(formatKey(key), tokenExpireSeconds);
		}
		expire(formatToken(token), tokenExpireSeconds);
	}

	private String get(String key) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            return jedis.get(key);
//        }

		try {
			Object rt = JedisClusterUtils.get(key);
			if (BlankUtils.isNotBlank(rt)) {
				return rt.toString();
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private void set(String key, String value, int expireSeconds) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            return jedis.setex(key, expireSeconds, value);
//        }
		JedisClusterUtils.set(key, value, expireSeconds);
	}

	private void expire(String key, int seconds) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            jedis.expire(key, seconds);
//        }
		JedisClusterUtils.expire(key, seconds);
	}

	private void delete(String... keys) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            jedis.del(keys);
//        }
		JedisClusterUtils.del(keys);
	}

	private String getToken(String key) {
		return get(formatKey(key));
	}

	private String formatKey(String key) {
		return REDIS_KEY_PREFIX.concat(key);
	}

	private String formatToken(String token) {
		return REDIS_TOKEN_PREFIX.concat(token);
	}
}