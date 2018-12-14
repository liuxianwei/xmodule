package com.penglecode.xmodule.common.web.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.shiro.ShiroUtils;
import com.penglecode.xmodule.common.web.shiro.client.ClientSavedRequest;
import com.penglecode.xmodule.common.web.shiro.consts.ShiroApplicationConstants;

/**
 * 共通的UserFilter
 * 
 * @author 	pengpeng
 * @date	2018年6月12日 上午11:29:04
 */
public class CommonAppUserFilter extends UserFilter implements UnauthenticatedUrlAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonAppUserFilter.class);
	
	private String unauthenticatedUrl;
	
	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		try {
			request.setAttribute(ShiroApplicationConstants.UNAUTHENTICATED_REQUEST_URLKEY, WebUtils.getRequestUri(WebUtils.toHttp(request)));
			request.getRequestDispatcher(getUnauthenticatedUrl()).forward(request, response); //跳转到@RequestMapping(value="/unauthenticated")
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void saveRequest(ServletRequest req) {
		//API服务提供的全是Ajax接口，因此saveRequest执行记录该Ajax请求来自的源页面(Referer)
		HttpServletRequest request = WebUtils.toHttp(req);
		String backUrl = request.getHeader("referer");
		if(!StringUtils.isEmpty(backUrl)) {
	        Session session = ShiroUtils.getSession();
	        ClientSavedRequest savedRequest = new ClientSavedRequest(request, backUrl);
	        session.setAttribute(WebUtils.SAVED_REQUEST_KEY, savedRequest);
	        LOGGER.info(">>> Saving request url : " + savedRequest.getRequestUrl());
		}
	}

	public String getUnauthenticatedUrl() {
		return unauthenticatedUrl;
	}

	public void setUnauthenticatedUrl(String unauthenticatedUrl) {
		this.unauthenticatedUrl = unauthenticatedUrl;
	}

}
