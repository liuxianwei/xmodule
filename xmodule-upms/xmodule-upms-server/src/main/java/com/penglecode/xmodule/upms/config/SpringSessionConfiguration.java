package com.penglecode.xmodule.upms.config;

import static com.penglecode.xmodule.common.redis.LettuceConnectionFactoryUtils.createLettuceClientConfiguration;
import static com.penglecode.xmodule.common.redis.LettuceConnectionFactoryUtils.createLettuceConnectionFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.SpringSessionRedisConnectionFactory;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.redis.serializer.ProtostuffRedisSerializer;
import com.penglecode.xmodule.common.util.BeanUtils;

import io.lettuce.core.resource.ClientResources;

@Configuration
@ConditionalOnWebApplication(type=Type.SERVLET)
public class SpringSessionConfiguration extends AbstractSpringConfiguration {

	/** Spring-Session Redis配置 */
	
	@Bean(name="springSessionRedisProperties")
	@ConfigurationProperties(prefix="spring.redis.session")
	public RedisProperties sessionRedisProperties(@Qualifier("commonRedisProperties")RedisProperties commonRedisProperties) {
		return BeanUtils.deepClone(commonRedisProperties);
	}
	
	@Bean(name="springSessionClientConfiguration")
	public LettuceClientConfiguration sessionClientConfiguration(
			@Qualifier("springSessionRedisProperties")RedisProperties properties,
			@Qualifier("defaultClientResources")ClientResources clientResources
			) {
		return createLettuceClientConfiguration(properties, clientResources, properties.getLettuce().getPool());
	}
	
	@SpringSessionRedisConnectionFactory
	@Bean(name="springSessionRedisConnectionFactory")
	public RedisConnectionFactory sessionRedisConnectionFactory(
			@Qualifier("springSessionRedisProperties")RedisProperties properties,
			@Qualifier("springSessionClientConfiguration")LettuceClientConfiguration clientConfiguration) {
		return createLettuceConnectionFactory(properties, clientConfiguration);
	}
	
	@Bean(name="springSessionDefaultRedisSerializer")
	public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
		return new ProtostuffRedisSerializer();
	}
	
}
