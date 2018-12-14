package com.penglecode.xmodule.common.support;

public interface SpringTypedBeanManager<T,P> {

	public T getTypedBean(P parameter);
	
}
