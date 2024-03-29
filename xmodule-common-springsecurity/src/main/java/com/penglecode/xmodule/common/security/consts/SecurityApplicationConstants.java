package com.penglecode.xmodule.common.security.consts;

/**
 * Spring-Security应用程序常量
 * 
 * @author 	pengpeng
 * @date	2018年10月26日 下午1:02:36
 */
public class SecurityApplicationConstants {

	/**
	 * 保存在request属性中的SavedRequestUrl的key
	 */
	public static final String SAVED_REQUEST_URL_KEY = "springSecuritySavedRequestUrl";
	
	/**
	 * 客户端检测可访问性的实际访问url的参数名
	 */
	public static final String CLIENT_ACCESS_URL_PARAM_NAME = "url";
	
	/**
	 * 客户端访问权限判断请求的URL
	 */
	public static final String SECURITY_ACCESS_DECIDE_REQUEST_URL = "/security/accessdecide";
	
	/**
	 * 客户端访问权限判断请求的属性KEY
	 */
	public static final String SECURITY_ACCESS_DECIDE_REQUEST_KEY = "securityAccessDecideRequest";
	
	/**
	 * 当前客户端访问的应用ID的属性KEY
	 */
	public static final String CURRENT_CLIENT_ACCESS_APP_ID_KEY = "currentClientAccessAppId";
	
}
