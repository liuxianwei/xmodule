package com.penglecode.xmodule.myexample.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.common.web.support.HttpAccessLogging;
import com.penglecode.xmodule.myexample.model.User;

@RestController
@RequestMapping("/api/example2")
public class Example2Controller extends HttpAPIResourceSupport {

	@RequestMapping(value="/app_{appId}/user/{userId}", method=GET, produces=APPLICATION_JSON)
	@HttpAccessLogging(title="GET_APP_USER")
	public Object getAppUser(@PathVariable("appId")String appId, @PathVariable("userId")String userId) throws Exception {
		return Result.success().message("OK").data("(" + appId + "," + userId + ")").build();
	}
	
	@RequestMapping(value="/user/login", method=POST, produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	@HttpAccessLogging(title="USER_LOGIN")
	public Object userLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody User user) {
		return Result.success().message("OK").data(user).build();
	}
	
}
