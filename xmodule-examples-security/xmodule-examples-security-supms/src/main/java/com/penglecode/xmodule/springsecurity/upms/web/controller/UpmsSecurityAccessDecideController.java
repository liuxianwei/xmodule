package com.penglecode.xmodule.springsecurity.upms.web.controller;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;
import com.penglecode.xmodule.common.support.MvvmWebAppConfig;
import com.penglecode.xmodule.common.util.FileUtils;
import com.penglecode.xmodule.common.util.ResourceUtils;
import com.penglecode.xmodule.common.web.security.config.SecurityConfigProperties;
import com.penglecode.xmodule.common.web.springmvc.view.TextJavascriptView;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

@Controller
public class UpmsSecurityAccessDecideController extends HttpAPIResourceSupport {

	@Autowired
	private SecurityConfigProperties securityConfigProperties;
	
	@Autowired
	private MvvmWebAppConfig mvvmWebAppConfigProperties;
	
	/**
	 * 客户端访问权限判断请求被允许
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/security/accessdecide")
	public Object accessAllowed(HttpServletRequest request, HttpServletResponse response) {
		String javascript = getDefaultJavascript4AccessAllowed();
		return createModelAndView(request, response, javascript);
	}
	
	/**
	 * 处理未认证
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/security/accessdecide/unauthenticated")
	public Object accessUnauthenticated(HttpServletRequest request, HttpServletResponse response) {
		String redirectUrl = getRedirectUrl(securityConfigProperties.getLoginUrl());
		String message = "不好意思，您还未登录!";
		String jsTemplateName = "authc_access_denied.js";
		String javascript = getJavascriptByTemplate(jsTemplateName, message, redirectUrl);
		return createModelAndView(request, response, javascript);
	}
	
	/**
	 * 处理未授权
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/security/accessdecide/unauthorized")
	public Object accessUnauthorized(HttpServletRequest request, HttpServletResponse response) {
		String redirectUrl = getRedirectUrl(securityConfigProperties.getUnauthorizedUrl());
		String message = "不好意思，您没有权限访问该资源!";
		String jsTemplateName = "authz_access_denied.js";
		String javascript = getJavascriptByTemplate(jsTemplateName, message, redirectUrl);
		return createModelAndView(request, response, javascript);
	}
	
	protected String getRedirectUrl(String redirectUrl) {
		return mvvmWebAppConfigProperties.getAppWebContextPath() + redirectUrl;
	}
	
	protected ModelAndView createModelAndView(HttpServletRequest request, HttpServletResponse response, String javascript) {
		TextJavascriptView v = new TextJavascriptView();
		ModelAndView mv = new ModelAndView(v, v.getOutputContentModelKey(), javascript.toString());
		mv.setStatus(HttpStatus.OK);
		return mv;
	}
	
	protected String getJavascriptTemplateLocation(String jsTemplateName) {
		return String.format("classpath:static/js/security/%s", jsTemplateName);
	}
	
	protected String getJavascriptByTemplate(String jsTemplateName, Object... args) {
		try {
			String jsTemplateLocation = getJavascriptTemplateLocation(jsTemplateName);
			Resource jsResource = ResourceUtils.getResource(jsTemplateLocation);
			String jsTemplate = FileUtils.readFileToString(jsResource.getFile(), GlobalConstants.DEFAULT_CHARSET);
			return MessageFormat.format(jsTemplate, args);
		} catch (IOException e) {
			throw new ApplicationRuntimeException(e.getMessage(), e);
		}
	}
	
	protected String getDefaultJavascript4AccessAllowed() {
		return "var result = {\"message\": \"Access Allowed\"}";
	}
	
}
