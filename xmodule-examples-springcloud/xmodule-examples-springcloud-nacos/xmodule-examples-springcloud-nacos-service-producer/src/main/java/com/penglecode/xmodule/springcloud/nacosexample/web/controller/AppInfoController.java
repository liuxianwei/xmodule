package com.penglecode.xmodule.springcloud.nacosexample.web.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.springcloud.nacosexample.service.api.AppInfoApiService;

@RestController
public class AppInfoController extends HttpAPIResourceSupport {

	@GetMapping(value="/getAppInfo")
	public Object getAppInfo() {
		Map<String,AppInfoApiService> beans = getApplicationContext().getBeansOfType(AppInfoApiService.class);
		System.out.println(beans);
		return Result.success().message("OK").build();
	}
	
}
