package com.penglecode.xmodule.springsecurity.upms.web.security.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.penglecode.xmodule.common.web.security.consts.SecurityApplicationConstants;

/**
 * 客户端访问权限判断请求过滤器
 * 
 * 例如客户端发送请求：/security/accessdecide?url=/user/index.html
 * 来判断当前登录者有没有权限访问指定的url(/user/index.html)
 * 
 * @author 	pengpeng
 * @date	2018年10月27日 上午11:57:23
 */
public class SecurityAccessDecideRequestFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		boolean wrapRequest = SecurityAccessDecideRequest.isSecurityAccessibleRequest(request);
		if(wrapRequest) {
			request = new SecurityAccessDecideRequest(request); //包裹request来改变请求url
			request.setAttribute(SecurityApplicationConstants.SECURITY_ACCESS_DECIDE_REQUEST_KEY, request); //标记当前请求为客户端访问权限判断请求
		}
		filterChain.doFilter(request, response);
		/*if(wrapRequest) {
			((SecurityAccessDecideRequest) request).restoreRequest(); //还原为原来的request
		}*/
	}

}
