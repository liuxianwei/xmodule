package com.penglecode.xmodule.springsecurity.upms.web.security.authc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.security.consts.SecurityApplicationConstants;

public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
	
	private RequestCache requestCache = new HttpSessionRequestCache();
	
	private String successHandlerUrl;
	
	public CustomAuthenticationSuccessHandler(String successHandlerUrl, String defaultSuccessUrl, boolean alwaysUseDefaultTargetUrl, String targetUrlParameter) {
		super();
		setSuccessHandlerUrl(successHandlerUrl);
		setDefaultTargetUrl(defaultSuccessUrl);
		setAlwaysUseDefaultTargetUrl(alwaysUseDefaultTargetUrl);
		setTargetUrlParameter(targetUrlParameter);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		LOGGER.info(">>> Login Success!");
		request.getSession().setAttribute(GlobalConstants.UPMS_LOGIN_USER_SESSION_KEY, authentication.getPrincipal());
		onSuper1AuthenticationSuccess(request, response, authentication);
	}
	
	protected void onSuper1AuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) {
		SavedRequest savedRequest = requestCache.getRequest(request, response);

		if (savedRequest == null) {
			onSuper2AuthenticationSuccess(request, response, authentication);
			return;
		}
		String targetUrlParameter = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl()
				|| (targetUrlParameter != null && StringUtils.hasText(request
						.getParameter(targetUrlParameter)))) {
			requestCache.removeRequest(request, response);
			onSuper2AuthenticationSuccess(request, response, authentication);
			return;
		}

		clearAuthenticationAttributes(request);

		// Use the DefaultSavedRequest URL
		String targetUrl = savedRequest.getRedirectUrl();
		LOGGER.debug("Redirecting to target url: " + targetUrl);
		request.setAttribute(SecurityApplicationConstants.SAVED_REQUEST_URL_KEY, targetUrl);
		forwardToSuccessHandler(request, response);
		return;
	}
	
	protected void onSuper2AuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) {
		String targetUrl = determineTargetUrl(request, response);
		LOGGER.debug("Redirecting to target url: " + targetUrl);
		request.setAttribute(SecurityApplicationConstants.SAVED_REQUEST_URL_KEY, targetUrl);
		clearAuthenticationAttributes(request);
		forwardToSuccessHandler(request, response);
	}
	
	protected void forwardToSuccessHandler(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher(getSuccessHandlerUrl()).forward(request, response);
		} catch (Exception e) {
			throw new InternalAuthenticationServiceException(e.getMessage(), e);
		}
	}
	
	protected RequestCache getRequestCache() {
		return requestCache;
	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}

	protected String getSuccessHandlerUrl() {
		return successHandlerUrl;
	}

	public void setSuccessHandlerUrl(String successHandlerUrl) {
		this.successHandlerUrl = successHandlerUrl;
	}

}
