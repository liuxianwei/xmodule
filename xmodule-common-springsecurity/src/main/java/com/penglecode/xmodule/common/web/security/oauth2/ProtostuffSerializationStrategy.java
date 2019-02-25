package com.penglecode.xmodule.common.web.security.oauth2;

import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.serializer.protostuff.ProtostuffSerializer;

/**
 * 基于Protostuff的RedisTokenStoreSerializationStrategy实现
 * 
 * {@link #OAuth2ProtostuffDelegates}
 * @author 	pengpeng
 * @date	2019年2月23日 上午10:48:01
 */
public class ProtostuffSerializationStrategy extends StandardStringSerializationStrategy {

	private final ProtostuffSerializer serializer;
	
	public ProtostuffSerializationStrategy() {
		this(ProtostuffSerializer.INSTANCE);
	}

	public ProtostuffSerializationStrategy(ProtostuffSerializer serializer) {
		super();
		Assert.notNull(serializer, "Parameter 'serializer' must be required!");
		this.serializer = serializer;
	}

	@Override
	protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
		return serializer.deserialize(bytes);
	}

	@Override
	protected byte[] serializeInternal(Object object) {
		return serializer.serialize(object);
	}

	public ProtostuffSerializer getSerializer() {
		return serializer;
	}

}
