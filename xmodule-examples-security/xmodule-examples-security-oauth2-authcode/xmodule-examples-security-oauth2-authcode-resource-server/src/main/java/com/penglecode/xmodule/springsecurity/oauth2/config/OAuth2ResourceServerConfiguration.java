package com.penglecode.xmodule.springsecurity.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.penglecode.xmodule.common.web.security.oauth2.CustomOAuth2ExceptionRenderer;

/**
 * SpringSecurity-OAuth2资源服务器配置
 * 
 * @author 	pengpeng
 * @date	2019年2月13日 下午5:21:40
 */
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	private static final String DEFAULT_RESOURCE_ID = "demo-resource";
	
	@Autowired
	private ResourceServerTokenServices tokenServices;
	
	/**
	 * OAuth2认证授权异常渲染
	 * @return
	 */
	@Bean(name="oauth2ExceptionRenderer")
	public CustomOAuth2ExceptionRenderer oauth2ExceptionRenderer() {
		CustomOAuth2ExceptionRenderer oauth2ExceptionRenderer = new CustomOAuth2ExceptionRenderer();
		oauth2ExceptionRenderer.setResponseContentType(MediaType.APPLICATION_JSON_UTF8);
		return oauth2ExceptionRenderer;
	}
	
	/**
	 * 令牌异常(未发现令牌、令牌失效等)处理入口
	 * @return
	 */
	@Bean(name="oauth2AuthenticationEntryPoint")
	public AuthenticationEntryPoint oauth2AuthenticationEntryPoint() {
		OAuth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
		oauth2AuthenticationEntryPoint.setExceptionRenderer(oauth2ExceptionRenderer());
		return oauth2AuthenticationEntryPoint;
	}
	
	/**
	 * 禁止访问处理
	 * @return
	 */
	@Bean(name="oauth2AccessDeniedHandler")
	public OAuth2AccessDeniedHandler oauth2AccessDeniedHandler() {
		OAuth2AccessDeniedHandler oauth2AccessDeniedHandler = new OAuth2AccessDeniedHandler();
		oauth2AccessDeniedHandler.setExceptionRenderer(oauth2ExceptionRenderer());
		return oauth2AccessDeniedHandler;
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(DEFAULT_RESOURCE_ID) //如果设置了resourceId，那么必须加入到AuthorizationServer端配置的resourceIds(...)列表中去
				 .tokenServices(tokenServices)
				 .authenticationEntryPoint(oauth2AuthenticationEntryPoint())
				 .accessDeniedHandler(oauth2AccessDeniedHandler());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/api/**") //指定API资源访问安全管辖路径
			.authorizeRequests()
			.antMatchers("/api/server/nowtime").permitAll()
            .anyRequest()
            .authenticated();
	}
	
}
