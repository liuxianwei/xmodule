package com.penglecode.xmodule.upms.events;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent>, ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println(">>> ApplicationReadyEvent1 = " + event);
		if(event.getApplicationContext().equals(applicationContext)) {
			System.out.println(">>> ApplicationReadyEvent2 = " + event);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
