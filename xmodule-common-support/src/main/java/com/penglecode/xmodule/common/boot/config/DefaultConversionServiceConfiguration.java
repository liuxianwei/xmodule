package com.penglecode.xmodule.common.boot.config;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import com.penglecode.xmodule.common.util.CollectionUtils;

@Configuration
@ConditionalOnMissingBean(ConversionService.class)
public class DefaultConversionServiceConfiguration extends AbstractSpringConfiguration {

	private final ObjectProvider<List<Converter<?, ?>>> convertersProvider;
	
	private final ObjectProvider<List<ConverterFactory<?, ?>>> converterFactorysProvider;
	
	public DefaultConversionServiceConfiguration(ObjectProvider<List<Converter<?, ?>>> convertersProvider,
			ObjectProvider<List<ConverterFactory<?, ?>>> converterFactorysProvider) {
		super();
		this.convertersProvider = convertersProvider;
		this.converterFactorysProvider = converterFactorysProvider;
	}

	@Bean(name="conversionService")
	public ConversionService conversionService() {
		GenericConversionService conversionService = new DefaultConversionService();
		List<Converter<?,?>> converters = convertersProvider.getIfAvailable();
		if(!CollectionUtils.isEmpty(converters)) {
			for(Converter<?, ?> converter : converters) {
				conversionService.addConverter(converter);
			}
		}
		List<ConverterFactory<?, ?>> converterFactorys = converterFactorysProvider.getIfAvailable();
		if(!CollectionUtils.isEmpty(converterFactorys)) {
			for(ConverterFactory<?, ?> converterFactory : converterFactorys) {
				conversionService.addConverterFactory(converterFactory);
			}
		}
		return conversionService;
	}
	
}
