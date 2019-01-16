package com.penglecode.xmodule.springsecurity.upms.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.ModuleExceptionResolver;
import com.penglecode.xmodule.common.support.MvvmAppConfig;
import com.penglecode.xmodule.common.support.ModuleExceptionResolver.ExceptionMetadata;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.security.config.SecurityConfigProperties;
import com.penglecode.xmodule.common.web.security.consts.SecurityApplicationConstants;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.springsecurity.upms.web.security.authc.LoginUser;

@RestController
public class UpmsLoginController extends HttpAPIResourceSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpmsLoginController.class);
	
	@Autowired
	private SecurityConfigProperties securityConfigProperties;
	
	@Autowired
	private MvvmAppConfig mvvmWebAppConfigProperties;
	
	/**
	 * 登录出错
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping(value="/login/failure", produces=APPLICATION_JSON)
	public Object loginFailure(HttpServletRequest request, HttpServletResponse response) {
		Exception exception = getSessionAttribute(request, WebAttributes.AUTHENTICATION_EXCEPTION);
		String message = "未知错误!";
		String code = "500";
		if(exception != null) {
			ExceptionMetadata em = ModuleExceptionResolver.resolveException(exception);
			message = em.getMessage();
			code = em.getCode();
		}
		LOGGER.error(">>> 登录出错! 错误信息：{}", message);
		return Result.failure().code(code).message(message).build();
	}
	
	/**
	 * 登录成功
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping(value="/login/success", produces=APPLICATION_JSON)
	public Object loginSuccess(HttpServletRequest request, HttpServletResponse response) {
		LoginUser loginUser = getSessionAttribute(request, GlobalConstants.UPMS_LOGIN_USER_SESSION_KEY);
		LOGGER.info(">>> 登录成功! loginUser = {}", loginUser);
		String redirectUrl = getRequestAttribute(request, SecurityApplicationConstants.SAVED_REQUEST_URL_KEY);
		return Result.success().code("200").message("OK").data(redirectUrl).build();
	}
	
	/**
	 * 获取登录用户信息
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping(value="/login/user/current", produces=APPLICATION_JSON)
	public Object getLoginUser(HttpServletRequest request, HttpServletResponse response) {
		LoginUser loginUser = getSessionAttribute(request, GlobalConstants.UPMS_LOGIN_USER_SESSION_KEY);
		Map<String,Object> loginUserMap = null;
		if(loginUser != null) {
			loginUserMap = loginUser.asMap();
		}
		LOGGER.info(">>> 获取登录用户信息! loginUser = {}", loginUserMap);
		return Result.success().code("200").message("OK").data(loginUserMap).build();
	}
	
	/**
	 * 退出成功
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping(value="/logout/success", produces=APPLICATION_JSON)
	public Object logoutSuccess(HttpServletRequest request, HttpServletResponse response) {
		String redirectUrl = getLoginUrl();
		LOGGER.info(">>> 退出成功! Redirect to target url : {}", redirectUrl);
		return Result.success().code("200").message("OK").data(redirectUrl).build();
	}
	
	protected String getLoginUrl() {
		return mvvmWebAppConfigProperties.getAppWebContextPath() + securityConfigProperties.getLoginUrl();
	}
	
}
