package com.penglecode.xmodule.upms.events;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.penglecode.xmodule.common.listener.SpringEventListener;

@SpringEventListener
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println(">>> ContextRefreshedEvent = " + event);
	}

}
