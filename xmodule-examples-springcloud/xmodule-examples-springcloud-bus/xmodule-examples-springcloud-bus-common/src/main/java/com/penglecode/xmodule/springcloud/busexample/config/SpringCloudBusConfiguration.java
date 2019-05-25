package com.penglecode.xmodule.springcloud.busexample.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.bus.ConditionalOnBusEnabled;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.BasePackage;
import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.springcloud.busexample.bus.listener.AppInfoUpdatedRemoteBusEventListener;
import com.penglecode.xmodule.springcloud.busexample.bus.listener.BusExampleAckEventListener;

@Configuration
@ConditionalOnBusEnabled
@RemoteApplicationEventScan(basePackageClasses=BasePackage.class)
public class SpringCloudBusConfiguration extends AbstractSpringConfiguration {

	public final static String SPRING_CLOUD_BUS_EXAMPLE_ROLE = "spring.cloud.bus.example.role";
	
	@Bean
	public BusExampleAckEventListener busExampleAckEventListener() {
		return new BusExampleAckEventListener();
	}
	
	@Bean
	@ConditionalOnProperty(name=SPRING_CLOUD_BUS_EXAMPLE_ROLE, havingValue="consumer")
	public AppInfoUpdatedRemoteBusEventListener appInfoUpdatedRemoteBusEventListener() {
		return new AppInfoUpdatedRemoteBusEventListener();
	}
	
}
