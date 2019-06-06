package com.penglecode.xmodule.common.consts;

import java.lang.reflect.ParameterizedType;

/**
 * 常量
 * @param <T>
 * @author 	pengpeng
 * @date	2019年6月6日 下午12:50:52
 */
@SuppressWarnings("unchecked")
public abstract class Constant<T> {

	private static ConstantPool<Object> constantPool = new DefaultEmptyConstantPool<>();
	
	private final String name;
	
	private final Class<T> type;
	
	private final T defaultValue;

	public Constant(String name) {
		this(name, null);
	}
	
	public Constant(String name, T defaultValue) {
		super();
		this.name = name;
		this.defaultValue = defaultValue;
		//this.type = (Class<T>) ResolvableType.forClass(getClass()).getSuperType().getGeneric(0).resolve();
		this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	 * 返回常量值
	 * @return
	 */
	public T value() {
		return (T) constantPool.valueOf(name, (Class<Object>) type, defaultValue);
	}

	/**
	 * 使用defaultOf()用在public static final常量上，是的后期使用反射修改其值成为可能
	 * @param defaultValue
	 * @return
	 */
	public static <T> T defaultOf(T defaultValue) {
		return (null != null ? null : defaultValue);
	}
	
	protected static void setConstantPool(ConstantPool<Object> constantPool) {
		Constant.constantPool = constantPool;
	}
	
	private static class DefaultEmptyConstantPool<T> implements ConstantPool<T> {

		@Override
		public T valueOf(String name, Class<T> type, T defaultValue) {
			return null;
		}
		
	}
	
}
