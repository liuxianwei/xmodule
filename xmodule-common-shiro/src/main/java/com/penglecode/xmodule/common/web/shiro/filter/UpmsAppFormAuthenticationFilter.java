package com.penglecode.xmodule.common.web.shiro.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;
import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.util.HttpServletUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.NetUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.shiro.ShiroUtils;
import com.penglecode.xmodule.common.web.shiro.authc.CustomUsernamePasswordToken;
import com.penglecode.xmodule.common.web.shiro.client.ClientSavedRequest;
import com.penglecode.xmodule.common.web.shiro.consts.ShiroApplicationConstants;
import com.penglecode.xmodule.common.web.shiro.service.ShiroPrincipalService;
import com.penglecode.xmodule.common.web.support.LoginToken;
import com.penglecode.xmodule.upms.model.UpmsResource;
import com.penglecode.xmodule.upms.model.UpmsRole;
import com.penglecode.xmodule.upms.model.UpmsUser;

/**
 * 自定义的服务端FormAuthenticationFilter
 * 
 * @author pengpeng
 * @date 2018年6月2日 下午3:12:40
 */
public class UpmsAppFormAuthenticationFilter extends FormAuthenticationFilter implements UnauthenticatedUrlAware {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UpmsAppFormAuthenticationFilter.class);
	
	private String unauthenticatedUrl;
	
	@Autowired
	private ShiroPrincipalService<UpmsUser,UpmsRole,UpmsResource> shiroPrincipalService;
	
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		Long appId = ApplicationConstants.CURRENT_APPLICATION_ID;
		CustomUsernamePasswordToken customToken = (CustomUsernamePasswordToken) token;
		UpmsUser loginUser = (UpmsUser) customToken.getAttributes().get(ShiroApplicationConstants.LOGIN_USER_OBJECT_KEY);
		if(loginUser == null) {
			loginUser = shiroPrincipalService.getPrincipalObject((String)token.getPrincipal());
		}
		LoginToken<UpmsUser> loginToken = shiroPrincipalService.createLoginToken(loginUser, appId);
		loginToken.setLoginAddrIp(NetUtils.getRemoteIpAddr(WebUtils.toHttp(request)));
		ShiroUtils.setSessionAttribute(GlobalConstants.UPMS_LOGIN_TOKEN_SESSION_KEY, loginToken);
		issueSuccessRedirect(request, response);
		return true; //login success and return true, let request chain continue to: @RequestMapping(value="/login")
	}

	@Override
	protected AuthenticationToken createToken(String username, String password, boolean rememberMe, String host) {
		return new CustomUsernamePasswordToken(username, password, rememberMe, host);
	}

	protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        request.setAttribute(getFailureKeyAttribute(), ae);
    }

	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
		Session session = ShiroUtils.getSession();
		ClientSavedRequest savedRequest = (ClientSavedRequest) session.getAttribute(WebUtils.SAVED_REQUEST_KEY);
		String fallbackUrl = null;
		if(savedRequest != null) {
			fallbackUrl = savedRequest.getRequestUrl();
		}
		if(StringUtils.isEmpty(fallbackUrl)) {
			fallbackUrl = getDefaultFallbackUrl(request, response);
		}
		request.setAttribute(ShiroApplicationConstants.AUTHC_FALLBACK_URL_KEY, fallbackUrl);
	}

	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String params = HttpServletUtils.getRequestBody(WebUtils.toHttp(request));
		Map<String,String> paramMap = JsonUtils.json2Object(params, new TypeReference<Map<String,String>>(){});
        return createToken(paramMap.get(getUsernameParam()), paramMap.get(getPasswordParam()), request, response);
    }
	
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

	protected String getDefaultFallbackUrl(ServletRequest request, ServletResponse response) {
		return getSuccessUrl();
	}

	public String getUnauthenticatedUrl() {
		return unauthenticatedUrl;
	}

	public void setUnauthenticatedUrl(String unauthenticatedUrl) {
		this.unauthenticatedUrl = unauthenticatedUrl;
	}

	public ShiroPrincipalService<UpmsUser, UpmsRole, UpmsResource> getShiroPrincipalService() {
		return shiroPrincipalService;
	}

	public void setShiroPrincipalService(ShiroPrincipalService<UpmsUser, UpmsRole, UpmsResource> shiroPrincipalService) {
		this.shiroPrincipalService = shiroPrincipalService;
	}
	
}
