package com.penglecode.xmodule.common.web.jersey.jackson;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.penglecode.xmodule.common.util.JsonUtils;

@Provider
public class JacksonObjectMapperProvider implements ContextResolver<ObjectMapper> {

	private final ObjectMapper objectMapper;
	
    public JacksonObjectMapperProvider() {
		super();
		this.objectMapper = JsonUtils.createDefaultObjectMapper();
	}

	@Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}
