package com.penglecode.xmodule.common.initializer;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.consts.SpringEnvConstantPool;
import com.penglecode.xmodule.common.util.FileUtils;
import com.penglecode.xmodule.common.util.SpringUtils;

/**
 * Spring应用启动完成时的初始化程序
 * 
 * @author 	pengpeng
 * @date	2018年2月5日 下午12:40:31
 */
@Component
public class DefaultSpringAppPostInitializer extends SpringAppInitializeSupport implements ApplicationContextAware, EnvironmentAware, MessageSourceAware, ServletContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpringAppPostInitializer.class);

	/**
	 * Spring应用的上下文
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		LOGGER.info(">>> 初始化Spring应用的应用上下文! applicationContext = {}", applicationContext);
		SpringUtils.setApplicationContext(applicationContext);
		setFinalFieldValue(ApplicationConstants.class, "APPLICATION_CONTEXT", applicationContext);
	}

	/**
	 * Spring应用的环境变量初始化
	 */
	@Override
	public void setEnvironment(Environment environment) {
		LOGGER.info(">>> 初始化Spring应用的环境变量! environment = {}", environment);
		SpringEnvConstantPool.setEnvironment(environment);
		setFinalFieldValue(ApplicationConstants.class, "ENVIRONMENT", environment);
	}
	
	/**
	 * Spring应用的全局messageSource
	 */
	@Override
	public void setMessageSource(MessageSource messageSource) {
		LOGGER.info(">>> 初始化Spring应用的全局国际化资源文件! messageSource = {}", messageSource);
		setFinalFieldValue(ApplicationConstants.class, "MESSAGE_SOURCE_ACCESSOR", new MessageSourceAccessor(messageSource, GlobalConstants.DEFAULT_LOCALE));
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		LOGGER.info(">>> 初始化Servlet环境的系统常量! servletContext = {}", servletContext);
		setFinalFieldValue(ApplicationConstants.class, "SERVLET_CONTEXT", servletContext);
		setFinalFieldValue(ApplicationConstants.class, "CONTEXT_PATH", FileUtils.formatFilePath(servletContext.getContextPath()));
		setFinalFieldValue(ApplicationConstants.class, "CONTEXT_REAL_PATH", FileUtils.formatFilePath(servletContext.getRealPath("/")));
		setFinalFieldValue(ApplicationConstants.class, "RESOURCE_PATTERN_RESOLVER", new ServletContextResourcePatternResolver(servletContext));
	}

}
