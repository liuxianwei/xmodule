package com.penglecode.xmodule.common.util;

import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;

public class WebUtils {

	/**
	 * 从请求header中提取参数
	 * @param request
	 * @param paramName
	 * @return
	 */
	public static String getHeaderParam(ServletRequest request, String paramName) {
		HttpServletRequest req = (HttpServletRequest) request;
		String paramValue = req.getHeader(paramName);
		return StringUtils.trim(paramValue);
	}
	
	/**
	 * 获取所有请求头
	 * @param request
	 * @return
	 */
	public static HttpHeaders getHeaders(ServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		HttpServletRequest req = (HttpServletRequest) request;
		Enumeration<String> headerNames = req.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			Enumeration<String> values = req.getHeaders(name);
			headers.put(name, Collections.list(values));
		}
		return headers;
	}
	
	/**
	 * 获取请求的域名URL
	 * 例如请求URL为：http://127.0.0.1:8080/flexedgex-upms-web/index.html，则返回：http://127.0.0.1:8080
	 * @param request
	 * @return
	 */
	public static String getHttpDomainUrl(ServletRequest request) {
		String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
		StringBuilder requestUrl = new StringBuilder(scheme);
        requestUrl.append("://");
        requestUrl.append(host);//5
        //6
        if("http".equalsIgnoreCase(scheme) && port != 80) {
            requestUrl.append(":").append(String.valueOf(port));
        } else if("https".equalsIgnoreCase(scheme) && port != 443) {
            requestUrl.append(":").append(String.valueOf(port));
        }
        return requestUrl.toString();
	}
	
}
