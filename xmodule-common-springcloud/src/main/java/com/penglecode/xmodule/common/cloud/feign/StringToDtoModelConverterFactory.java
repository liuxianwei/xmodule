package com.penglecode.xmodule.common.cloud.feign;

import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import com.penglecode.xmodule.common.support.DtoModel;

@SuppressWarnings("unchecked")
public class StringToDtoModelConverterFactory implements ConverterFactory<String, DtoModel> {

	private final Map<Class<? extends DtoModel>, Converter<String, ? extends DtoModel>> converters;
	
	public StringToDtoModelConverterFactory(
			Map<Class<? extends DtoModel>, Converter<String, ? extends DtoModel>> converters) {
		super();
		this.converters = converters;
	}

	
	@Override
	public <T extends DtoModel> Converter<String, T> getConverter(Class<T> targetType) {
		if(converters != null) {
			return (Converter<String, T>) converters.get(targetType);
		}
		return null;
	}
	
}
