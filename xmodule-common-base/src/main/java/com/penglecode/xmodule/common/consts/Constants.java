package com.penglecode.xmodule.common.consts;

@SuppressWarnings("unchecked")
public final class Constants {

	private static ConstantPool<Object> constantPool = new DefaultConstantPool<Object>();
	
	public static String valueOf(String name, String defaultValue) {
		return valueOf(name, String.class, defaultValue);
	}
	
	public static <T> T valueOf(String name, Class<T> constType, T defaultValue) {
		return (T) constantPool.valueOf(name, (Class<Object>) constType, defaultValue);
	}

	public static void setConstantPool(ConstantPool<Object> constantPool) {
		Constants.constantPool = constantPool;
	}
	
	public static <T> T valueOf(T defaultValue) {
		return (null != null ? null : defaultValue);
	}
	
	private static class DefaultConstantPool<T> implements ConstantPool<T> {

		@Override
		public T valueOf(String name, Class<T> constType, T defaultValue) {
			return null;
		}
		
	}
	
}
