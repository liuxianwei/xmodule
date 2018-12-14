package com.penglecode.xmodule.common.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.Assert;

/**
 * 使用Spring BeanWrapper来操作bean属性的工具类
 * 
 * @author 	pengpeng
 * @date	2018年10月11日 下午3:49:23
 */
public class SpringBeanUtils {

	private static final ConversionService defaultConversionService = new DefaultConversionService();
	
	/**
	 * 将properties中的值填充到指定bean中去
	 * @param bean
	 * @param properties
	 * @param conversionService
	 */
	public static void setBeanProperty(Object bean, Map<String,Object> properties, ConversionService conversionService) {
		Assert.notNull(bean, "Parameter 'bean' can not be null!");
		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		beanWrapper.setConversionService(conversionService);
		for(Map.Entry<String,Object> entry : properties.entrySet()) {
			String propertyName = entry.getKey();
			if(beanWrapper.isWritableProperty(propertyName)) {
				beanWrapper.setPropertyValue(propertyName, entry.getValue());
			}
		}
	}
	
	/**
	 * 将properties中的值填充到指定bean中去
	 * @param bean
	 * @param properties
	 */
	public static void setBeanProperty(Object bean, Map<String,Object> properties) {
		setBeanProperty(bean, properties, defaultConversionService);
	}
	
	/**
	 * 将properties中的值填充到指定bean中去
	 * @param beans
	 * @param properties
	 * @param conversionService
	 */
	public static void setBeanProperty(List<Object> beans, Map<String,Object> properties, ConversionService conversionService) {
		Assert.notEmpty(beans, "Parameter 'bean' can not be empty!");
		beans.forEach(bean -> {
			setBeanProperty(bean, properties, conversionService);
		});
	}
	
	/**
	 * 将properties中的值填充到指定bean中去
	 * @param beans
	 * @param properties
	 */
	public static void setBeanProperty(List<Object> beans, Map<String,Object> properties) {
		setBeanProperty(beans, properties, defaultConversionService);
	}
	
}
