package com.penglecode.xmodule.common.consts;

import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

/**
 * Spring托管的可配置常量基类
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月18日 下午10:43:06
 * @version  	1.0
 */
public abstract class SpringManagedConstants {

	/**
	 * Spring的Environment
	 */
	private static volatile Environment environment;
	
	public static Environment getEnvironment() {
		Assert.notNull(environment, "The environment must be initialized when application startup!");
		return environment;
	}

	public static void setEnvironment(Environment environment) {
		SpringManagedConstants.environment = environment;
	}
	
	/**
	 * 从Spring管理的Properties文件中获取
	 * @param property
	 * @return
	 */
	protected static String valueOf(String property, String defaultValue) {
		return getEnvironment().getProperty(property, defaultValue);
	}
	
	/**
	 * 从Spring管理的Properties文件中获取
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	protected static <T> T valueOf(String property, Class<T> targetType, T defaultValue) {
		return getEnvironment().getProperty(property, targetType, defaultValue);
	}
	
	protected static <T> T valueOf(T defaultValue) {
		return (null != null ? null : defaultValue);
	}
	
}
