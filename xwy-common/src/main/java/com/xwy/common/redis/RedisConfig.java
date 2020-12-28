package com.xwy.common.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.xwy.common.redis.util.RedisObjectSerializer;

/**
 * redis 配置
 * 
 * @author xiangwy
 * @date: 2020-12-02 10:19:32
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
@Configuration
public class RedisConfig {

	/**
	 * redis 集群模式
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 10:20:28
	 * @param factory
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Primary
	@Bean("redisTemplate")
	@ConditionalOnProperty(name = "spring.redis.cluster.nodes", matchIfMissing = false)
	public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(factory);
		RedisSerializer stringSerializer = new StringRedisSerializer();
		RedisSerializer redisObjectSerializer = new RedisObjectSerializer();
		redisTemplate.setKeySerializer(stringSerializer); // key的序列化类型
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(redisObjectSerializer); // value的序列化类型
		redisTemplate.afterPropertiesSet();
		redisTemplate.opsForValue().set("hello", "wolrd");
		return redisTemplate;
	}

	/**
	 * reids 单机模式
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-02 10:22:15
	 * @param factory
	 * @return
	 */
	@Primary
	@Bean("redisTemplate")
	@ConditionalOnProperty(name = "spring.redis.host", matchIfMissing = true)
	public RedisTemplate<String, Object> getSingleRedisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(factory);
		redisTemplate.setKeySerializer(new StringRedisSerializer()); // key的序列化类型
		redisTemplate.setValueSerializer(new RedisObjectSerializer()); // value的序列化类型
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

}
