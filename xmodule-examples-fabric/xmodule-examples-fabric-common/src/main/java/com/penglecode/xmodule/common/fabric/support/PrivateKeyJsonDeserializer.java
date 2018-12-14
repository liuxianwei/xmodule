package com.penglecode.xmodule.common.fabric.support;

import java.io.IOException;
import java.security.PrivateKey;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.penglecode.xmodule.common.fabric.util.FabricUtils;

public class PrivateKeyJsonDeserializer extends JsonDeserializer<PrivateKey> {

	@Override
	public PrivateKey deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String stringKey = jp.getText();
		return FabricUtils.toPrivateKey(stringKey);
	}

}
