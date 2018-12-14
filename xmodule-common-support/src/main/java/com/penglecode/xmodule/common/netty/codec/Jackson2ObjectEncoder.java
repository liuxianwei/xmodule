package com.penglecode.xmodule.common.netty.codec;

import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.penglecode.xmodule.common.util.JsonUtils;

/**
 * 基于约定的通过Jackson2框架进行序列化的ObjectEncoder
 * 
 * 发送出去的消息格式：
 * 			{
 * 				"messageType": "java.util.List<com.xyz.bean.User>",
 * 				"messageData": [{fields of <com.xyz.bean.User>}, {fields of <com.xyz.bean.User>}]
 * 			}
 * 
 * @author 	pengpeng
 * @date	2018年12月13日 下午4:43:33
 */
public class Jackson2ObjectEncoder extends AbstractObjectEncoder<JsonObjectMessage<?>> {

	private final ObjectMapper objectMapper;
	
	private String charset = "UTF-8";
	
	public Jackson2ObjectEncoder() {
		this(JsonUtils.createDefaultObjectMapper());
	}

	public Jackson2ObjectEncoder(ObjectMapper objectMapper) {
		super();
		Assert.notNull(objectMapper, "Parameter 'objectMapper' can not be null!");
		this.objectMapper = objectMapper;
	}
	
	@Override
	protected byte[] objectToBytes(JsonObjectMessage<?> msg) throws Exception {
		Assert.notNull(msg, "Parameter 'msg' can not be null!");
		Assert.hasText(msg.getMessageType(), "Parameter 'msg[messageType]' must be required!");
		Assert.notNull(msg.getMessageData(), "Parameter 'msg[messageData]' can not be null!");
		String json = objectMapper.writeValueAsString(msg);
		return json.getBytes(charset);
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

}
