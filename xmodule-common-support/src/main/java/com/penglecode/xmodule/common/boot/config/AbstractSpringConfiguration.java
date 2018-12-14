package com.penglecode.xmodule.common.boot.config;

import java.lang.reflect.Field;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.util.ReflectionUtils;

public abstract class AbstractSpringConfiguration implements EnvironmentAware, ApplicationContextAware {

    private Environment environment;
    
    private ApplicationContext applicationContext;
	
	public ResourcePatternResolver getDefaultResourcePatternResolver() {
		return ApplicationConstants.RESOURCE_PATTERN_RESOLVER;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Environment getEnvironment() {
		return environment;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
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
	
}
