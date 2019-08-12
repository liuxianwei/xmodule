package com.penglecode.xmodule.common.web.springmvc.handler;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.support.ExceptionDescriptor;
import com.penglecode.xmodule.common.support.ExceptionDescriptorResolver;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.ArrayUtils;
import com.penglecode.xmodule.common.util.ExceptionUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
/**
 * 默认的MVC异常处理器,按请求是异步请求还是同步请求分别做不同的处理
 * 
 * @author 	pengpeng
 * @date	2019年2月18日 下午12:10:08
 */
public class DefaultMvcHandlerExceptionResolver extends AbstractMvcHandlerExceptionResolver {

	/**
	 * 将HttpServletResponse的httpStatus的值设为Result.code ?
	 */
	private boolean applyResponseStatusByResultCode = true;
	
	protected ModelAndView handle4AsyncRequest(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		ExceptionDescriptor ed = resolveExceptionDescriptor(request, response, handler, ex);
		
		if(applyResponseStatusByResultCode) {
			handleResponseStatus(response, ed);
		}
		
		Result<Object> result = ed.toResult();
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		jsonView.setExtractValueFromSingleKeyModel(true);
		ModelAndView mav = new ModelAndView(jsonView);
		mav.addObject(result);
		return mav;
	}
	
	protected ModelAndView handle4SyncRequest(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		ExceptionDescriptor ed = resolveExceptionDescriptor(request, response, handler, ex);
		
		handleResponseStatus(response, ed);
		
		Map<String,Object> exceptionMetadata = new HashMap<String,Object>();
		exceptionMetadata.put("message", ed.getMessage());
		exceptionMetadata.put("stackTrace", ExceptionUtils.getStackTrace(ed.getTarget()));
		exceptionMetadata.put("code", ed.getCode());
		ModelAndView mav = new ModelAndView(getDefaultExceptionView());
		mav.addObject("exceptionMetadata", exceptionMetadata);
		return mav;
	}
	
	protected void handleResponseStatus(HttpServletResponse response, ExceptionDescriptor ed) {
		HttpStatus httpStatus = null;
		Integer statusCode = null;
		try {
			statusCode = Integer.valueOf(ed.getCode());
		} catch (Exception e) {
		}
		if(statusCode != null) {
			httpStatus = HttpStatus.resolve(statusCode);
		}
		if(httpStatus != null) {
			response.setStatus(httpStatus.value());
		}
	}
	
	protected HttpStatus getHttpStatusByCode(Result<?> body) {
		Integer statusCode = null;
		try {
			statusCode = Integer.valueOf(body.getCode());
		} catch (NumberFormatException e) {
		}
		if(statusCode != null) {
			return HttpStatus.resolve(statusCode);
		}
		return null;
	}
	
	protected ExceptionDescriptor resolveExceptionDescriptor(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		String message = "";
		int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
		if (ex instanceof HttpRequestMethodNotSupportedException) {
			HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
			statusCode = httpStatus.value();
			message = getMessageSourceAccessor().getMessage("message.httpstatus." + statusCode);
			message = StringUtils.defaultIfEmpty(message, httpStatus.getReasonPhrase());
			return new ExceptionDescriptor(ex, statusCode, message);
		} else if (ex instanceof MissingServletRequestParameterException) {
			HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
			statusCode = httpStatus.value();
			message = getMessageSourceAccessor().getMessage("message.httpstatus." + statusCode);
			message = StringUtils.defaultIfEmpty(message, httpStatus.getReasonPhrase());
			return new ExceptionDescriptor(ex, statusCode, message);
		} else if (ex instanceof RestClientResponseException) {
			RestClientResponseException restEx = (RestClientResponseException) ex;
			
			HttpHeaders headers = restEx.getResponseHeaders();
			byte[] responseBody = restEx.getResponseBodyAsByteArray();
			if(headers != null && !ArrayUtils.isEmpty(responseBody)) {
				MediaType contentType = headers.getContentType();
				if(contentType != null && contentType.includes(MediaType.APPLICATION_JSON)) {
					Charset charset = ObjectUtils.defaultIfNull(contentType.getCharset(), getDefaultCharset());
					String json = new String(responseBody, charset);
					if(JsonUtils.isJsonObject(json)) {
						JSONObject jsonObject = new JSONObject(json);
						if(!jsonObject.isNull("code") && !jsonObject.isNull("message")) {
							return new ExceptionDescriptor(ex, jsonObject.optInt("code"), jsonObject.optString("message"));
						}
					}
				}
			}
			
			HttpStatus httpStatus = HttpStatus.resolve(restEx.getRawStatusCode());
			httpStatus = ObjectUtils.defaultIfNull(httpStatus, HttpStatus.INTERNAL_SERVER_ERROR);
			statusCode = httpStatus.value();
			message = getMessageSourceAccessor().getMessage("message.httpstatus." + statusCode);
			message = StringUtils.defaultIfEmpty(message, httpStatus.getReasonPhrase());
			return new ExceptionDescriptor(ex, statusCode, message);
		}
		return ExceptionDescriptorResolver.resolveException(ex);
	}
	
	protected MessageSourceAccessor getMessageSourceAccessor() {
		return ApplicationConstants.MESSAGE_SOURCE_ACCESSOR;
	}

	protected boolean isApplyResponseStatusByResultCode() {
		return applyResponseStatusByResultCode;
	}

	public void setApplyResponseStatusByResultCode(boolean applyResponseStatusByResultCode) {
		this.applyResponseStatusByResultCode = applyResponseStatusByResultCode;
	}

}
