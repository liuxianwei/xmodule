package com.penglecode.xmodule.upms.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.upms.web.filter.UpmsLoginUserSessionTestApplyFilter;

@Configuration
@ConditionalOnWebApplication(type=Type.SERVLET)
@ConditionalOnProperty(prefix="upms.session", name="test", havingValue="true")
public class UpmsTestSessionConfiguration extends AbstractSpringConfiguration {

	@Bean(name="upmsLoginUserSessionTestApplyFilter")
	public UpmsLoginUserSessionTestApplyFilter upmsLoginUserSessionTestApplyFilter() {
		return new UpmsLoginUserSessionTestApplyFilter();
	}
	
	@Bean
	public FilterRegistrationBean<Filter> enableUpmsLoginUserSessionTestApplyFilter(@Qualifier("upmsLoginUserSessionTestApplyFilter") Filter filter) {
	    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
	    filterRegistrationBean.setFilter(filter);
	    filterRegistrationBean.setEnabled(true);
	    filterRegistrationBean.setName("upmsLoginUserSessionTestApplyFilter");
	    filterRegistrationBean.addUrlPatterns("/*");
	    filterRegistrationBean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 1);
	    return filterRegistrationBean;
	}
	
}
