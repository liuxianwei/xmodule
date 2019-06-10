package com.penglecode.xmodule.springcloud.nacosexample.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;

@Configuration
public class NacosConfigConfiguration extends AbstractSpringConfiguration {

	@Bean
	@ConfigurationProperties(prefix="spring.cloud.nacos.config")
	public NacosConfigProperties nacosConfigProperties() {
		return new NacosConfigProperties();
	}
	
}
