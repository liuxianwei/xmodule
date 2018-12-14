package com.penglecode.xmodule.common.boot.config;

import org.springframework.core.Ordered;

public class FilterRegistrationOrder {

	/**
	 * spring自带的CharsetEncodingFilter的注册顺序
	 */
	public static final int ORDER_CHARSET_ENCODING_FILTER = Ordered.HIGHEST_PRECEDENCE;
	
	/**
	 * 客户端请求预处理过滤器的注册顺序
	 */
	public static final int ORDER_CLIENT_URL_ACCESS_PREPARE_FILTER = Ordered.HIGHEST_PRECEDENCE + 10;
	
	/**
	 * 客户端访问权限判断请求过滤器
	 */
	public static final int ORDER_SECURITY_ACCESS_DECIDE_REQUEST_FILTER = Ordered.HIGHEST_PRECEDENCE + 10;
	
	/**
	 * ShiroFilter的注册顺序
	 */
	public static final int ORDER_SHIRO_FILTER = Ordered.HIGHEST_PRECEDENCE + 20;
	
	/**
	 * Http访问日志记录过滤器的注册顺序
	 */
	public static final int ORDER_HTTP_ACCESS_LOGGING_FILTER = Ordered.HIGHEST_PRECEDENCE + 30;
	
	/**
	 * Jersey Restful过滤器的注册顺序
	 */
	public static final int ORDER_JERSEY_FILTER = Ordered.HIGHEST_PRECEDENCE + 40;
	
}
