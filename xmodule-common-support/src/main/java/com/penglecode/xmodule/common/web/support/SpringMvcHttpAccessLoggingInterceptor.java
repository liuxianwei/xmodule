package com.penglecode.xmodule.common.web.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SpringMvcHttpAccessLoggingInterceptor implements HttpAccessLoggingInterceptor {

	@Override
	public void beforeAccess(HttpServletRequest request, HttpServletResponse response, HttpAccessLogContext context) {
		
	}

	@Override
	public void afterAccess(HttpServletRequest request, HttpServletResponse response, HttpAccessLogContext context,
			Throwable exception) {
		
	}

}
