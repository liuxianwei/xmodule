package com.penglecode.xmodule.common.security.oauth2;

import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.penglecode.xmodule.common.util.JsonUtils;

/**
 * 基于Jackson2的JSON序列化RedisTokenStoreSerializationStrategy实现
 * 主要向ObjectMapper注册了SpringSecurity-OAuth2令牌序列化中各对象的Serializer/Deserializer实现类
 * 
 * {@link #OAuth2TokenJackson2Module}
 * @author 	pengpeng
 * @date	2019年2月23日 上午10:48:01
 */
public class Jackson2SerializationStrategy extends StandardStringSerializationStrategy {

	private final ObjectMapper objectMapper;
	
	public Jackson2SerializationStrategy() {
		this(JsonUtils.createDefaultObjectMapper());
	}

	public Jackson2SerializationStrategy(ObjectMapper objectMapper) {
		super();
		Assert.notNull(objectMapper, "Parameter 'objectMapper' must be required!");
		objectMapper.registerModule(new OAuth2TokenJackson2Module());
		this.objectMapper = objectMapper;
	}

	@Override
	protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
		String json = deserializeStringInternal(bytes);
		return JsonUtils.json2Object(objectMapper, json, clazz);
	}

	@Override
	protected byte[] serializeInternal(Object object) {
		String json = JsonUtils.object2Json(objectMapper, object);
		return serializeInternal(json);
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

}
