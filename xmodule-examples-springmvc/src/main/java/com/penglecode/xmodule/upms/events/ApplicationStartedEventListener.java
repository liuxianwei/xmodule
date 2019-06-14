package com.penglecode.xmodule.upms.events;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent>, ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		System.out.println(">>> ApplicationStartedEvent1 = " + event);
		if(event.getApplicationContext().equals(applicationContext)) {
			System.out.println(">>> ApplicationStartedEvent2 = " + event);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
