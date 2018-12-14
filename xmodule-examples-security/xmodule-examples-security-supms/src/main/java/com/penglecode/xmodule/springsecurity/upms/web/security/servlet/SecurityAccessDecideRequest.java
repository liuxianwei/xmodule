package com.penglecode.xmodule.springsecurity.upms.web.security.servlet;

import java.net.URI;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.util.WebUtils;
import com.penglecode.xmodule.common.web.security.consts.SecurityApplicationConstants;

/**
 * 客户端访问权限判断请求(重写了一些方法来达到修改请求URL的目的)
 * 
 * 将请求：/security/accessdecide?url=/user/index.html 修改为 ==> /user/index.html
 * 
 * @author 	pengpeng
 * @date	2018年10月27日 上午9:59:31
 */
public class SecurityAccessDecideRequest extends HttpServletRequestWrapper {

	private final String clientAccessUrl;
	
	private final URI clientAccessUri;
	
	private volatile boolean requestRestored = false;
	
	public SecurityAccessDecideRequest(HttpServletRequest request) {
		super(request);
		Assert.isTrue(isSecurityAccessibleRequest(request), "The wrapping request is not security accessible request!");
		this.clientAccessUrl = resolveClientAccessUrl(request);
		this.clientAccessUri = WebUtils.createURI(this.clientAccessUrl);
	}
	
	protected String resolveClientAccessUrl(HttpServletRequest request) {
		String clientAccessUrl = request.getParameter(SecurityApplicationConstants.CLIENT_ACCESS_URL_PARAM_NAME);
		clientAccessUrl = StringUtils.defaultIfEmpty(clientAccessUrl, request.getHeader("referer"));
		URI uri = WebUtils.createURI(clientAccessUrl);
		if(!StringUtils.isEmpty(uri.getQuery())) {
			return uri.getPath() + "?" + uri.getQuery();
		} else {
			return uri.getPath();
		}
	}

	@Override
	public String getQueryString() {
		if(!requestRestored) {
			return clientAccessUri.getQuery();
		} else {
			return super.getQueryString();
		}
	}

	@Override
	public String getRequestURI() {
		if(!requestRestored) {
			return clientAccessUri.getPath();
		} else {
			return super.getRequestURI();
		}
	}

	@Override
	public StringBuffer getRequestURL() {
		if(!requestRestored) {
			String oldRequestUrl = super.getRequestURL().toString();
			String requestUrl = oldRequestUrl.replace(SecurityApplicationConstants.SECURITY_ACCESS_DECIDE_REQUEST_URL, clientAccessUri.getPath());
			return new StringBuffer(requestUrl);
		} else {
			return super.getRequestURL();
		}
	}

	@Override
	public String getServletPath() {
		if(!requestRestored) {
			return clientAccessUri.getPath();
		} else {
			return super.getServletPath();
		}
	}
	
	public RequestDispatcher getRequestDispatcher(String path) {
		restoreRequest(); //在调用getRequestDispatcher().forward()之前必须恢复为原来的request，否则getRequestDispatcher().forward()不起作用
		return super.getRequestDispatcher(path);
	}

	/**
	 * 还原为原来的Request
	 */
	public void restoreRequest() {
		this.requestRestored = true;
	}
	
	protected String getClientAccessUrl() {
		return clientAccessUrl;
	}

	protected URI getClientAccessUri() {
		return clientAccessUri;
	}

	public static boolean isSecurityAccessibleRequest(HttpServletRequest request) {
		if(SecurityApplicationConstants.SECURITY_ACCESS_DECIDE_REQUEST_URL.equals(request.getRequestURI())) {
			String clientAccessUrl = request.getParameter(SecurityApplicationConstants.CLIENT_ACCESS_URL_PARAM_NAME);
			clientAccessUrl = StringUtils.defaultIfEmpty(clientAccessUrl, request.getHeader("referer"));
			if(!StringUtils.isEmpty(clientAccessUrl) && UrlUtils.isValidRedirectUrl(clientAccessUrl)) {
				return true;
			}
		}
		return false;
	}
	
}
