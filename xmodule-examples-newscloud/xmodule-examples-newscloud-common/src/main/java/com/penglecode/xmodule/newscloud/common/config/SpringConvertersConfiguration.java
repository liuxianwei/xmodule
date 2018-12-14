package com.penglecode.xmodule.newscloud.common.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.cloud.feign.DtoModelToStringConverter;
import com.penglecode.xmodule.common.cloud.feign.StringToDtoModelConverter;
import com.penglecode.xmodule.common.cloud.feign.StringToDtoModelConverterFactory;
import com.penglecode.xmodule.common.support.DtoModel;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.newscloud.newscenter.model.News;
import com.penglecode.xmodule.newscloud.usercenter.model.User;

@Configuration
public class SpringConvertersConfiguration extends AbstractSpringConfiguration implements WebMvcConfigurer, FeignFormatterRegistrar {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		List<Converter<?,?>> converters = customConverters();
		for(Converter<?,?> converter : converters) {
			registry.addConverter(converter);
		}
		
		List<ConverterFactory<?,?>> converterFactorys = customConverterFactorys();
		for(ConverterFactory<?,?> converterFactory : converterFactorys) {
			registry.addConverterFactory(converterFactory);
		}
	}
	
	@Override
	public void registerFormatters(FormatterRegistry registry) {
		List<Converter<?,?>> converters = customConverters();
		for(Converter<?,?> converter : converters) {
			registry.addConverter(converter);
		}
		
		List<ConverterFactory<?,?>> converterFactorys = customConverterFactorys();
		for(ConverterFactory<?,?> converterFactory : converterFactorys) {
			registry.addConverterFactory(converterFactory);
		}
	}
	
	@Bean
	public List<Converter<?,?>> customConverters() {
		List<Converter<?,?>> converters = new ArrayList<Converter<?,?>>();
		converters.add(new DtoModelToStringConverter());
		return converters;
	}
	
	@Bean
	public List<ConverterFactory<?,?>> customConverterFactorys() {
		List<ConverterFactory<?,?>> converterFactorys = new ArrayList<>();
		
		Map<Class<? extends DtoModel>, Converter<String, ? extends DtoModel>> converters = new HashMap<>();
		converters.put(User.class, new StringToDtoModelConverter<User>(User.class));
		converters.put(News.class, new StringToDtoModelConverter<News>(News.class));
		converters.put(Sort.class, new StringToDtoModelConverter<Sort>(Sort.class));
		converters.put(Page.class, new StringToDtoModelConverter<Page>(Page.class));
		converterFactorys.add(new StringToDtoModelConverterFactory(converters));
		
		return converterFactorys;
	}
	
}
