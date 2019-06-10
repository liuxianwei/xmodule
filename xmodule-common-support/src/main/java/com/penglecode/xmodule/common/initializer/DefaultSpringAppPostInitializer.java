package com.penglecode.xmodule.common.initializer;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.util.FileUtils;

/**
 * Spring应用启动完成时的初始化程序
 * 
 * @author 	pengpeng
 * @date	2018年2月5日 下午12:40:31
 */
public class DefaultSpringAppPostInitializer extends SpringAppPostInitializer<ConfigurableApplicationContext> implements MessageSourceAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpringAppPostInitializer.class);

	public void doInitialize(ConfigurableApplicationContext applicationContext) {
		LOGGER.info(">>> Spring应用启动后置初始化程序! applicationContext = {}", applicationContext);
		if(applicationContext instanceof WebApplicationContext){ //容器环境下运行Spring ApplicationContext上下文
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
	
	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		setFinalFieldValue(ApplicationConstants.class, "MESSAGE_SOURCE_ACCESSOR", new MessageSourceAccessor(messageSource, GlobalConstants.DEFAULT_LOCALE));
	}

}
