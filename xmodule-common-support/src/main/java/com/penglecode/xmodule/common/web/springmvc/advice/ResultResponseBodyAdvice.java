package com.penglecode.xmodule.common.web.springmvc.advice;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.penglecode.xmodule.common.support.Result;

/**
 * 标准化输出，即指定Response的statusCode为Result.code
 * 
 * @author 	pengpeng
 * @date	2019年8月6日 下午2:36:54
 */
@ControllerAdvice
@ConditionalOnClass(ResponseBodyAdvice.class)
public class ResultResponseBodyAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		if(body instanceof Result) {
			applyHttpStatusByResultCode(request, response, (Result<?>) body);
		}
		return body;
	}
	
	protected void applyHttpStatusByResultCode(ServerHttpRequest request, ServerHttpResponse response, Result<?> body) {
		HttpStatus status = getHttpStatusFromResult(body);
		if(status != null) {
			response.setStatusCode(status);
		}
	}
	
	protected HttpStatus getHttpStatusFromResult(Result<?> body) {
		Integer statusCode = null;
		try {
			statusCode = Integer.valueOf(body.getCode());
		} catch (Exception e) {
		}
		if(statusCode != null) {
			return HttpStatus.resolve(statusCode);
		}
		return null;
	}

}
