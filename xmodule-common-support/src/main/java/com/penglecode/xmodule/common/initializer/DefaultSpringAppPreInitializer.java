package com.penglecode.xmodule.common.initializer;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.consts.SpringEnvConstantPool;
import com.penglecode.xmodule.common.util.FileUtils;
import com.penglecode.xmodule.common.util.SpringUtils;

/**
 * Spring应用启动之处初始化程序
 * 该类配置方式如下：
 * 1、 在web.xml中以contextInitializerClasses上下文参数配置
 * 		<context-param>
 * 			<param-name>contextInitializerClasses</param-name>
 * 			<param-value>xyz.SpringAppPreBootingInitializer</param-value>
 * 		</context-param>
 * 
 * 2、 在Springboot的application.properties中配置
 * 		context.initializer.classes=xyz.SpringAppPreBootingInitializer
 * 
 * @author 	pengpeng
 * @date	2018年2月5日 下午12:40:31
 */
public class DefaultSpringAppPreInitializer extends AbstractSpringAppInitializer<ConfigurableApplicationContext> implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpringAppPreInitializer.class);

	public void doInitialize(ConfigurableApplicationContext applicationContext) {
		ApplicationContext rootApplicationContext = SpringUtils.getRootApplicationContext(applicationContext);
		if(SpringUtils.getApplicationContext() == null) {
			LOGGER.info(">>> Spring 应用启动前置初始化程序! applicationContext = {}", applicationContext);
			SpringUtils.setApplicationContext(rootApplicationContext);
			SpringEnvConstantPool.setEnvironment(rootApplicationContext.getEnvironment());
			setFinalFieldValue(ApplicationConstants.class, "APPLICATION_CONTEXT", rootApplicationContext);
			if(applicationContext instanceof WebApplicationContext) { //容器环境下运行Spring ApplicationContext上下文
				setFinalFieldValue(ApplicationConstants.class, "WEB_APPLICATION_CONTEXT", applicationContext);
				ServletContext servletContext = ((WebApplicationContext) applicationContext).getServletContext();
				if(servletContext != null) {
					LOGGER.info(">>> 初始化Spring应用中依赖于Servlet环境的系统常量!");
					setFinalFieldValue(ApplicationConstants.class, "SERVLET_CONTEXT", servletContext);
					setFinalFieldValue(ApplicationConstants.class, "CONTEXT_PATH", FileUtils.formatFilePath(servletContext.getContextPath()));
					setFinalFieldValue(ApplicationConstants.class, "CONTEXT_REAL_PATH", FileUtils.formatFilePath(servletContext.getRealPath("/")));
					setFinalFieldValue(ApplicationConstants.class, "RESOURCE_PATTERN_RESOLVER", new ServletContextResourcePatternResolver(servletContext));
				}
			}
		}
	}

}
