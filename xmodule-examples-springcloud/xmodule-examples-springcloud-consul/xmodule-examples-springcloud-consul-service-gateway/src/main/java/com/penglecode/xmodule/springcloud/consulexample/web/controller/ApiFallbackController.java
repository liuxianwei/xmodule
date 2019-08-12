package com.penglecode.xmodule.springcloud.consulexample.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.cloud.feign.HystrixFallbackResults;
import com.penglecode.xmodule.common.support.Result;

import reactor.core.publisher.Mono;

@RestController
public class ApiFallbackController {

	@RequestMapping("/api/fallback")
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public Mono<Object> apiFallback() {
		Result<Object> body = HystrixFallbackResults.defaultFallbackResult();
		System.out.println(">>> " + body);
		return Mono.just(body);
	}
	
}
