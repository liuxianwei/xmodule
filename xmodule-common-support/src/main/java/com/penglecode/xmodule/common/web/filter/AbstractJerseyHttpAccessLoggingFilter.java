package com.penglecode.xmodule.common.web.filter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.container.ResourceInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.jersey.filter.HttpAccessLoggingResourcesHolder;
import com.penglecode.xmodule.common.web.support.MvcResourceMethodMapping;

public abstract class AbstractJerseyHttpAccessLoggingFilter extends AbstractHttpAccessLoggingFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSpringMvcHttpAccessLoggingFilter.class);
	
	@Override
	protected void initAllMvcResourceMethodMappings() {
		List<ResourceInfo> allHttpAccessLoggingResources = HttpAccessLoggingResourcesHolder.getAllHttpAccessLoggingResources();
		if(!CollectionUtils.isEmpty(allHttpAccessLoggingResources)) {
			List<MvcResourceMethodMapping> allMvcResourceMethodMappings = new ArrayList<MvcResourceMethodMapping>();
			for(ResourceInfo resourceInfo : allHttpAccessLoggingResources) {
				Method resourceMethod = resourceInfo.getResourceMethod();
				Class<?> resourceClass = resourceInfo.getResourceClass();
				Path classPathMapping = AnnotationUtils.findAnnotation(resourceClass, Path.class);
				Path methodPathMapping = AnnotationUtils.findAnnotation(resourceMethod, Path.class);
				HttpMethod httpMethodMapping = AnnotationUtils.findAnnotation(resourceMethod, HttpMethod.class);
				MvcResourceMethodMapping mvcResourceMethodMapping = resolveMvcResourceMethodMapping(resourceClass, resourceMethod, classPathMapping, methodPathMapping, httpMethodMapping);
				if(mvcResourceMethodMapping != null) {
					allMvcResourceMethodMappings.add(mvcResourceMethodMapping);
					LOGGER.info("Add {}", mvcResourceMethodMapping);
				}
			}
			setAllMvcResourceMethodMappings(allMvcResourceMethodMappings);
		}
	}
	
	protected MvcResourceMethodMapping resolveMvcResourceMethodMapping(Class<?> resourceClass, Method resourceMethod, Path classPathMapping, Path methodPathMapping, HttpMethod httpMethodMapping) {
		String backupMethod = "GET";
		String mainMethod = null;
		String uriPrefix = "", uriSuffix = "";
		if(classPathMapping != null) {
			uriPrefix = classPathMapping.value();
		}
		
		if(methodPathMapping != null) {
			uriSuffix = methodPathMapping.value();
		}
		
		String restUri = uriPrefix;
		if(!uriPrefix.endsWith("/") && !uriSuffix.startsWith("/")) {
			restUri += "/";
		}
		restUri += uriSuffix;
		if(!restUri.startsWith("/")) {
			restUri = "/" + restUri;
		}
		
		String resourceUriPattern = StringUtils.stripEnd(restUri, "/");
		if(httpMethodMapping != null) {
			mainMethod = httpMethodMapping.value();
		}
		mainMethod = StringUtils.defaultIfEmpty(mainMethod, backupMethod);
		return new MvcResourceMethodMapping(resourceClass, resourceMethod, Arrays.asList(mainMethod), Arrays.asList(resourceUriPattern));
	}
	
}
