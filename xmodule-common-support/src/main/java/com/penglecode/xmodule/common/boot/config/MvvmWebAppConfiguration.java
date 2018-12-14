package com.penglecode.xmodule.common.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.support.MvvmWebAppConfigProperties;

/**
 * 前后端分离开发模式的应用配置(包括应用的name, url, contextpath等)
 * 
 * @author 	pengpeng
 * @date	2018年2月3日 下午5:53:07
 */
@Configuration
public class MvvmWebAppConfiguration extends AbstractSpringConfiguration {

	@Bean
	@ConfigurationProperties(prefix="spring.cloud.mvvm-app-config")
	public MvvmWebAppConfigProperties mvvmWebAppConfig() {
		MvvmWebAppConfigProperties appconfig = new MvvmWebAppConfigProperties();
		setFinalFieldValue(ApplicationConstants.class, "GLOBAL_APP_CONFIG", appconfig);
		return appconfig;
	}
	
}
