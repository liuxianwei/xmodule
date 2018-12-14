package com.penglecode.xmodule.myexample.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.Application;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

/**
 * Eureka的DiscoveryClient Controller
 * 
 * @author 	pengpeng
 * @date	2018年9月19日 上午11:39:20
 */
@RestController
@RequestMapping("/api/eureka/discoveryclient")
public class EurekaDiscoveryClientController extends HttpAPIResourceSupport {
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	/**
	 * 获取指定名称的应用信息
	 * @param appName
	 * @return
	 */
	@RequestMapping(value="/application/{appName}", method=GET, produces=APPLICATION_JSON)
	public Object getSpecifiedApplication(@PathVariable("appName") String appName) {
		if("current".equals(appName)) {
			appName = getEnvironment().getProperty("spring.application.name");
		}
		Application application = discoveryClient.getApplication(appName);
		return Result.success().data(application).build();
	}
	
	/**
	 * 获取指定应用下的实例列表
	 * @param appName
	 * @return
	 */
	@RequestMapping(value="/application/{appName}/instances", method=GET, produces=APPLICATION_JSON)
	public Object getSpecifiedApplicationInstances(@PathVariable("appName") String appName) {
		if("current".equals(appName)) {
			appName = getEnvironment().getProperty("spring.application.name");
		}
		Application application = discoveryClient.getApplication(appName);
		return Result.success().data(application.getInstances()).build();
	}
	
}
