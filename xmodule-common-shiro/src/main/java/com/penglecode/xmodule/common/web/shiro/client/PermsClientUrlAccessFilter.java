package com.penglecode.xmodule.common.web.shiro.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;

import com.penglecode.xmodule.common.web.shiro.ShiroUtils;

public class PermsClientUrlAccessFilter extends ClientUrlAccessFilter {

	@Override
	protected boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response, Object pathConfig) throws Throwable {
		Subject subject = ShiroUtils.getSubject();
        String[] perms = (String[]) pathConfig;

        boolean isPermitted = true;
        if (perms != null && perms.length > 0) {
            if (perms.length == 1) {
                if (!subject.isPermitted(perms[0])) {
                    isPermitted = false;
                }
            } else {
                if (!subject.isPermittedAll(perms)) {
                    isPermitted = false;
                }
            }
        }

        return isPermitted;
	}

	@Override
	public void onAccessDenied(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		request.getRequestDispatcher("/shiro/accessible/perms/denied").forward(request, response); // forward to SpringMVC Controller
	}

	@Override
	public void onAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		request.getRequestDispatcher("/shiro/accessible/perms/allowed").forward(request, response); // forward to SpringMVC Controller
	}


}
