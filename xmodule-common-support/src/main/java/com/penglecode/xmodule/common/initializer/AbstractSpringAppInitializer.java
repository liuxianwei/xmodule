package com.penglecode.xmodule.common.initializer;

import java.lang.reflect.Field;

import org.springframework.context.ConfigurableApplicationContext;

import com.penglecode.xmodule.common.util.ReflectionUtils;

/**
 * 应用程序初始化基类
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月14日 下午4:01:51
 * @version  	1.0
 */
public abstract class AbstractSpringAppInitializer<T extends ConfigurableApplicationContext> {

	protected void setFinalFieldValue(Field field, Object value) {
		if(field != null){
			ReflectionUtils.setFinalFieldValue(null, field, value);
		}
	}
	
	protected void setFinalFieldValue(Class<?> targetClass, String fieldName, Object value) {
		Field field = ReflectionUtils.findField(targetClass, fieldName);
		if(field != null){
			ReflectionUtils.setFinalFieldValue(null, field, value);
		}
	}
	
	public abstract void initialize(T applicationContext);
	
}
