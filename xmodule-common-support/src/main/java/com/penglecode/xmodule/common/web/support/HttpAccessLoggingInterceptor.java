package com.penglecode.xmodule.common.web.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpAccessLoggingInterceptor {

	public void beforeAccess(HttpServletRequest request, HttpServletResponse response, HttpAccessLogContext context);
	
	public void afterAccess(HttpServletRequest request, HttpServletResponse response, HttpAccessLogContext context, Throwable exception);
	
}
