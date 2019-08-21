package com.penglecode.xmodule.common.support;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.penglecode.xmodule.common.consts.GlobalConstants;

/**
 * Apache HttpClient4 请求/响应日志记录拦截器
 * 
 * @author 	pengpeng
 * @date	2019年8月17日 上午9:38:10
 */
public class HttpClientLoggingInterceptor implements HttpRequestInterceptor, HttpResponseInterceptor {

	private final Logger logger;
	
	public HttpClientLoggingInterceptor() {
		super();
		this.logger = LoggerFactory.getLogger(HttpClientLoggingInterceptor.class);
	}
	
	public HttpClientLoggingInterceptor(String loggerName) {
		this.logger = LoggerFactory.getLogger(loggerName);
	}

	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		getLogger().debug("==> Request URI       : {}", request.getRequestLine().getUri());
		getLogger().debug("==> Request Method    : {}", request.getRequestLine().getMethod());
		getLogger().debug("==> Request Headers   : {}", Arrays.asList(request.getAllHeaders()));
		getLogger().debug("==> Request Body      : {}", getRequestBody(request));
	}

	@Override
	public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
		getLogger().debug("<== Response Status   : {}", response.getStatusLine());
		getLogger().debug("<== Response Headers  : {}", Arrays.asList(response.getAllHeaders()));
		getLogger().debug("<== Response Body     : {}", getResponseBody(response));
	}
	
	protected String getRequestBody(HttpRequest request) throws IOException {
		if(request instanceof HttpEntityEnclosingRequest) {
			HttpEntityEnclosingRequest enclosingRequest = (HttpEntityEnclosingRequest) request;
			HttpEntity entity = enclosingRequest.getEntity();
			if(entity != null) { //rewrite request/response body for exception: Attempted read from closed stream.
				entity = new ByteArrayEntity(EntityUtils.toByteArray(entity));
				enclosingRequest.setEntity(entity);
			}
			return getEntityContentAsString(entity);
		}
		return null;
	}
	
	protected String getResponseBody(HttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();
		if(entity != null) { //rewrite request/response body for exception: Attempted read from closed stream.
			entity = new ByteArrayEntity(EntityUtils.toByteArray(entity));
			EntityUtils.updateEntity(response, entity);
		}
		return getEntityContentAsString(response.getEntity());
	}
	
	protected String getEntityContentAsString(HttpEntity entity) throws IOException {
		if(entity != null) {
			return EntityUtils.toString(entity, GlobalConstants.DEFAULT_CHARSET);
		}
		return "NO CONTENT";
	}

	protected Logger getLogger() {
		return logger;
	}

}
