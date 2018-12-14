package com.penglecode.xmodule.springsecurity.simple.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.TEXT_HTML;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.penglecode.xmodule.common.util.ExceptionUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

@Controller
public class ExampleController extends HttpAPIResourceSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleController.class);
	
	@RequestMapping(value="/login/failure", method=POST, produces=TEXT_HTML)
	public String loginFailure(HttpServletRequest request, HttpServletResponse response, Map<String,Object> model) {
		LOGGER.info(">>> login failure! method = {}", request.getMethod());
		Exception exception = getRequestAttribute(request, WebAttributes.AUTHENTICATION_EXCEPTION);
		if(exception == null) {
			exception = getSessionAttribute(request, WebAttributes.AUTHENTICATION_EXCEPTION);
		}
		model.put("error", Boolean.TRUE);
		model.put("message", ExceptionUtils.getRootCauseMessage(exception));
		return "login";
	}
	
	@RequestMapping(value="/login/success", method=POST, produces=TEXT_HTML)
	public String loginSuccess(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info(">>> login success! method = {}", request.getMethod());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		setSessionAttribute(request, "loginUser", authentication.getPrincipal());
		return "hello";
	}
	
	@RequestMapping(value={"/", "/home"}, method=GET, produces=TEXT_HTML)
	public String home(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info(">>> goto home");
		return "home";
	}
	
	@RequestMapping(value="/hello", method=GET, produces=TEXT_HTML)
	public String hello(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info(">>> goto hello");
		return "hello";
	}
	
	@RequestMapping(value="/login", method=GET, produces=TEXT_HTML)
	public String login(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info(">>> goto login");
		return "login";
	}
	
}
