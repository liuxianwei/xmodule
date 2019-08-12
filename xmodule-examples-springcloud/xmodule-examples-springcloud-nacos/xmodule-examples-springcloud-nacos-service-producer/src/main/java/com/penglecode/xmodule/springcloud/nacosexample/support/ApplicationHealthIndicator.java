package com.penglecode.xmodule.springcloud.nacosexample.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import com.penglecode.xmodule.common.boot.actuator.ControllableHealthIndicator;

/**
 * spring-actuactor的服务监控检测指示器
 * 
 * @author 	pengpeng
 * @date	2018年9月6日 下午1:32:32
 */
@Component("applicationHealthIndicator")
public class ApplicationHealthIndicator extends ControllableHealthIndicator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationHealthIndicator.class);

	@Override
	protected void checkHealth(Builder builder) {
		builder.up();
		LOGGER.info(">>> 检测应用健康状况：{}", builder.build());
	}
	
}
