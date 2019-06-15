package com.penglecode.xmodule.springsecurity.upms.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.security.config.SecurityConfigProperties;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

@RestController
public class UpmsSecurityController extends HttpAPIResourceSupport {

	@Autowired
	private SecurityConfigProperties securityConfigProperties;
	
	/**
	 * 处理未认证
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/security/unauthenticated", produces=APPLICATION_JSON)
	public Object unauthenticated(HttpServletRequest request, HttpServletResponse response) {
		Integer status = HttpStatus.UNAUTHORIZED.value();
		response.setStatus(status);
		String redirectUrl = securityConfigProperties.getLoginUrl();
		return Result.failure().code(status.toString()).message("Unauthenticated").data(redirectUrl).build();
	}
	
	/**
	 * 处理未授权
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/security/unauthorized", produces=APPLICATION_JSON)
	public Object unauthorized(HttpServletRequest request, HttpServletResponse response) {
		Integer status = HttpStatus.FORBIDDEN.value();
		response.setStatus(status);
		return Result.failure().code(status.toString()).message("Unauthorized").data(null).build();
	}
	
}
