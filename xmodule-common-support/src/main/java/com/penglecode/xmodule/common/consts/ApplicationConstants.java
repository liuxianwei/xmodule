package com.penglecode.xmodule.common.consts;

import static com.penglecode.xmodule.common.consts.Constant.defaultOf;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.penglecode.xmodule.common.support.NamedThreadFactory;

/**
 * 应用的全局常量,其中包括：Spring上下文对象、Servlet上下文对象、应用的上下文路径、应用系统默认字符集、默认Locale、默认日期格式等常量
 * 
 * @author pengpeng
 * @date 2015年10月16日 上午11:02:27
 * @version 1.0
 */
public abstract class ApplicationConstants {

	/**
	 * Spring的ROOT上下文,由@{link
	 * org.springframework.web.context.ContextLoaderListener}加载出来的spring上下文
	 */
	public static final ApplicationContext APPLICATION_CONTEXT = defaultOf(null);
	
	/**
	 * Spring的环境变量上下文
	 */
	public static final Environment ENVIRONMENT = defaultOf(null);

	/**
	 * Spring管理的资源文件访问器
	 */
	public static final MessageSourceAccessor MESSAGE_SOURCE_ACCESSOR = defaultOf(null);

	/**
	 * 应用中的Servlet上下文
	 */
	public static final ServletContext SERVLET_CONTEXT = defaultOf(null);

	/**
	 * Spring的资源解析器
	 */
	public static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = defaultOf(new PathMatchingResourcePatternResolver());

	/**
	 * 应用默认的线程池
	 */
	public static final Executor DEFAULT_EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4, new NamedThreadFactory("DEFAULT-EXECUTOR-"));
	
	/**
	 * 应用的上下文路径, e.g. /myapp
	 */
	public static final String CONTEXT_PATH = defaultOf(null);

	/**
	 * 应用的上下文的实际路径, e.g. D:/Program Files/apache-tomcat-7.0.54-/webapps/myapp
	 */
	public static final String CONTEXT_REAL_PATH = defaultOf(null);

}
