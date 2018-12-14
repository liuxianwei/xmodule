package com.penglecode.xmodule.myexample.support;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.penglecode.xmodule.common.web.filter.AbstractSpringMvcHttpAccessLoggingFilter;
import com.penglecode.xmodule.common.web.support.HttpAccessLogContext;
import com.penglecode.xmodule.myexample.model.User;

@Component
public class DefaultSpringMvcHttpAccessLoggingFilter extends AbstractSpringMvcHttpAccessLoggingFilter {

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T getAccessUser(HttpServletRequest request, HttpAccessLogContext context) {
		User user = new User();
		user.setUserId(1L);
		user.setUserName("admin");
		user.setPassword("123456");
		return (T) user;
	}

}
