package com.penglecode.xmodule.myexample.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

@RestController
@RequestMapping("/api")
public class ProviderExampleController extends HttpAPIResourceSupport {

	@RequestMapping(value="/now", method=GET, produces=APPLICATION_JSON)
	public Object nowTime(HttpServletRequest request, HttpServletResponse response) {
		return Result.success().message("OK").data(DateTimeUtils.formatNow()).build();
	}
	
	@RequestMapping(value="/java/envs", method=GET, produces=APPLICATION_JSON)
	public Object getJavaEnvs() throws Exception {
		Map<String,String> envs = System.getenv();
		return Result.success().message("OK").data(envs).build();
	}
	
	@RequestMapping(value="/java/props", method=GET, produces=APPLICATION_JSON)
	public Object getJavaProps() throws Exception {
		Properties props = System.getProperties();
		return Result.success().message("OK").data(props).build();
	}
	
}
