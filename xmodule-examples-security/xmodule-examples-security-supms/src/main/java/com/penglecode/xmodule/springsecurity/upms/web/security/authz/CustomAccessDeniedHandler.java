package com.penglecode.xmodule.springsecurity.upms.web.security.authz;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.web.security.consts.SecurityApplicationConstants;
import com.penglecode.xmodule.springsecurity.upms.web.security.servlet.SecurityAccessDecideRequest;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	/**
	 * 正常访问请求处理url
	 */
	private final String normalAccessHandlerUrl;
	
	/**
	 * 客户端访问权限判断请求处理url
	 */
	private final String decideAccessHandlerUrl;
	
	public CustomAccessDeniedHandler(String normalAccessHandlerUrl, String decideAccessHandlerUrl) {
		super();
		Assert.hasText(normalAccessHandlerUrl, "Parameter 'normalAccessHandlerUrl' must be required!");
		Assert.hasText(decideAccessHandlerUrl, "Parameter 'decideAccessHandlerUrl' must be required!");
		this.normalAccessHandlerUrl = normalAccessHandlerUrl;
		this.decideAccessHandlerUrl = decideAccessHandlerUrl;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);
		HttpServletRequest securityAccessDecideRequest = (SecurityAccessDecideRequest) request.getAttribute(SecurityApplicationConstants.SECURITY_ACCESS_DECIDE_REQUEST_KEY);
		if(securityAccessDecideRequest != null) {
			request.getRequestDispatcher(decideAccessHandlerUrl).forward(request, response);
		} else {
			request.getRequestDispatcher(normalAccessHandlerUrl).forward(request, response);
		}
		return;
	}

	protected String getNormalAccessHandlerUrl() {
		return normalAccessHandlerUrl;
	}

	protected String getDecideAccessHandlerUrl() {
		return decideAccessHandlerUrl;
	}
	
}
