package com.penglecode.xmodule.common.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.initializer.DefaultSpringAppPostInitializer;
import com.penglecode.xmodule.common.initializer.SpringAppEnvironmentInitializer;
import com.penglecode.xmodule.common.initializer.SpringAppPostInitializeListener;

/**
 * 默认的应用后置初始化配置
 * 
 * @author 	pengpeng
 * @date	2018年2月3日 下午5:53:07
 */
@Configuration
public class DefaultAppPostInitializeConfiguration extends AbstractSpringConfiguration {

	/**
	 * 后置初始化监听器
	 * @return
	 */
	@Bean
	public SpringAppPostInitializeListener springAppPostInitializeListener() {
		return new SpringAppPostInitializeListener();
	}
	
	/**
	 * 默认的后置初始化
	 * @return
	 */
	@Bean
	public DefaultSpringAppPostInitializer defaultSpringAppPostInitializer() {
		return new DefaultSpringAppPostInitializer();
	}
	
	/**
	 * 应用的环境变量初始化
	 * @return
	 */
	@Bean
	public SpringAppEnvironmentInitializer springAppEnvironmentInitializer() {
		return new SpringAppEnvironmentInitializer();
	}
	
}
