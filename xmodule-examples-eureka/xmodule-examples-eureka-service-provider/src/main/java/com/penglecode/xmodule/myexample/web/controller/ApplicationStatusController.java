package com.penglecode.xmodule.myexample.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.EurekaInstanceConfig;
import com.penglecode.xmodule.common.boot.actuator.ControllableHealthIndicator;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

/**
 * 应用Controller
 * 
 * @author 	pengpeng
 * @date	2018年9月19日 上午11:39:20
 */
@RestController
@RequestMapping("/api/application")
public class ApplicationStatusController extends HttpAPIResourceSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStatusController.class);
	
	@Autowired(required=false)
	private ControllableHealthIndicator controllableHealthIndicator;
	
	@Autowired
	private EurekaServiceRegistry eurekaServiceRegistry;
	
	@Autowired
	private EurekaRegistration eurekaRegistration;
	
	/**
	 * 通过健康监测来控制应用的上线/下线
	 * 
	 * 注意：eureka-server端不能立马感知状态的变化,需要在下一个healthcheck定时任务(时间周期：eureka.client.instanceInfoReplicationIntervalSeconds)到来之际才能感知
	 * 
	 * @return
	 */
	@RequestMapping(value="/health/up", method=GET, produces=APPLICATION_JSON)
	public Object healthUp() {
		String appName = getEnvironment().getProperty("spring.application.name");
		String instanceId = getEnvironment().getProperty("eureka.instance.instanceId");
		if(controllableHealthIndicator != null) {
			LOGGER.info(">>> 强制设置应用实例{}的health状态为UP", appName + "@" + instanceId);
			controllableHealthIndicator.setForceHealthDown(Boolean.FALSE);
			return Result.success().message("OK").data("UP").build();
		} else {
			return Result.success().message("Current HealthIndicator instance is unsupported!").build();
		}
	}
	
	/**
	 * 通过健康监测来控制应用的上线/下线
	 * 
	 * 注意：eureka-server端不能立马感知状态的变化,需要在下一个healthcheck定时任务(时间周期：eureka.client.instanceInfoReplicationIntervalSeconds)到来之际才能感知
	 * 
	 * @return
	 */
	@RequestMapping(value="/health/down", method=GET, produces=APPLICATION_JSON)
	public Object healthDown() {
		String appName = getEnvironment().getProperty("spring.application.name");
		String instanceId = getEnvironment().getProperty("eureka.instance.instanceId");
		if(controllableHealthIndicator != null) {
			LOGGER.info(">>> 强制设置应用实例{}的health状态为DOWN", appName + "@" + instanceId);
			controllableHealthIndicator.setForceHealthDown(Boolean.TRUE);
			return Result.success().message("OK").data("DOWN").build();
		} else {
			return Result.success().message("Current HealthIndicator instance is unsupported!").build();
		}
	}
	
	/**
	 * 通过健康监测来控制应用的上线/下线
	 * 
	 * 由于是通过eureka rest api主动更新eureka-server端的服务状态,所以eureka-server端的服务状态会实时变更，
	 * 但是client端的application.getInstances()获取到的列表却不是实时的，需要等到下一个注册服务抓取定时任务(时间周期：eureka.client.registryFetchIntervalSeconds)到来之际才能变化
	 * 
	 * @return
	 */
	@RequestMapping(value="/instance/down", method=GET, produces=APPLICATION_JSON)
	public Object instanceDown() {
		EurekaInstanceConfig instance = eurekaRegistration.getInstanceConfig();
		String appName = instance.getAppname();
		String instanceId = instance.getInstanceId();
		LOGGER.info(">>> 强制应用实例{}下线", appName + "@" + instanceId);
		eurekaServiceRegistry.setStatus(eurekaRegistration, "DOWN");
		return Result.success().message("OK").build();
	}
	
	/**
	 * 通过健康监测来控制应用的上线/下线
	 * 
	 * 由于是通过eureka rest api主动更新eureka-server端的服务状态,所以eureka-server端的服务状态会实时变更，
	 * 但是client端的application.getInstances()获取到的列表却不是实时的，需要等到下一个注册服务抓取定时任务(时间周期：eureka.client.registryFetchIntervalSeconds)到来之际才能变化
	 * 
	 * @return
	 */
	@RequestMapping(value="/instance/up", method=GET, produces=APPLICATION_JSON)
	public Object instanceUp() {
		EurekaInstanceConfig instance = eurekaRegistration.getInstanceConfig();
		String appName = instance.getAppname();
		String instanceId = instance.getInstanceId();
		LOGGER.info(">>> 强制应用实例{}上线", appName + "@" + instanceId);
		eurekaServiceRegistry.setStatus(eurekaRegistration, "UP");
		return Result.success().message("OK").build();
	}
	
}
