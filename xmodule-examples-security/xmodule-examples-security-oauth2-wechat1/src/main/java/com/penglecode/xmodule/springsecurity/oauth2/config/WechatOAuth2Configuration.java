package com.penglecode.xmodule.springsecurity.oauth2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;

@Configuration
public class WechatOAuth2Configuration extends AbstractSpringConfiguration {

	@Bean
	@ConfigurationProperties(prefix="wechat.oauth2")
	public WechatOAuth2ConfigProperties wechatOAuth2Config() {
		return new WechatOAuth2ConfigProperties();
	}
	
}
