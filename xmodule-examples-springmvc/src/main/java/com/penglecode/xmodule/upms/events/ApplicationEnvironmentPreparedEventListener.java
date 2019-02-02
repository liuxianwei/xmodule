package com.penglecode.xmodule.upms.events;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

import com.penglecode.xmodule.common.listener.SpringEventListener;

@SpringEventListener
public class ApplicationEnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		System.out.println(">>> ApplicationEnvironmentPreparedEvent = " + event);
	}

}
