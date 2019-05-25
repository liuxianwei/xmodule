package com.penglecode.xmodule.common.boot.config;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.Servlet;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.web.springmvc.handler.AbstractMvcHandlerExceptionResolver;
import com.penglecode.xmodule.common.web.springmvc.handler.DefaultMvcHandlerExceptionResolver;
import com.penglecode.xmodule.common.web.springmvc.support.EnhancedRequestMappingHandlerAdapter;
/**
 * SpringMVC的定制化配置
 * 
 * @author 	pengpeng
 * @date	2018年2月24日 下午1:28:23
 */
@Configuration
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
public class DefaultSpringWebMvcConfiguration extends AbstractSpringConfiguration implements WebMvcConfigurer, WebMvcRegistrations {
	
	public static final Charset DEFAULT_CHARSET = Charset.forName(GlobalConstants.DEFAULT_CHARSET);
	
	private final DefaultListableBeanFactory beanFactory;
	
	private final RequestMappingHandlerAdapter defaultRequestMappingHandlerAdapter = new EnhancedRequestMappingHandlerAdapter();
	
	public DefaultSpringWebMvcConfiguration(DefaultListableBeanFactory beanFactory) {
		super();
		this.beanFactory = beanFactory;
	}

	@Bean
	@ConditionalOnMissingBean
	public DefaultMvcHandlerExceptionResolver defaultMvcHandlerExceptionResolver() {
		DefaultMvcHandlerExceptionResolver mvcHandlerExceptionResolver = new DefaultMvcHandlerExceptionResolver();
		mvcHandlerExceptionResolver.setDefaultExceptionView("/common/error/500.html");
		return mvcHandlerExceptionResolver;
	}
	
	@Bean
	public RequestContextFilter requestContextFilter() {
		OrderedRequestContextFilter filter = new OrderedRequestContextFilter();
		filter.setOrder(getEnvironment().getProperty("spring.mvc.requestcontext.filter.order", Integer.class, FilterRegistrationOrder.ORDER_REQUEST_CONTEXT_FILTER));
		return filter;
	}
	
	@Bean
	public InternalResourceViewResolver jspViewResolver() {
		InternalResourceViewResolver jspViewResolver = new InternalResourceViewResolver();
		jspViewResolver.setContentType("text/html");
		jspViewResolver.setPrefix("/WEB-INF/jsp/");
		jspViewResolver.setViewClass(JstlView.class);
		jspViewResolver.setSuffix(".jsp");
		return jspViewResolver;
	}
	
	@Bean
	public BeanNameViewResolver beanNameViewResolver() {
		BeanNameViewResolver resolver = new BeanNameViewResolver();
		resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
		return resolver;
	}
	
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		if(!CollectionUtils.isEmpty(converters)) {
			Map<Class<?>, HttpMessageConverter<?>> finalConverters = converters.stream().collect(Collectors.toMap(HttpMessageConverter::getClass, Function.identity(), (converter1, converter2) -> converter1));
			StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
			stringHttpMessageConverter.setDefaultCharset(DEFAULT_CHARSET);
			stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_HTML, MediaType.TEXT_PLAIN, MediaType.ALL));
			finalConverters.put(stringHttpMessageConverter.getClass(), stringHttpMessageConverter);
			
			MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
			mappingJackson2HttpMessageConverter.setDefaultCharset(DEFAULT_CHARSET);
			mappingJackson2HttpMessageConverter.setObjectMapper(JsonUtils.getDefaultObjectMapper());
			mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			finalConverters.put(mappingJackson2HttpMessageConverter.getClass(), mappingJackson2HttpMessageConverter);
			
			converters.clear();
			converters.addAll(finalConverters.values());
		}
	}

	/**
	 * 过滤静态资源不走DispatcherServlet
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//上传文件的保存目录：C:/Users/Pengle/AppData/Local/Temp/tomcat-docbase.5548171285913942755.8080/upload，需要将其作为静态资源进行过滤(不走Controller)
		registry.addResourceHandler("/upload/**").addResourceLocations("/upload/");
	}
	
	/**
	 * 在SpringMVC中，路径参数(PathVariable)如果带.的话，.后面的值将被忽略掉,通过以下配置来更改
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
        configurer.setUseTrailingSlashMatch(true);
    }
	
	/**
	 * 配置MVC全局异常处理器
	 */
	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		Map<String,AbstractMvcHandlerExceptionResolver> beans = beanFactory.getBeansOfType(AbstractMvcHandlerExceptionResolver.class);
		if(!CollectionUtils.isEmpty(beans)) {
			resolvers.addAll(beans.values());
		}
	}

	/**
	 * 自定义RequestMappingHandlerAdapter
	 */
	@Override
	public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
		return defaultRequestMappingHandlerAdapter;
	}
	
}
