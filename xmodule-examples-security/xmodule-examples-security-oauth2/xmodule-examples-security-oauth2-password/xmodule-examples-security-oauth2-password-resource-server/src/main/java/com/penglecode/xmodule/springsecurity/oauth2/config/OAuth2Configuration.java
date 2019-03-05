package com.penglecode.xmodule.springsecurity.oauth2.config;

import static com.penglecode.xmodule.common.redis.LettuceConnectionFactoryUtils.createLettuceClientConfiguration;
import static com.penglecode.xmodule.common.redis.LettuceConnectionFactoryUtils.createLettuceConnectionFactory;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.http.converter.jaxb.JaxbOAuth2ExceptionMessageConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.util.BeanUtils;
import com.penglecode.xmodule.common.web.security.oauth2.Jackson2SerializationStrategy;
import com.penglecode.xmodule.common.web.security.oauth2.OAuth2ClientConfigProperties;
import com.penglecode.xmodule.common.web.security.oauth2.SecurityOAuth2Constants;

import io.lettuce.core.resource.ClientResources;

@Configuration
public class OAuth2Configuration extends AbstractSpringConfiguration {

	@Bean
	@ConfigurationProperties(prefix="spring.security.oauth2.client")
	public OAuth2ClientConfigProperties oauth2ClientConfig() {
		return new OAuth2ClientConfigProperties();
	}
	
	@Bean
	public JaxbOAuth2ExceptionMessageConverter jaxbOAuth2ExceptionMessageConverter() {
		return new JaxbOAuth2ExceptionMessageConverter();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * 基于RemoteTokenServices的token检验配置
	 * 
	 * @author 	pengpeng
	 * @date	2019年2月19日 下午3:21:47
	 */
	@Configuration
	@ConditionalOnProperty(prefix="spring.security.oauth2.client.token", name="storeType", havingValue="memory")
	public static class InMemoryTokenStoreConfiguration extends AbstractSpringConfiguration {
		
		@Autowired
		private OAuth2ClientConfigProperties oauth2ClientConfig;
		
		@Bean(name="oauth2RestTemplate")
		public RestTemplate oauth2RestTemplate(RestTemplateBuilder restTemplateBuilder) {
			return restTemplateBuilder.errorHandler(new DefaultResponseErrorHandler() {
				@Override
				public void handleError(ClientHttpResponse response) throws IOException {
					if (response.getRawStatusCode() != 400) { // Ignore 400
						super.handleError(response);
					}
				}
			}).additionalInterceptors(new ClientHttpRequestInterceptor() {
				@Override
				public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
						throws IOException {
					request.getHeaders().add(SecurityOAuth2Constants.OAUTH2_TOKEN_SERVICE_REQUEST_SOURCE, RemoteTokenServices.class.getSimpleName());
					return execution.execute(request, body);
				}
			}).build();
		}
		
		/**
		 * Token服务
		 * @return
		 */
		@Bean(name="tokenServices")
		public ResourceServerTokenServices tokenServices(@Qualifier("oauth2RestTemplate") RestTemplate restTemplate) {
			RemoteTokenServices tokenServices = new RemoteTokenServices();
			tokenServices.setClientId(oauth2ClientConfig.getClientId());
			tokenServices.setClientSecret(oauth2ClientConfig.getClientSecret());
			tokenServices.setCheckTokenEndpointUrl(oauth2ClientConfig.getToken().getCheckUrl());
			tokenServices.setRestTemplate(restTemplate);
			return tokenServices;
		}
		
	}
	
	/**
	 * 基于远程TokenStore的token检验配置
	 * 
	 * @author 	pengpeng
	 * @date	2019年2月13日 上午10:22:40
	 */
	@Configuration
	@ConditionalOnProperty(prefix="spring.security.oauth2.client.token", name="storeType", havingValue="redis")
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
			tokenStore.setSerializationStrategy(new Jackson2SerializationStrategy());
			//tokenStore.setSerializationStrategy(new ProtostuffSerializationStrategy());
			return tokenStore;
	    }
		
		/**
		 * Token服务
		 * @return
		 */
		@Bean(name="tokenServices")
		public ResourceServerTokenServices tokenServices(@Qualifier("redisTokenStore") TokenStore tokenStore) {
			DefaultTokenServices tokenServices = new DefaultTokenServices();
			tokenServices.setTokenStore(tokenStore);
			tokenServices.setSupportRefreshToken(true);
			tokenServices.setClientDetailsService(null);
			return tokenServices;
		}
		
	}
	
}
