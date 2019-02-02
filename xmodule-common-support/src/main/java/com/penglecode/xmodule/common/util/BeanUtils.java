package com.penglecode.xmodule.common.util;

import java.util.Map;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanMap;

/**
 * bean与bean之间、bean与map之间的转换
 * 
 * @author 	pengpeng
 * @date	2019年1月30日 上午10:19:27
 */
@SuppressWarnings({"unchecked", "deprecation"})
public class BeanUtils extends org.springframework.beans.BeanUtils {

	/**
	 * bean转为map
	 * @param bean
	 * @return
	 */
	public static Map<String,Object> beanToMap(Object bean) {
		return BeanMap.create(bean);
	}
	
	/**
	 * map转为bean
	 * @param source
	 * @param targetType
	 * @return
	 */
	public static <T> T mapToBean(Map<String,Object> source, Class<T> targetType) {
		T target = instantiate(targetType);
		new BeanWrapperImpl(target).setPropertyValues(source);
		return target;
	}
	
	/**
	 * 将Map形式的属性值(properties)填充到bean中去
	 * @param bean
	 * @param properties
	 */
	public static void populate(Object bean, Map<String,Object> properties) {
		new BeanWrapperImpl(bean).setPropertyValues(properties);
	}
	
}
