package com.penglecode.xmodule.myexample.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

@Controller
public class WelcomeController extends HttpAPIResourceSupport {

	@RequestMapping("/welcome")
	public String welcome() {
		return "welcome";
	}
	
	@ResponseBody
	@RequestMapping("/try-exception")
	public Object tryException(String value) {
		return Double.parseDouble(value);
	}
	
}
