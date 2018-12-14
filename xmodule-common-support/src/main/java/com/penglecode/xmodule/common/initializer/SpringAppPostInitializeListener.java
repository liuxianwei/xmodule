package com.penglecode.xmodule.common.initializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.web.context.WebApplicationContext;

import com.penglecode.xmodule.common.util.CollectionUtils;
/**
 * Spring应用程序后置初始化监听器
 * 
 * @author 	pengpeng
 * @date	2018年9月3日 下午3:08:51
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SpringAppPostInitializeListener implements ApplicationContextAware, InitializingBean, DisposableBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringAppPostInitializeListener.class);
	
	private ConfigurableApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if(applicationContext instanceof ConfigurableApplicationContext) {
			this.applicationContext = (ConfigurableApplicationContext) applicationContext;
		} else {
			LOGGER.error("Expecting a ConfigurableApplicationContext to be ware during startup, but actually is : {}, spring application initializer will be ignored!", applicationContext);
		}
	}

	protected ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void afterPropertiesSet() throws Exception {
		String mode = applicationContext instanceof WebApplicationContext ? "Java Servlet Mode" : "Java Application Mode";
		LOGGER.info(String.format("Spring application post initializing start in %s", mode));
		LOGGER.info("Spring application post initializing begin ...");
        Map<String, SpringAppPostInitializer> initializerBeans = applicationContext.getBeansOfType(SpringAppPostInitializer.class);
        if(!CollectionUtils.isEmpty(initializerBeans)) {
        	List<SpringAppPostInitializer> applicationInitializers = new ArrayList<SpringAppPostInitializer>(initializerBeans.values());
            if (!CollectionUtils.isEmpty(applicationInitializers)) {
            	OrderComparator.sort(applicationInitializers);
                for (SpringAppPostInitializer applicationInitializer : applicationInitializers) {
                	applicationInitializer.initialize(applicationContext);
                }
            }
        }
        LOGGER.info("Spring application post initializing end ...");
	}

	public void destroy() throws Exception {
		String mode = applicationContext instanceof WebApplicationContext ? "Java Servlet Mode" : "Java Application Mode";
		LOGGER.info(String.format("Spring application post destroyed in %s", mode));
	}
	
}
