package com.penglecode.xmodule.springsecurity.simple.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
	
	private final String defaultFailureUrl;
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	public CustomAuthenticationFailureHandler(String defaultFailureUrl) {
		super();
		this.defaultFailureUrl = defaultFailureUrl;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		LOGGER.info(">>> Login Failure!");
		request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
		
		getRedirectStrategy().sendRedirect(request, response, defaultFailureUrl);
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected String getDefaultFailureUrl() {
		return defaultFailureUrl;
	}

}
