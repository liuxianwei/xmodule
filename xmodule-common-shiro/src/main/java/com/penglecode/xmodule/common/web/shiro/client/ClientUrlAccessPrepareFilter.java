package com.penglecode.xmodule.common.web.shiro.client;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.shiro.client.ShiroClientAccessUtils.ShiroClientAccessURL;
import com.penglecode.xmodule.common.web.shiro.consts.ShiroApplicationConstants;
import com.penglecode.xmodule.upms.model.UpmsApp;

/**
 * 客户端请求预处理过滤器
 * 
 * @author 	pengpeng
 * @date	2018年7月11日 上午11:21:27
 */
public class ClientUrlAccessPrepareFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String refererUrl = request.getHeader("referer");
		if(!StringUtils.isEmpty(refererUrl)) {
			ShiroClientAccessURL accessUrl = ShiroClientAccessUtils.createClientAccessURL(refererUrl);
			request.setAttribute(ShiroApplicationConstants.SHIRO_CLIENT_ACCESS_URL, accessUrl);
			UpmsApp currentAccessApp = ShiroClientAccessUtils.getClientAccessAppInfo(accessUrl.getAppWebContextPath());
			request.setAttribute(ShiroApplicationConstants.CURRENT_CLIENT_ACCESS_APPLICATION_ID, currentAccessApp.getAppId());
		}
		filterChain.doFilter(request, response);
	}

}
