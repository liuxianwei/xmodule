package com.penglecode.xmodule.upms.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.penglecode.xmodule.upms.model.UpmsUser;
import com.penglecode.xmodule.upms.service.UpmsUserService;
import com.penglecode.xmodule.upms.web.security.authc.UpmsLoginUser;
import com.penglecode.xmodule.upms.web.security.util.UpmsUtils;

/**
 * 给当前访问的session设置用户session对象(测试用)
 * 
 * @author 	pengpeng
 * @date	2019年1月24日 上午10:47:41
 */
public class UpmsLoginUserSessionTestApplyFilter extends OncePerRequestFilter {

	@Autowired
	private UpmsUserService upmsUserService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		UpmsLoginUser loginUser = UpmsUtils.getLoginUser(session);
		if(loginUser == null) {
			UpmsUser user = upmsUserService.getUserByUserName("admin");
			UpmsUtils.setCurrentLoginUser(new UpmsLoginUser(user));
		}
		filterChain.doFilter(request, response);
	}

}
