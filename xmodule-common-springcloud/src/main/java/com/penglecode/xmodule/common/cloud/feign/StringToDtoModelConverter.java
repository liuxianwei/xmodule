package com.penglecode.xmodule.common.cloud.feign;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.support.DtoModel;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.StringUtils;

public class StringToDtoModelConverter<T extends DtoModel> implements Converter<String, T> {

	private final Class<T> dtoModelType;
	
	public StringToDtoModelConverter(Class<T> dtoModelType) {
		super();
		Assert.notNull(dtoModelType, "Parameter 'dtoModelType' can not be null!");
		this.dtoModelType = dtoModelType;
	}

	public Class<? extends DtoModel> getDtoModelType() {
		return dtoModelType;
	}

	@Override
	public T convert(String source) {
		if(!StringUtils.isEmpty(source) && JsonUtils.isJsonObject(source)) {
			return JsonUtils.json2Object(source, dtoModelType);
		}
		return null;
	}

}
