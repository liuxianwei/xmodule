package com.penglecode.xmodule.common.netty.codec;

import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.penglecode.xmodule.common.util.ArrayUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * 基于约定的通过Jackson2框架进行反序列化的ObjectDecoder
 * 
 * 接收到的消息格式：
 * 			{
 * 				"messageType": "java.util.List<com.xyz.bean.User>",
 * 				"messageData": [{fields of <com.xyz.bean.User>}, {fields of <com.xyz.bean.User>}]
 * 			}
 * 
 * @author 	pengpeng
 * @date	2018年12月13日 下午4:44:46
 */
public class Jackson2ObjectDecoder extends AbstractObjectDecoder {

	private static final String MESSAGE_TYPE_NAME = "messageType";
	
	private static final String MESSAGE_DATA_NAME = "messageData";
	
	private static final String DEFAULT_JSON_OBJECT_CANONICAL = "java.util.LinkedHashMap";
	
	private final ObjectMapper objectMapper;
	
	private final TypeFactory typeFactory;
	
	private String charset = "UTF-8";
	
	public Jackson2ObjectDecoder() {
		this(JsonUtils.createDefaultObjectMapper());
	}

	public Jackson2ObjectDecoder(ObjectMapper objectMapper) {
		super();
		Assert.notNull(objectMapper, "Parameter 'objectMapper' can not be null!");
		this.objectMapper = objectMapper;
		this.typeFactory = objectMapper.getTypeFactory();
	}

	@Override
	protected Object bytesToObject(byte[] datas) throws Exception {
		if(!ArrayUtils.isEmpty(datas)) {
			String json = new String(datas, charset);
			Assert.isTrue(JsonUtils.isJsonObject(json), String.format("Expect to decode a JSON Object data, but got a unrecognized data : %s", json));
			JsonNode rootNode = objectMapper.readTree(json);
			String msgJavaTypeCanonical = getCanonicalMessageType(rootNode);
			JavaType msgJavaType = typeFactory.constructFromCanonical(msgJavaTypeCanonical);
			JsonObjectMessage<Object> message = new JsonObjectMessage<Object>();
			message.setMessageType(msgJavaTypeCanonical);
			
			JsonNode messageNode = rootNode.get(MESSAGE_DATA_NAME);
			json = messageNode.toString();
			Object messageData = objectMapper.readValue(json, msgJavaType);
			message.setMessageData(messageData);
			return message;
		}
		return null;
	}
	
	protected String getCanonicalMessageType(JsonNode jsonNode) {
		Assert.isTrue(jsonNode instanceof ObjectNode, "Only JSON Object data or JSON Object Array data can be supported!");
		String canonical = null;
		if(jsonNode.hasNonNull(MESSAGE_TYPE_NAME)) {
			canonical = jsonNode.get(MESSAGE_TYPE_NAME).textValue();
			if(!StringUtils.isEmpty(canonical)) {
				if(createJavaTypeFromCanonical(canonical) == null) {
					canonical = null;
				}
			}
		}
		return StringUtils.defaultIfEmpty(canonical, DEFAULT_JSON_OBJECT_CANONICAL);
	}
	
	protected JavaType createJavaTypeFromCanonical(String canonical) {
		return typeFactory.constructFromCanonical(canonical);
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public TypeFactory getTypeFactory() {
		return typeFactory;
	}

}
