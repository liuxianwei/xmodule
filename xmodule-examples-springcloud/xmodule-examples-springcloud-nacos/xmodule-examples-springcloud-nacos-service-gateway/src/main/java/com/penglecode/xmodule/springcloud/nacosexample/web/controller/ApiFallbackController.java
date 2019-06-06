package com.penglecode.xmodule.springcloud.nacosexample.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.cloud.feign.HystrixFallbackResults;

@RestController
public class ApiFallbackController {

	@RequestMapping("/api/fallback")
	public Object apiFallback() {
		return HystrixFallbackResults.defaultFallbackResult();
	}
	
}
