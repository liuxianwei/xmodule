package com.penglecode.xmodule.springsecurity.simple.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.TEXT_HTML;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.penglecode.xmodule.common.util.ExceptionUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

@Controller
public class ExampleController extends HttpAPIResourceSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleController.class);
	
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
	
	@RequestMapping(value="/user", method=GET, produces=TEXT_HTML)
	public String user(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info(">>> goto user");
		return "user";
	}
	
	@RequestMapping(value="/login", method=GET, produces=TEXT_HTML)
	public String login(HttpServletRequest request, HttpServletResponse response, Map<String,Object> model) {
		LOGGER.info(">>> goto login");
		Exception exception = getSessionAttribute(request, WebAttributes.AUTHENTICATION_EXCEPTION);
		if(exception != null) {
			model.put("error", Boolean.TRUE);
			model.put("message", ExceptionUtils.getRootCauseMessage(exception));
		}
		return "login";
	}
	
}
