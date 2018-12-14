package com.penglecode.xmodule.springsecurity.simple.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
	
	public CustomAuthenticationSuccessHandler(String defaultSuccessUrl, boolean alwaysUseDefaultTargetUrl, String targetUrlParameter) {
		super();
		setDefaultTargetUrl(defaultSuccessUrl);
		setAlwaysUseDefaultTargetUrl(alwaysUseDefaultTargetUrl);
		setTargetUrlParameter(targetUrlParameter);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		LOGGER.info(">>> Login Success!");
		request.getSession().setAttribute("loginUser", authentication.getPrincipal());
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
