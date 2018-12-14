package com.penglecode.xmodule.common.web.shiro.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;

import com.penglecode.xmodule.common.web.shiro.ShiroUtils;

public class AuthcClientUrlAccessFilter extends ClientUrlAccessFilter {

	@Override
	protected boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response, Object pathConfig) throws Throwable {
		Subject subject = ShiroUtils.getSubject();
        return subject.isAuthenticated();
	}

	@Override
	public void onAccessDenied(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		request.getRequestDispatcher("/shiro/accessible/authc/denied").forward(request, response); // forward to SpringMVC Controller
	}

	@Override
	public void onAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		request.getRequestDispatcher("/shiro/accessible/authc/allowed").forward(request, response); // forward to SpringMVC Controller
	}


}
