package com.penglecode.xmodule.common.fabric.support;

import java.io.IOException;
import java.security.PrivateKey;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.penglecode.xmodule.common.fabric.util.FabricUtils;

public class PrivateKeyJsonSerializer extends JsonSerializer<PrivateKey> {

	@Override
	public void serialize(PrivateKey value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
		jgen.writeString(FabricUtils.toString(value));
	}

}
