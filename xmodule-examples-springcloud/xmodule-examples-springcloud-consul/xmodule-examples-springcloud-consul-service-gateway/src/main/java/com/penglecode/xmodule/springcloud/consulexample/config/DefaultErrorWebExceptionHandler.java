package com.penglecode.xmodule.springcloud.consulexample.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpLogging;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.penglecode.xmodule.common.util.StringUtils;

import reactor.core.publisher.Mono;

public class DefaultErrorWebExceptionHandler extends org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler {

	protected static final Log logger = HttpLogging
			.forLogName(DefaultErrorWebExceptionHandler.class);
	
	public DefaultErrorWebExceptionHandler(ErrorAttributes errorAttributes,
			ResourceProperties resourceProperties, ErrorProperties errorProperties,
			ApplicationContext applicationContext) {
		super(errorAttributes, resourceProperties, errorProperties, applicationContext);
	}

	protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		boolean includeStackTrace = isIncludeStackTrace(request, MediaType.ALL);
		Map<String, Object> errorAttrs = getErrorAttributes(request, includeStackTrace);
		errorAttrs = processErrorAttributes(request, includeStackTrace, errorAttrs);
		HttpStatus errorStatus = getHttpStatus(errorAttrs);
		errorAttrs.remove("status");
		return ServerResponse.status(errorStatus.value())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(BodyInserters.fromObject(errorAttrs))
				.doOnNext((resp) -> logError(request, errorStatus));
	}
	
	protected Map<String, Object> processErrorAttributes(ServerRequest request, boolean includeStackTrace, Map<String, Object> errorAttrs) {
		HttpStatus errorStatus = getHttpStatus(errorAttrs);
		Map<String, Object> originalAttrs = getErrorAttributes(request, includeStackTrace);
		Map<String, Object> newAttrs = new LinkedHashMap<String, Object>();
		newAttrs.put("success", false);
		
		String message = (String) originalAttrs.get("message");
		if(!StringUtils.isEmpty(message)) {
			if(message.contains("Connection refused:")) {
				errorStatus = HttpStatus.SERVICE_UNAVAILABLE;
			}
		}
		message = StringUtils.defaultIfEmpty(message, errorStatus.getReasonPhrase());
		if(!message.contains("请求失败:")) {
			message = String.format("请求失败: %s", message);
		}
		
		newAttrs.put("code", String.valueOf(errorStatus.value()));
		newAttrs.put("message", message);
		newAttrs.put("data", null);
		newAttrs.put("status", errorStatus.value());
		return newAttrs;
	}

	@Override
	protected void logError(ServerRequest request, HttpStatus errorStatus) {
		Throwable ex = getError(request);
		if(ex != null) {
			logger.error(ex.getMessage(), ex);
		}
	}
	
}
