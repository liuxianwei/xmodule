package com.penglecode.xmodule.common.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.initializer.SpringAppPostInitializeListener;
import com.penglecode.xmodule.common.initializer.SpringAppPostInitializer;

/**
 * Spring应用后置初始化配置
 * 
 * @author 	pengpeng
 * @date	2018年2月3日 下午5:53:07
 */
@Configuration
@ConditionalOnBean(SpringAppPostInitializer.class)
public class SpringAppPostInitializeConfiguration extends AbstractSpringConfiguration {

	@Bean
	public SpringAppPostInitializeListener springAppPostInitializeListener() {
		return new SpringAppPostInitializeListener();
	}
	
}
