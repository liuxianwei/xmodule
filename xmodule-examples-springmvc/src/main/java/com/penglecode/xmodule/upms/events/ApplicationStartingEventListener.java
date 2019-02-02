package com.penglecode.xmodule.upms.events;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

import com.penglecode.xmodule.common.listener.SpringEventListener;

@SpringEventListener
public class ApplicationStartingEventListener implements ApplicationListener<ApplicationStartingEvent> {

	@Override
	public void onApplicationEvent(ApplicationStartingEvent event) {
		System.out.println(">>> ApplicationStartingEvent = " + event);
	}

}
