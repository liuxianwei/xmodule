package com.penglecode.xmodule.common.consts;

public interface ConstantPool<T> {

	public T valueOf(String name, Class<T> constType, T defaultValue);
	
}
