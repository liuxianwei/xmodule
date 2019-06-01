package com.penglecode.xmodule.springcloud.nacosexample.service.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

@RestController("defaultAppInfoApiService")
public class AppInfoApiServiceImpl extends HttpAPIResourceSupport implements AppInfoApiService {

	@Override
	public Result<Map<String, Object>> getAppInfo() {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("appName", getEnvironment().getProperty("spring.application.name"));
		data.put("appPort", getEnvironment().getProperty("server.port"));
		return Result.success().message("OK").data(data).build();
	}

}
