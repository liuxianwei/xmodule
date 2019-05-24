package com.penglecode.xmodule.common.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;

import com.penglecode.xmodule.BasePackage;
import com.penglecode.xmodule.common.util.ClassScanningUtils;

/**
 * Spring事件监听器自动注册Listener，参照{@link #org.springframework.boot.context.config.DelegatingApplicationListener}来实现的，
 * 通过扫描注解@SpringEventListener
 * (注意：ApplicationStartingEvent是无法自动注册的，因为ApplicationStartingEvent只能使用SpringApplication.addListeners(..)来注册，别无他法！)
 * 
 * @author 	pengpeng
 * @date	2019年1月26日 下午5:50:48
 */
public class SpringEventListenerRegistryListener implements ApplicationListener<ApplicationEvent>, PriorityOrdered {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringEventListenerRegistryListener.class);
	
	private SimpleApplicationEventMulticaster multicaster;
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(multicaster == null) {
			multicaster = new SimpleApplicationEventMulticaster();
			List<ApplicationListener<ApplicationEvent>> listeners = scanSpringEventListeners();
			for (ApplicationListener<ApplicationEvent> listener : listeners) {
				multicaster.addApplicationListener(listener);
				LOGGER.info(">>> Registry a scanned ApplicationListener (annotated by @SpringEventListener): {}", listener.getClass());
			}
		}
		multicaster.multicastEvent(event);
	}

	@SuppressWarnings("unchecked")
	protected List<ApplicationListener<ApplicationEvent>> scanSpringEventListeners() {
		List<ApplicationListener<ApplicationEvent>> listeners = new ArrayList<ApplicationListener<ApplicationEvent>>();
		Set<String> classNames = ClassScanningUtils.scanPackageClassNames(BasePackage.class.getPackage().getName());
		for(String className : classNames) {
			try {
				Class<?> clazz = Class.forName(className);
				if(ApplicationListener.class.isAssignableFrom(clazz) && AnnotationUtils.findAnnotation(clazz, SpringEventListener.class) != null) {
					listeners.add(BeanUtils.instantiateClass((Class<ApplicationListener<ApplicationEvent>>) clazz));
				}
			} catch (Throwable e) {
				LOGGER.error(e.getMessage());
			}
		}
		return listeners.stream().sorted(AnnotationAwareOrderComparator.INSTANCE).collect(Collectors.toList());
	}
	
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

}
