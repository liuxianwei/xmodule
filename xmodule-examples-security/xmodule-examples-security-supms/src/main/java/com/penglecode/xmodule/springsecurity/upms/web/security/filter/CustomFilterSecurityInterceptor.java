package com.penglecode.xmodule.springsecurity.upms.web.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * 自定义的URL-ROLE权限拦截器
 * 重写invoke方法，invoke方法的重写逻辑完全copy自父类
 * 本类仅改了FILTER_APPLIED的值，从而避免后续FilterSecurityInterceptor能够被继续链式调用
 * 
 * @author 	pengpeng
 * @date	2018年10月26日 上午9:34:35
 */
public class CustomFilterSecurityInterceptor extends FilterSecurityInterceptor {

	private static final String FILTER_APPLIED = "__spring_security_customFilterSecurityInterceptor_filterApplied";
	
	public void invoke(FilterInvocation fi) throws IOException, ServletException {
		if ((fi.getRequest() != null)
				&& (fi.getRequest().getAttribute(FILTER_APPLIED) != null)
				&& isObserveOncePerRequest()) {
			// filter already applied to this request and user wants us to observe
			// once-per-request handling, so don't re-do security checking
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		}
		else {
			// first time this request being called, so perform security checking
			if (fi.getRequest() != null && isObserveOncePerRequest()) {
				fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
			}

			InterceptorStatusToken token = super.beforeInvocation(fi);

			try {
				fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
			}
			finally {
				super.finallyInvocation(token);
			}

			super.afterInvocation(token, null);
		}
	}
	
}
