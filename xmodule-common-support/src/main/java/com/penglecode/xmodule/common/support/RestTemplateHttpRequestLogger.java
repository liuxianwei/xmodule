package com.penglecode.xmodule.common.support;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.util.ArrayUtils;

/**
 * RestTemplate请求日志拦截器
 * 
 * @author 	pengpeng
 * @date	2019年1月17日 下午5:06:39
 */
public class RestTemplateHttpRequestLogger implements ClientHttpRequestInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateHttpRequestLogger.class);
	
	private String charset = GlobalConstants.DEFAULT_CHARSET;
	
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		long logTime = System.currentTimeMillis();
		String requestUrl = request.getURI().toString();
		MediaType requestContentType = request.getHeaders().getContentType();
		LOGGER.info("【{}】>>> 记录HTTP请求日志: requestUrl = {}", logTime, requestUrl);
		if(!ArrayUtils.isEmpty(body) && requestContentType != null) {
			if(requestContentType.includes(MediaType.APPLICATION_JSON) && requestContentType.includes(MediaType.APPLICATION_XML)) {
				String requestBody = new String(body, charset);
				LOGGER.info("【{}】>>> 记录HTTP请求日志: requestBody = {}", logTime, requestBody);
			}
		}
		ClientHttpResponse response = execution.execute(request, body);
		String responseBody = IOUtils.toString(response.getBody(), charset);
		LOGGER.info("【{}】<<< 记录HTTP请求日志: responseCode = {}, responseBody = {}", logTime, response.getRawStatusCode(), responseBody);
		return response;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}
