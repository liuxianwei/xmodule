package com.penglecode.xmodule.common.web.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.penglecode.xmodule.common.web.shiro.consts.ShiroApplicationConstants;

/**
 * 共通的PermissionsAuthorizationFilter
 * 
 * @author 	pengpeng
 * @date	2018年6月27日 下午3:05:33
 */
public class CommonPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter implements UnauthenticatedUrlAware {

	private String unauthenticatedUrl;
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = getSubject(request, response);
        // If the subject isn't identified, redirect to login URL
        if (subject.getPrincipal() == null) {
            saveRequestAndRedirectToLogin(request, response);
        } else {
            // If subject is known but not authorized, redirect to the unauthorized URL if there is one
            // If no unauthorized URL is specified, just return an unauthorized HTTP status code
            String unauthorizedUrl = getUnauthorizedUrl();
            //SHIRO-142 - ensure that redirect _or_ error code occurs - both cannot happen due to response commit:
            if (StringUtils.hasText(unauthorizedUrl)) {
            	try {
					request.setAttribute(ShiroApplicationConstants.UNAUTHORIZED_REQUEST_URLKEY, WebUtils.getRequestUri(WebUtils.toHttp(request)));
					request.getRequestDispatcher(getUnauthorizedUrl()).forward(request, response); //跳转到@RequestMapping(value="/unauthorized")
				} catch (ServletException e) {
					e.printStackTrace();
				}
                //WebUtils.issueRedirect(request, response, unauthorizedUrl);
            } else {
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        return false;
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
	protected void saveRequest(ServletRequest request) {
		// 因为后台服务提供的都是ajax请求服务，所以此处saveRequest将不做任何事情，具体的saveRequest逻辑请见：#ClientUrlAccessibleServlet.saveRequest()
	}

	public String getUnauthenticatedUrl() {
		return unauthenticatedUrl;
	}

	public void setUnauthenticatedUrl(String unauthenticatedUrl) {
		this.unauthenticatedUrl = unauthenticatedUrl;
	}
	
}
