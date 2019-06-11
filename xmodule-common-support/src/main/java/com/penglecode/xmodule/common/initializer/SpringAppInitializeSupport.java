package com.penglecode.xmodule.common.initializer;

import java.lang.reflect.Field;

import com.penglecode.xmodule.common.util.ReflectionUtils;

/**
 * Spring应用初始化辅助类
 * 
 * @author 	pengpeng
 * @date	2019年6月11日 上午11:38:39
 */
public abstract class SpringAppInitializeSupport {

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
