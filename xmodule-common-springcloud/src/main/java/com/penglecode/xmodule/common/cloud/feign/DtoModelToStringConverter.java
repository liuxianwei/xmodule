package com.penglecode.xmodule.common.cloud.feign;

import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;
import com.penglecode.xmodule.common.support.DtoModel;
import com.penglecode.xmodule.common.util.JsonUtils;

/**
 * spring-cloud-starter-openfeign中针对@RequestParam注解复杂自定义对象时用到此converter
 * 
 * 将DtoModel对象转换成json
 * 
 * @author 	pengpeng
 * @date	2018年10月11日 上午11:10:38
 */
public class DtoModelToStringConverter implements Converter<DtoModel, String> {

	public static final String PROP_DTO_MODEL_TYPE_KEY = "dtoModelType";
	
	private final ObjectMapper objectMapper;
	
	public DtoModelToStringConverter() {
		super();
		this.objectMapper = JsonUtils.createDefaultObjectMapper();
		this.objectMapper.setSerializationInclusion(Include.NON_EMPTY);
	}

	@Override
	public String convert(DtoModel source) {
		try {
			String json = objectMapper.writeValueAsString(source);
			DocumentContext context = JsonPath.parse(json);
			context.put("$", PROP_DTO_MODEL_TYPE_KEY, source.getClass().getName()); //添加字段{"dtoModelType": "aaa.bbb.ccc.Xyz"}
			return context.jsonString();
		} catch (Exception e) {
			throw new ApplicationRuntimeException(e);
		}
	}
	
}
