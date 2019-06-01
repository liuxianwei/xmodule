package com.penglecode.xmodule.springcloud.nacosexample.web.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.springcloud.nacosexample.service.api.AppInfoApiService;

@RestController
public class AppInfoController implements AppInfoApiService {

	@Resource(name="appInfoApiService")
	private AppInfoApiService appInfoApiService;
	
	@Override
	public Result<Map<String, Object>> getAppInfo() {
		Result<Map<String, Object>> result = appInfoApiService.getAppInfo();
		return result;
	}

}
