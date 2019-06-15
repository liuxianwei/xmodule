package com.penglecode.xmodule.springsecurity.oauth2.config;

import static com.penglecode.xmodule.common.redis.LettuceConnectionFactoryUtils.createLettuceClientConfiguration;
import static com.penglecode.xmodule.common.redis.LettuceConnectionFactoryUtils.createLettuceConnectionFactory;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.http.converter.jaxb.JaxbOAuth2ExceptionMessageConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.security.oauth2.OAuth2ExceptionResponseBodyHandler;
import com.penglecode.xmodule.common.security.oauth2.OAuth2MvcHandlerExceptionResolver;
import com.penglecode.xmodule.common.security.oauth2.OAuth2ServerConfigProperties;
import com.penglecode.xmodule.common.security.oauth2.ProtostuffSerializationStrategy;
import com.penglecode.xmodule.common.util.BeanUtils;
import com.penglecode.xmodule.common.web.springmvc.handler.DefaultMvcHandlerExceptionResolver;

import io.lettuce.core.resource.ClientResources;

@Configuration
public class OAuth2Configuration extends AbstractSpringConfiguration {

	@Bean
	@ConfigurationProperties(prefix="spring.security.oauth2.server")
	public OAuth2ServerConfigProperties oauth2ServerConfig() {
		return new OAuth2ServerConfigProperties();
	}
	
	@Bean
	public JaxbOAuth2ExceptionMessageConverter jaxbOAuth2ExceptionMessageConverter() {
		return new JaxbOAuth2ExceptionMessageConverter();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DefaultMvcHandlerExceptionResolver defaultMvcHandlerExceptionResolver() {
		DefaultMvcHandlerExceptionResolver mvcHandlerExceptionResolver = new OAuth2MvcHandlerExceptionResolver();
		mvcHandlerExceptionResolver.setDefaultExceptionView("/common/error/500.html");
		mvcHandlerExceptionResolver.setOrder(Ordered.LOWEST_PRECEDENCE);
		return mvcHandlerExceptionResolver;
	}
	
	/**
	 * ExceptionHandlerExceptionResolver的顺序要在DefaultMvcHandlerExceptionResolver之前，
	 * 否则OAuth2的一系列Endpoints上的@ExceptionHandler注解将失去作用
	 * @param httpMessageConverters
	 * @return
	 */
	@Bean
	public ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver(HttpMessageConverters httpMessageConverters) {
		ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();
		exceptionHandlerExceptionResolver.setOrder(Ordered.LOWEST_PRECEDENCE + 1);
		exceptionHandlerExceptionResolver.setMessageConverters(httpMessageConverters.getConverters());
		exceptionHandlerExceptionResolver.setResponseBodyAdvice(Arrays.asList(new OAuth2ExceptionResponseBodyHandler())); //对OAuth2Exception异常进行单独处理(报文格式的统一)
		return exceptionHandlerExceptionResolver;
	}
	
	/**
	 * 基于内存存储的TokenStore配置
	 * 
	 * @author 	pengpeng
	 * @date	2019年2月13日 下午5:17:50
	 */
	@Configuration
	@ConditionalOnProperty(prefix="spring.security.oauth2.server.token", name="storeType", havingValue="memory")
	public static class InMemoryTokenStoreConfiguration extends AbstractSpringConfiguration {

		@Bean(name="memoryTokenStore")
		public TokenStore memoryTokenStore() {
			return new InMemoryTokenStore();
		}
		
	}
	
	/**
	 * 基于Redis存储的TokenStore配置
	 * 
	 * @author 	pengpeng
	 * @date	2019年2月13日 上午10:22:40
	 */
	@Configuration
	@ConditionalOnProperty(prefix="spring.security.oauth2.server.token", name="storeType", havingValue="redis")
	public static class RedisTokenStoreConfiguration extends AbstractSpringConfiguration {
		
		@Bean(name="securityRedisProperties")
		@ConfigurationProperties(prefix="spring.redis.security")
		public RedisProperties securityRedisProperties(@Qualifier("commonRedisProperties")RedisProperties commonRedisProperties) {
			return BeanUtils.deepClone(commonRedisProperties);
		}
		
		@Bean(name="securityClientConfiguration")
		public LettuceClientConfiguration securityClientConfiguration(
				@Qualifier("securityRedisProperties")RedisProperties properties,
				@Qualifier("defaultClientResources")ClientResources clientResources
				) {
			return createLettuceClientConfiguration(properties, clientResources, properties.getLettuce().getPool());
		}
		
		@Bean(name="securityRedisConnectionFactory")
		public RedisConnectionFactory securityRedisConnectionFactory(
				@Qualifier("securityRedisProperties")RedisProperties properties,
				@Qualifier("securityClientConfiguration")LettuceClientConfiguration clientConfiguration) {
			return createLettuceConnectionFactory(properties, clientConfiguration);
		}
		
		@Bean(name="redisTokenStore")
	    public TokenStore redisTokenStore(@Qualifier("securityRedisConnectionFactory")RedisConnectionFactory redisConnectionFactory) {
			RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
			//tokenStore.setSerializationStrategy(new Jackson2SerializationStrategy());
			tokenStore.setSerializationStrategy(new ProtostuffSerializationStrategy());
			return tokenStore;
	    }
		
	}
	
}
