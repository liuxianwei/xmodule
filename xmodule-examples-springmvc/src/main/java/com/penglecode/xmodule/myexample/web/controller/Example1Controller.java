package com.penglecode.xmodule.myexample.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.common.web.support.HttpAccessLogging;
import com.penglecode.xmodule.myexample.consts.ExampleConstants;

@RestController
@RequestMapping("/api/example1")
public class Example1Controller extends HttpAPIResourceSupport {

	@RequestMapping(value={"/appid"}, method=GET, produces=APPLICATION_JSON)
	public Object appId(HttpServletRequest request, HttpServletResponse response) {
		return Result.success().message("OK").data(ExampleConstants.APP_ID).build();
	}
	
	@RequestMapping(value={"/time", "/now"}, method=GET, produces=APPLICATION_JSON)
	@HttpAccessLogging(title="NOW_TIME")
	public Object nowTime(HttpServletRequest request, HttpServletResponse response) {
		return Result.success().message("OK").data(DateTimeUtils.formatNow()).build();
	}
	
	@RequestMapping(value="/java/envs", method=GET, produces=APPLICATION_JSON)
	@HttpAccessLogging(title="JAVA_ENVS")
	public Object getJavaEnvs() throws Exception {
		Map<String,String> envs = System.getenv();
		return Result.success().message("OK").data(envs).build();
	}
	
	@RequestMapping(value="/java/env/{envName}", method=GET, produces=APPLICATION_JSON)
	@HttpAccessLogging(title="JAVA_ENV_BY_NAME")
	public Object getJavaEnv(@PathParam("envName")String envName) throws Exception {
		Map<String,String> envs = System.getenv();
		return Result.success().message("OK").data(envs.get(envName)).build();
	}
	
}
