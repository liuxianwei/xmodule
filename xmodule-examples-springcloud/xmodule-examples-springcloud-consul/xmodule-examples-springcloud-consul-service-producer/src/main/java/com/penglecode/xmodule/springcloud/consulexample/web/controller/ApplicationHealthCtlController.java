package com.penglecode.xmodule.springcloud.consulexample.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.boot.actuator.ControllableHealthIndicator;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

/**
 * 应用的健康控制
 * 即通过健康检测来控制应用的强制上下线
 * 
 * @author 	pengpeng
 * @date	2019年8月1日 下午4:53:30
 */
@RestController
public class ApplicationHealthCtlController extends HttpAPIResourceSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationHealthCtlController.class);
	
	@Autowired
	private ControllableHealthIndicator controllableHealthIndicator;
	
	@GetMapping(value="/api/app/onffline")
	public Result<Object> forceAppOffline() {
		LOGGER.info(">>> 强制应用下线...");
		controllableHealthIndicator.setForceAppOffline(Boolean.TRUE);
		return Result.success().message("OK").build();
	}
	
}
