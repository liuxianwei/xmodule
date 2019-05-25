package com.penglecode.xmodule.springcloud.busexample.bus.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.event.AckRemoteApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import com.penglecode.xmodule.common.util.JsonUtils;

public class BusExampleAckEventListener implements ApplicationListener<AckRemoteApplicationEvent> {

	@Autowired
	private Environment environment;
	
	@Override
	public void onApplicationEvent(AckRemoteApplicationEvent event) {
		System.out.println(String.format("【ACK-Event】>>> ServiceId [%s] listeners on, event = %s", environment.getProperty("spring.cloud.bus.id"), JsonUtils.object2Json(event)));
	}

}
