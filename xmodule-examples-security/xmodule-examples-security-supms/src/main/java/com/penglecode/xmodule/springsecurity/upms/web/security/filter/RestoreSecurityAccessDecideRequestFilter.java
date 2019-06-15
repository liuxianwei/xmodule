package com.penglecode.xmodule.springsecurity.upms.web.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.penglecode.xmodule.common.security.consts.SecurityApplicationConstants;
import com.penglecode.xmodule.springsecurity.upms.web.security.servlet.SecurityAccessDecideRequest;

public class RestoreSecurityAccessDecideRequestFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		SecurityAccessDecideRequest securityAccessDecideRequest = (SecurityAccessDecideRequest) request.getAttribute(SecurityApplicationConstants.SECURITY_ACCESS_DECIDE_REQUEST_KEY);
		if(securityAccessDecideRequest != null) {
			securityAccessDecideRequest.restoreRequest(); //还原为原来的request
		}
		filterChain.doFilter(request, response);
	}

}
