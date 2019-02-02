package com.penglecode.xmodule.common.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.penglecode.xmodule.common.redis.serializer.ProtostuffRedisSerializer;

import io.lettuce.core.RedisClient;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;


/**
 * springboot与spring-data-redis集成
 * 
 * @author 	pengpeng
 * @date	2018年2月3日 下午2:56:41
 */
@Configuration
@ConditionalOnClass(RedisClient.class)
public class CommonRedisConfiguration extends AbstractSpringConfiguration {

	@Bean(name="commonRedisProperties")
	@ConfigurationProperties(prefix="spring.redis.common")
	public RedisProperties commonRedisProperties() {
		return new RedisProperties();
	}
	
	@Bean(name="defaultRedisKeySerializer")
	public RedisSerializer<String> defaultRedisKeySerializer() {
		return new StringRedisSerializer();
	}
	
	@Bean(name="defaultRedisValueSerializer")
	public RedisSerializer<Object> defaultRedisValueSerializer() {
		return new ProtostuffRedisSerializer();
	}
	
	@Bean(name="defaultClientResources", destroyMethod="shutdown")
	public ClientResources defaultClientResources() {
		return DefaultClientResources.create();
	}
	
}
