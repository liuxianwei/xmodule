package com.penglecode.xmodule.upms.events;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import com.penglecode.xmodule.common.listener.SpringEventListener;

@SpringEventListener
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println(">>> ApplicationReadyEvent = " + event);
	}

}
