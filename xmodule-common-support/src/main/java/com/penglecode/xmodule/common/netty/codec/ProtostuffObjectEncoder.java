package com.penglecode.xmodule.common.netty.codec;

import com.penglecode.xmodule.common.serializer.ObjectSerializer;
import com.penglecode.xmodule.common.serializer.protostuff.ProtostuffSerializer;

/**
 * 基于Protostuff的对象到字节的Encoder
 * 
 * @author 	pengpeng
 * @date	2018年12月14日 上午9:16:43
 */
public class ProtostuffObjectEncoder extends AbstractObjectEncoder<Object> {

	private final ObjectSerializer objectSerializer = ProtostuffSerializer.INSTANCE;
	
	@Override
	protected byte[] objectToBytes(Object msg) throws Exception {
		return objectSerializer.serialize(msg);
	}

	public ObjectSerializer getObjectSerializer() {
		return objectSerializer;
	}

}
