package com.penglecode.xmodule.common.consts;

import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;

/**
 * 框架可配置常量的实现
 * 
 * 注意该实现可能存在bug，产生bug的根源是：在#DefaultSpringAppPreInitializer中常量池初始化(SpringEnvConstantPool.setEnvironment(...))之前，
 * 常量所在的Class类被ClassLoader加载过了,即常量(例如：public static final String MY_CONST = valueOf(...))已经在运行时初始化完毕了，所以出现始终得不到期望的值!
 * 
 * 一个导致该问题出现的常见例子：在context.listener.classes所指定的Listener中使用基于package的类扫描，被扫描的类名应用的Class.forName(), 导致常量所在的类提前被类加载器加载了
 * 
 * @see #SpringEnvConstantPool
 * @see #DefaultSpringAppPreInitializer <code>SpringEnvConstantPool.setEnvironment(...)</code>
 * 
 * @author 	pengpeng
 * @date	2019年5月25日 下午3:13:35
 */
@SuppressWarnings("unchecked")
public final class Constants {

	private static ConstantPool<Object> constantPool = new DefaultEmptyConstantPool<Object>();
	
	public static String valueOf(String name, String defaultValue) {
		return valueOf(name, String.class, defaultValue);
	}
	
	public static <T> T valueOf(String name, Class<T> constType, T defaultValue) {
		if(constantPool.getClass().equals(DefaultEmptyConstantPool.class)) {
			throw new ApplicationRuntimeException("The SpringEnvConstantPool has not been initialized while a public static final constant definition loaded by ClassLoader!");
		}
		return (T) constantPool.valueOf(name, (Class<Object>) constType, defaultValue);
	}

	public static void setConstantPool(ConstantPool<Object> constantPool) {
		Constants.constantPool = constantPool;
	}
	
	public static <T> T valueOf(T defaultValue) {
		return (null != null ? null : defaultValue);
	}
	
	private static class DefaultEmptyConstantPool<T> implements ConstantPool<T> {

		@Override
		public T valueOf(String name, Class<T> constType, T defaultValue) {
			return null;
		}
		
	}
	
}
