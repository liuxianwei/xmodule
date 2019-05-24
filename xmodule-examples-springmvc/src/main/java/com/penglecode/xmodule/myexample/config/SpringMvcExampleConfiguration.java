package com.penglecode.xmodule.myexample.config;

import java.util.Arrays;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;

@Configuration
public class SpringMvcExampleConfiguration extends AbstractSpringConfiguration {

	private final ListableBeanFactory beanFactory;

	public SpringMvcExampleConfiguration(ListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		System.out.println("=================> " + Arrays.toString(this.beanFactory.getBeanNamesForType(ConversionService.class)));
	}
	
}
