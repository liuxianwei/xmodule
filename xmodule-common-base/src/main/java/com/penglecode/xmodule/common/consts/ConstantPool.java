package com.penglecode.xmodule.common.consts;

/**
 * 常量池
 * @param <T>
 * @author 	pengpeng
 * @date	2019年6月6日 下午12:56:51
 */
public interface ConstantPool<T> {

	public T valueOf(String name, Class<T> constType, T defaultValue);
	
}