package com.penglecode.xmodule.common.consts;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.penglecode.xmodule.common.support.MvvmWebAppConfigProperties;

/**
 * 应用的全局常量,其中包括：Spring上下文对象、Servlet上下文对象、应用的上下文路径、应用系统默认字符集、默认Locale、默认日期格式等常量
 * 
 * @author	  	pengpeng
 * @date	  	2015年10月16日 上午11:02:27
 * @version  	1.0
 */
public abstract class ApplicationConstants extends SpringManagedConstants {

	/**
	 * Spring的ROOT上下文,由@{link org.springframework.web.context.ContextLoaderListener}加载出来的spring上下文
	 */
	public static final ApplicationContext APPLICATION_CONTEXT = valueOf(null);
	
	/**
	 * Spring的MVC上下文,由@{link org.springframework.web.servlet.DispatcherServlet}加载出来的Spring MVC上下文
	 */
	public static final ApplicationContext WEB_APPLICATION_CONTEXT = valueOf(null);
	
	/**
	 * 应用中的Servlet上下文
	 */
	public static final ServletContext SERVLET_CONTEXT = valueOf(null);
	
	/**
	 * Spring的资源解析器
	 */
	public static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = valueOf(new PathMatchingResourcePatternResolver());
	
	/**
	 * 应用的上下文路径, e.g. /myapp
	 */
	public static final String CONTEXT_PATH = valueOf(null);
	
	/**
	 * 应用的上下文的实际路径, e.g. D:/Program Files/apache-tomcat-7.0.54-/webapps/myapp
	 */
	public static final String CONTEXT_REAL_PATH = valueOf(null);
	
	/**
	 * 全局应用配置(包括应用的name, url, contextpath等)
	 */
	public static final MvvmWebAppConfigProperties GLOBAL_APP_CONFIG = valueOf(null);
	
	/**
	 * 全局文件服务器的URL
	 */
	public static final String GLOBAL_FILE_SERVER_URL = valueOf("global.fileserver.url", null);

	/**
	 * 全局文件服务器的根路径
	 */
	public static final String GLOBAL_FILE_SERVER_ROOT = valueOf("global.fileserver.root", null);
	
	/**
	 * 默认的应用ID
	 */
	public static final Long DEFAULT_APPLICATION_ID = 1L;
	
	/**
	 * 当前应用的appId
	 */
	public static final Long CURRENT_APPLICATION_ID = valueOf("application.current.appId", Long.class, null);
	
	/**
	 * 统一用户权限管理系统UI服务的URL
	 */
	public static final String UPMS_WEB_SERVER_URL = valueOf("upms.webserver.url", null);
	
	/**
	 * 统一用户权限管理系统API服务的URL
	 */
	public static final String UPMS_API_SERVER_URL = valueOf("upms.apiserver.url", null);
	
	/**
	 * 统一用户权限管理系统UI服务的CONTEXT_PATH
	 */
	public static final String UPMS_WEB_SERVER_CONTEXT_PATH = valueOf("upms.webserver.contextpath", "/sbprojects-upms-web");
	
}
