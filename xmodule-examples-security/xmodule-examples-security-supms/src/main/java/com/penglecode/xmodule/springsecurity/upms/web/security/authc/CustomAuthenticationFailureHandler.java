package com.penglecode.xmodule.springsecurity.upms.web.security.authc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
	
	private final String failureHandlerUrl;
	
	public CustomAuthenticationFailureHandler(String failureHandlerUrl) {
		super();
		this.failureHandlerUrl = failureHandlerUrl;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		LOGGER.info(">>> Login Failure!");
		request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
		
		request.getRequestDispatcher(failureHandlerUrl).forward(request, response); //跳转到defaultFailureUrl多指定的controller方法里去进一步处理
		return;
	}

	public String getFailureHandlerUrl() {
		return failureHandlerUrl;
	}

}
