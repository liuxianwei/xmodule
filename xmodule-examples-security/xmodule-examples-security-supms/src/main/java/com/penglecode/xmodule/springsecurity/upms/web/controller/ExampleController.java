package com.penglecode.xmodule.springsecurity.upms.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

@RestController
public class ExampleController extends HttpAPIResourceSupport {

	@GetMapping(value="/example/javaenvs", produces=APPLICATION_JSON)
	public Object getJavaEnvs(HttpServletRequest request, HttpServletResponse response) {
		return Result.success().message("OK").data(System.getenv()).build();
	}
	
}
