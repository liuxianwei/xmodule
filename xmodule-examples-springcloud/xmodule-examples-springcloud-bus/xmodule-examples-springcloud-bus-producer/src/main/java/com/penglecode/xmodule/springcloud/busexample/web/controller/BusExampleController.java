package com.penglecode.xmodule.springcloud.busexample.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.springcloud.busexample.model.AppInfo;
import com.penglecode.xmodule.springcloud.busexample.service.AppInfoService;

@RestController
public class BusExampleController extends HttpAPIResourceSupport {

	@Autowired
	private AppInfoService appInfoService;
	
	@PostMapping(value="/api/busexample/appinfo/add", produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Object> addAppInfo(@RequestBody AppInfo appInfo) {
		appInfoService.addAppInfo(appInfo);
		return Result.success().message("OK").build();
	}
	
	@PutMapping(value="/api/busexample/appinfo/update", produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Object> updateAppInfoById(@RequestBody AppInfo appInfo) {
		appInfoService.updateAppInfoById(appInfo);
		return Result.success().message("OK").build();
	}
	
	@DeleteMapping(value="/api/busexample/appinfo/delete/{appId}", consumes=APPLICATION_JSON)
	public Result<Object> deleteAppInfoById(@PathVariable("appId") Long appId) {
		appInfoService.deleteAppInfoById(appId);
		return Result.success().message("OK").build();
	}
}
