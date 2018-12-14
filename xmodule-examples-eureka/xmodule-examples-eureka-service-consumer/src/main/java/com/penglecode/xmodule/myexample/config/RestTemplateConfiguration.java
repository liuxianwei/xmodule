package com.penglecode.xmodule.myexample.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;

/**
 * RestTemplate自定义配置
 * 
 * @author 	pengpeng
 * @date	2018年9月4日 上午10:21:47
 */
@Configuration
@ConditionalOnClass(RestTemplate.class)
public class RestTemplateConfiguration extends AbstractSpringConfiguration {

	@LoadBalanced
	@Bean(name="restTemplate")
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}
	
}
