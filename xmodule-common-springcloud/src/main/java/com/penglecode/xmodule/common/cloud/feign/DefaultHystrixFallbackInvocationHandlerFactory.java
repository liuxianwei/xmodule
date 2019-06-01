package com.penglecode.xmodule.common.cloud.feign;

import org.springframework.cloud.openfeign.HystrixFallbackInvocationHandler;
import org.springframework.cloud.openfeign.HystrixFallbackInvocationHandlerFactory;

public class DefaultHystrixFallbackInvocationHandlerFactory implements HystrixFallbackInvocationHandlerFactory {

	@Override
	public HystrixFallbackInvocationHandler createInvocationHandler(Class<?> feignClientClass, Throwable cause) {
		return new DefaultHystrixFallbackInvocationHandler(feignClientClass, cause);
	}

}
