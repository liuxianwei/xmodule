package com.penglecode.xmodule.upms.events;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class WebServerInitializedEventListener implements ApplicationListener<WebServerInitializedEvent> {

	@Override
	public void onApplicationEvent(WebServerInitializedEvent event) {
		System.out.println(">>> WebServerInitializedEvent = " + event);
	}

}
