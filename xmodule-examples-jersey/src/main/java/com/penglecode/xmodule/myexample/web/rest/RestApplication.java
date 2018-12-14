package com.penglecode.xmodule.myexample.web.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.penglecode.xmodule.common.web.jersey.filter.HttpAccessLoggingResourcesHolder;
import com.penglecode.xmodule.common.web.jersey.handler.DefaultRestExceptionHandler;
import com.penglecode.xmodule.common.web.jersey.jackson.JacksonObjectMapperProvider;

@Component
public class RestApplication extends ResourceConfig {

	public RestApplication() {
		super();
		//全局异常处理handler
		register(JacksonObjectMapperProvider.class);
		register(JacksonFeature.class);
		register(DefaultRestExceptionHandler.class);
		register(HttpAccessLoggingResourcesHolder.class);
		
		register(Example1Rest.class);
		register(Example2Rest.class);
	}
	
}
