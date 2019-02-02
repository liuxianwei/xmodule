package com.penglecode.xmodule.upms.events;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;

import com.penglecode.xmodule.common.listener.SpringEventListener;

@SpringEventListener
public class ApplicationPreparedEventListener implements ApplicationListener<ApplicationPreparedEvent> {

	@Override
	public void onApplicationEvent(ApplicationPreparedEvent event) {
		System.out.println(">>> ApplicationPreparedEvent = " + event);
	}

}
