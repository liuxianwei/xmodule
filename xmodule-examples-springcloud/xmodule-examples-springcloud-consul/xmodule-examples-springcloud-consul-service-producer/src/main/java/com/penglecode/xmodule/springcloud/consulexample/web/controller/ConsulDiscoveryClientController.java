package com.penglecode.xmodule.springcloud.consulexample.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

@RestController
public class ConsulDiscoveryClientController extends HttpAPIResourceSupport {

	@Autowired
	private DiscoveryClient discoveryClient;
	
	@GetMapping(value="/api/consul/discovery/services", produces=APPLICATION_JSON)
	public Result<Object> getServices() {
		return Result.success().message("OK").data(discoveryClient.getServices()).build();
	}
	
	@GetMapping(value="/api/consul/discovery/instances/{serviceId}", produces=APPLICATION_JSON)
	public Result<Object> getInstances(@PathVariable("serviceId") String serviceId) {
		return Result.success().message("OK").data(discoveryClient.getInstances(serviceId)).build();
	}
	
}
