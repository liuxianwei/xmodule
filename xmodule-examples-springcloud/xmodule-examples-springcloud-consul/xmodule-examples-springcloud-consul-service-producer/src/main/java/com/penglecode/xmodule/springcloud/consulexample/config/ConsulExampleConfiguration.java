package com.penglecode.xmodule.springcloud.consulexample.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;

@Configuration
public class ConsulExampleConfiguration extends AbstractSpringConfiguration {

	@Bean
	@RefreshScope
	@ConfigurationProperties(prefix="app")
	public AppConfig appConfig() {
		return new AppConfig();
	}
	
}
