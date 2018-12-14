package com.penglecode.xmodule.myexample.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

/**
 * SpringCloud的DiscoveryClient Controller
 * 
 * @author 	pengpeng
 * @date	2018年9月19日 上午11:39:20
 */
@RestController
@RequestMapping("/api/spring/discoveryclient")
public class SpringDiscoveryClientController extends HttpAPIResourceSupport {
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	/**
	 * 获取所有应用服务列表
	 * @return
	 */
	@RequestMapping(value="/applications", method=GET, produces=APPLICATION_JSON)
	public Object getServices() {
		return Result.success().data(discoveryClient.getServices()).build();
	}
	
	@RequestMapping(value="/application/{appName}/instances", method=GET, produces=APPLICATION_JSON)
	public Object getSpecifiedApplicationInstances(@PathVariable("appName") String appName) {
		return Result.success().data(discoveryClient.getInstances(appName)).build();
	}
	
}
