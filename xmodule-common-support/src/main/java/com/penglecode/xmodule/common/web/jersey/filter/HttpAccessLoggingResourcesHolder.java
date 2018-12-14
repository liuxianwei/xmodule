package com.penglecode.xmodule.common.web.jersey.filter;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

import com.penglecode.xmodule.common.web.support.HttpAccessLogging;

public class HttpAccessLoggingResourcesHolder implements DynamicFeature {

	private static final List<ResourceInfo> allHttpAccessLoggingResources = new ArrayList<ResourceInfo>();

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if (resourceInfo.getResourceMethod().isAnnotationPresent(HttpAccessLogging.class)) {
        	allHttpAccessLoggingResources.add(resourceInfo);
        }
	}

	public static List<ResourceInfo> getAllHttpAccessLoggingResources() {
		return allHttpAccessLoggingResources;
	}
	
}
