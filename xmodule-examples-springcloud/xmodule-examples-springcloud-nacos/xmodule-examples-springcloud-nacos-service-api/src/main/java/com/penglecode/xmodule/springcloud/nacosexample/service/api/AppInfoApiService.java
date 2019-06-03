package com.penglecode.xmodule.springcloud.nacosexample.service.api;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import java.util.Map;

import org.springframework.cloud.openfeign.FallbackableFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.DefaultHystrixFallbackFactory;
import org.springframework.web.bind.annotation.GetMapping;

import com.penglecode.xmodule.common.support.Result;

@FeignClient(name="springcloud-nacos-producer", qualifier="appInfoApiService", contextId="appInfoApiService", fallbackFactory=DefaultHystrixFallbackFactory.class)
public interface AppInfoApiService extends FallbackableFeignClient {

	@GetMapping(value="/api/app/info", produces=APPLICATION_JSON)
	public Result<Map<String,Object>> getAppInfo();
	
}
