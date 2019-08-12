package com.penglecode.xmodule.springcloud.consulexample.web.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.springcloud.consulexample.config.AppConfig;
import com.penglecode.xmodule.springcloud.consulexample.consts.ConfigExampleConstants;

@RefreshScope
@RestController
@RequestMapping("/api/config")
public class ConsulConfigController extends HttpAPIResourceSupport {

	@Autowired
	private AppConfig appConfig;
	
	@Value("${app.name:null}")
	private String appName;
	
	@GetMapping(value="/{key}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result<Object> getConfig(@PathVariable("key") String key) {
		String value = getEnvironment().getProperty(key);
		return Result.success().message("OK").data(Collections.singletonMap(key, value)).build();
	}
	
	@GetMapping(value="/app/id", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result<Object> getAppId() {
		Long value = ConfigExampleConstants.APP_ID.value();
		return Result.success().message("OK").data(value).build();
	}
	
	@GetMapping(value="/app/name", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result<Object> getAppName() {
		return Result.success().message("OK").data(appName).build();
	}
	
	@GetMapping(value="/app", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result<Object> getAppInfo() {
		return Result.success().message("OK").data(appConfig.asMap()).build();
	}
	
}
