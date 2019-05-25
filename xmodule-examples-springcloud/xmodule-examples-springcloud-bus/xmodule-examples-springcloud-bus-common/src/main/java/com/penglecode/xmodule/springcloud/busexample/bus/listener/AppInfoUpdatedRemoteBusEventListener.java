package com.penglecode.xmodule.springcloud.busexample.bus.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.springcloud.busexample.bus.event.AppInfoUpdatedRemoteBusEvent;

public class AppInfoUpdatedRemoteBusEventListener implements ApplicationListener<AppInfoUpdatedRemoteBusEvent> {

	@Autowired
	private Environment environment;
	
	@Override
	public void onApplicationEvent(AppInfoUpdatedRemoteBusEvent event) {
		System.out.println(String.format("【AppInfo-Updated-Event】>>> ServiceId [%s] listeners on, event = %s", environment.getProperty("spring.cloud.bus.id"), JsonUtils.object2Json(event)));
	}

}
