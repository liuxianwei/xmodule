package org.springframework.cloud.openfeign;

public interface HystrixFallbackInvocationHandlerFactory {

	public HystrixFallbackInvocationHandler createInvocationHandler(Class<?> feignClientClass, Throwable cause);
	
}
