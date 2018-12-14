package com.penglecode.xmodule.common.web.shiro.consts;

import java.util.HashMap;
import java.util.Map;

import com.penglecode.xmodule.common.consts.SpringManagedConstants;
import com.penglecode.xmodule.upms.model.UpmsApp;

/**
 * Shiro应用的常量
 * 
 * @author 	pengpeng
 * @date	2018年6月27日 下午2:50:04
 */
public class ShiroApplicationConstants extends SpringManagedConstants {

	/**
	 * 所有应用信息列表映射
	 * Map[appWebContextPath, UpmsApp]
	 * @see #UpmsAppMappingConstManager
	 */
	public static final Map<String,UpmsApp> ALL_UPMS_APP_MAPPING = new HashMap<String,UpmsApp>();
	
	/**
	 * ClientUrlAccessibleServlet的url-pattern
	 */
	public static final String SHIRO_CLIENT_URL_ACCESSIBLE_SERVLET_URL_PATTERN = "/shiro/accessible";
	
	public static final String SHIRO_CLIENT_ACCESS_URL = "shiroClientAccessUrl";
	
	public static final String SHIRO_CLIENT_ACCESS_EXCEPTION = "shiroClientAccessException";
	
	/**
	 * 登录成功后重定向页面的request属性key
	 */
	public static final String AUTHC_FALLBACK_URL_KEY = "authcFallbackUrl";
	
	/**
	 * 登录用户对象的CustomUsernamePasswordToken属性key
	 */
	public static final String LOGIN_USER_OBJECT_KEY = "loginUser";
	
	/**
	 * 未授权的URL在request属性key
	 */
	public static final String UNAUTHORIZED_REQUEST_URLKEY = "unauthorizedRequestUrl";
	
	/**
	 * 未认证的URL在request属性key
	 */
	public static final String UNAUTHENTICATED_REQUEST_URLKEY = "unauthenticatedRequestUrl";
	
	/**
	 * 当前请求所指向的应用
	 */
	public static final String CURRENT_CLIENT_ACCESS_APPLICATION_ID = "currentClientAccessAppId";
	
}
