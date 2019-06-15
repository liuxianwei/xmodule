package com.penglecode.xmodule.common.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;

@Configuration
public class DefaultSecurityConfiguration extends AbstractSpringConfiguration {

	/**
	 * spring-security自定义的页面跳转配置
	 */
	@Bean
	@ConfigurationProperties(prefix="spring.security.config")
	public SecurityConfigProperties securityConfigProperties() {
		return new SecurityConfigProperties();
	}
	
}
