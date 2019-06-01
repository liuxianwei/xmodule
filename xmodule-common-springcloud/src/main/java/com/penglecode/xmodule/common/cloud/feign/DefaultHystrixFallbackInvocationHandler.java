package com.penglecode.xmodule.common.cloud.feign;

import java.lang.reflect.Method;

import org.springframework.cloud.openfeign.HystrixFallbackInvocationHandler;

import com.penglecode.xmodule.common.support.PageResult;

public class DefaultHystrixFallbackInvocationHandler extends HystrixFallbackInvocationHandler {

	public DefaultHystrixFallbackInvocationHandler(Class<?> feignClientClass, Throwable cause) {
		super(feignClientClass, cause);
	}

	@Override
	protected Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
		Class<?> returnType = method.getReturnType();
		if(PageResult.class.equals(returnType)) {
			return HystrixFallbackResults.defaultFallbackPageResult();
		} else {
			return HystrixFallbackResults.defaultFallbackResult();
		}
	}

}
