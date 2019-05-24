package com.penglecode.xmodule.common.boot.config;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
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
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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
import com.penglecode.xmodule.common.support.DtoModel;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.ReflectionUtils;
import com.penglecode.xmodule.common.web.springmvc.handler.AbstractMvcHandlerExceptionResolver;
import com.penglecode.xmodule.common.web.springmvc.handler.DefaultMvcHandlerExceptionResolver;
import com.penglecode.xmodule.common.web.springmvc.resolver.EnhancedRequestParamMethodArgumentResolver;
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
	
	private final List<HttpMessageConverter<?>> httpMessageConverters;
	
	private final DefaultListableBeanFactory beanFactory;
	
	private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;
	
	public DefaultSpringWebMvcConfiguration(ObjectProvider<List<HttpMessageConverter<?>>> httpMessageConvertersProvider,
			ObjectProvider<DefaultListableBeanFactory> beanFactoryProvider) {
		super();
		this.httpMessageConverters = httpMessageConvertersProvider.getIfAvailable();
		this.beanFactory = beanFactoryProvider.getIfAvailable();
		this.requestMappingHandlerAdapter = createRequestMappingHandlerAdapter();
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
	
	@Bean
	@SuppressWarnings("rawtypes")
	public HttpMessageConverters httpMessageConverters() {
		Map<Class<?>,HttpMessageConverter<?>> finalConverters = new LinkedHashMap<Class<?>,HttpMessageConverter<?>>();
		
		Map<String,HttpMessageConverter> defaultConverters = beanFactory.getBeansOfType(HttpMessageConverter.class);
		if(!CollectionUtils.isEmpty(defaultConverters)) {
			for(Map.Entry<String,HttpMessageConverter> entry : defaultConverters.entrySet()) {
				HttpMessageConverter converter = entry.getValue();
				finalConverters.put(converter.getClass(), converter);
			}
		}
		
		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
		stringHttpMessageConverter.setDefaultCharset(DEFAULT_CHARSET);
		stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_HTML, MediaType.TEXT_PLAIN, MediaType.ALL));
		finalConverters.put(stringHttpMessageConverter.getClass(), stringHttpMessageConverter);
		
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setDefaultCharset(DEFAULT_CHARSET);
		mappingJackson2HttpMessageConverter.setObjectMapper(JsonUtils.getDefaultObjectMapper());
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		finalConverters.put(mappingJackson2HttpMessageConverter.getClass(), mappingJackson2HttpMessageConverter);
		
		if(!CollectionUtils.isEmpty(httpMessageConverters)) {
			for(HttpMessageConverter<?> converter : httpMessageConverters) {
				finalConverters.put(converter.getClass(), converter);
			}
		}
		
		return new HttpMessageConverters(finalConverters.values());
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

	@Override
	public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
		return requestMappingHandlerAdapter;
	}
	
	protected RequestMappingHandlerAdapter createRequestMappingHandlerAdapter() {
		RequestMappingHandlerAdapter oldRequestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
		RequestMappingHandlerAdapter newRequestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
		
		Method getDefaultArgumentResolversMethod = ReflectionUtils.findMethod(RequestMappingHandlerAdapter.class, "getDefaultArgumentResolvers");
		List<HandlerMethodArgumentResolver> defaultArgumentResolvers = ReflectionUtils.invokeMethod(getDefaultArgumentResolversMethod, oldRequestMappingHandlerAdapter);
		replaceRequestParamMethodArgumentResolvers(defaultArgumentResolvers);
		newRequestMappingHandlerAdapter.setArgumentResolvers(defaultArgumentResolvers);
		
		Method getDefaultInitBinderArgumentResolversMethod = ReflectionUtils.findMethod(RequestMappingHandlerAdapter.class, "getDefaultInitBinderArgumentResolvers");
		List<HandlerMethodArgumentResolver> defaultInitBinderArgumentResolvers = ReflectionUtils.invokeMethod(getDefaultInitBinderArgumentResolversMethod, oldRequestMappingHandlerAdapter);
		replaceRequestParamMethodArgumentResolvers(defaultInitBinderArgumentResolvers);
		newRequestMappingHandlerAdapter.setInitBinderArgumentResolvers(defaultInitBinderArgumentResolvers);
		return newRequestMappingHandlerAdapter;
	}
	
	protected void replaceRequestParamMethodArgumentResolvers(List<HandlerMethodArgumentResolver> methodArgumentResolvers) {
		methodArgumentResolvers.forEach(argumentResolver -> {
			if(argumentResolver.getClass().equals(RequestParamMethodArgumentResolver.class)) {
				Boolean useDefaultResolution = ReflectionUtils.getFieldValue(argumentResolver, "useDefaultResolution");
				EnhancedRequestParamMethodArgumentResolver enhancedArgumentResolver = new EnhancedRequestParamMethodArgumentResolver(beanFactory, useDefaultResolution);
				enhancedArgumentResolver.setResolvableParameterTypes(Arrays.asList(DtoModel.class));
				Collections.replaceAll(methodArgumentResolvers, argumentResolver, enhancedArgumentResolver);
			}
		});
	}
	
}
