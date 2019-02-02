package com.penglecode.xmodule.common.serializer.protostuff;

import com.penglecode.xmodule.common.serializer.ObjectSerializer;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
/**
 * 基于protostuff的对象序列化
 * 
 * @author 	pengpeng
 * @date	2019年1月28日 下午4:24:07
 */
@SuppressWarnings({"unchecked"})
public class ProtostuffSerializer implements ObjectSerializer {

	public static final ProtostuffSerializer INSTANCE = new ProtostuffSerializer();
	
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024;
	
	private static final Schema<ObjectWrapper> SCHEMA;
	
	static {
		ProtostuffDelegates.initDelegates();
		SCHEMA = (Schema<ObjectWrapper>) RuntimeSchema.getSchema(ObjectWrapper.class);
	}
	
	public byte[] serialize(Object object) {
		if(object == null){
			return null;
		}
		LinkedBuffer buffer = LinkedBuffer.allocate(DEFAULT_BUFFER_SIZE);
		try {
			return ProtostuffIOUtil.toByteArray(new ObjectWrapper(object), SCHEMA, buffer);
		} finally {
			buffer.clear();
		}
	}

	public <T> T deserialize(byte[] bytes) {
		if(bytes == null || bytes.length == 0){
			return null;
		}
		try {
			ObjectWrapper objectWrapper = new ObjectWrapper();
			ProtostuffIOUtil.mergeFrom(bytes, objectWrapper, SCHEMA);
			return (T) objectWrapper.getObject();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
}