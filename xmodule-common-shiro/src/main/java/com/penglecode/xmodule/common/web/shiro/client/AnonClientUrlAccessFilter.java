package com.penglecode.xmodule.common.web.shiro.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnonClientUrlAccessFilter extends ClientUrlAccessFilter {

	@Override
	protected boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response, Object pathConfig) throws Throwable {
		return true; // Always return true since we allow access to anyone
	}

	@Override
	public void onAccessDenied(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// code never goes here
	}

	@Override
	public void onAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		request.getRequestDispatcher("/shiro/accessible/anon/allowed").forward(request, response); // forward to SpringMVC Controller
	}


}
