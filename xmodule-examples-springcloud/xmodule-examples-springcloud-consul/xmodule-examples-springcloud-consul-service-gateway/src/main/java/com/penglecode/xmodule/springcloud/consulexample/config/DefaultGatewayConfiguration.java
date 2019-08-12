package com.penglecode.xmodule.springcloud.consulexample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.springcloud.gateway.filter.factory.CustomRetryGatewayFilterFactory;

/**
 * 默认的gateway网关配置
 * 
 * @author 	pengpeng
 * @date	2019年7月16日 上午10:17:24
 */
@Configuration
public class DefaultGatewayConfiguration extends AbstractSpringConfiguration {

	@Bean
	public CustomRetryGatewayFilterFactory customRetryGatewayFilterFactory() {
		return new CustomRetryGatewayFilterFactory();
	}
	
}
