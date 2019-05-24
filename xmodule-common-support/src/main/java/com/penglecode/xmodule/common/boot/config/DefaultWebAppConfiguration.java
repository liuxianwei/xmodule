package com.penglecode.xmodule.common.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.MvvmWebAppConfig;
import com.penglecode.xmodule.common.web.support.DefaultHttpAccessExceptionHandler;
import com.penglecode.xmodule.common.web.support.HttpAccessExceptionHandler;

/**
 * 默认的WEB应用配置
 * 
 * @author 	pengpeng
 * @date	2018年2月3日 下午5:53:07
 */
@Configuration
public class DefaultWebAppConfiguration extends AbstractSpringConfiguration {

	/**
	 * 前后端分离开发模式的应用配置(包括应用的name, url, contextpath等)
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix="spring.cloud.mvvm-webapp-config")
	public MvvmWebAppConfig mvvmWebAppConfig() {
		MvvmWebAppConfig mvvmWebAppConfig = new MvvmWebAppConfig();
		setFinalFieldValue(GlobalConstants.class, "MVVM_WEBAPP_CONFIG", mvvmWebAppConfig);
		return mvvmWebAppConfig;
	}
	
	/**
	 * 配置默认的Http访问异常处理器
	 * @return
	 */
	@Bean(name="defaultHttpAccessExceptionHandler")
	public HttpAccessExceptionHandler defaultHttpAccessExceptionHandler() {
		return new DefaultHttpAccessExceptionHandler();
	}
	
}
