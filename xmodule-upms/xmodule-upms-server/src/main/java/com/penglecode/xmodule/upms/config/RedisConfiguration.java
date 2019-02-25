package com.penglecode.xmodule.upms.config;

import static com.penglecode.xmodule.common.redis.LettuceConnectionFactoryUtils.createLettuceClientConfiguration;
import static com.penglecode.xmodule.common.redis.LettuceConnectionFactoryUtils.createLettuceConnectionFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.util.BeanUtils;

import io.lettuce.core.resource.ClientResources;

@Configuration
public class RedisConfiguration extends AbstractSpringConfiguration {
	
	/** 业务数据缓存Redis配置 */
	
	@Bean(name="dataRedisProperties")
	@ConfigurationProperties(prefix="spring.redis.data")
	public RedisProperties dataRedisProperties(@Qualifier("commonRedisProperties")RedisProperties commonRedisProperties) {
		return BeanUtils.deepClone(commonRedisProperties);
	}
	
	@Bean(name="dataClientConfiguration")
	public LettuceClientConfiguration dataClientConfiguration(
			@Qualifier("dataRedisProperties")RedisProperties properties,
			@Qualifier("defaultClientResources")ClientResources clientResources
			) {
		return createLettuceClientConfiguration(properties, clientResources, properties.getLettuce().getPool());
	}
	
	@Bean(name="dataRedisConnectionFactory")
	public RedisConnectionFactory dataRedisConnectionFactory(
			@Qualifier("dataRedisProperties")RedisProperties properties,
			@Qualifier("dataClientConfiguration")LettuceClientConfiguration clientConfiguration) {
		return createLettuceConnectionFactory(properties, clientConfiguration);
	}
	
	@Bean(name="redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(
			@Qualifier("dataRedisConnectionFactory")RedisConnectionFactory redisConnectionFactory,
			@Qualifier("defaultRedisKeySerializer")RedisSerializer<String> redisKeySerializer,
			@Qualifier("defaultRedisValueSerializer")RedisSerializer<Object> redisValueSerializer
			) {
		RedisTemplate<String,Object> redisTemplate = new RedisTemplate<String,Object>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(redisKeySerializer);
		redisTemplate.setValueSerializer(redisValueSerializer);
		redisTemplate.setHashKeySerializer(redisKeySerializer);
		redisTemplate.setHashValueSerializer(redisValueSerializer);
		return redisTemplate;
	}
	
}
