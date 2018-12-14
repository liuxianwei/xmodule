package com.penglecode.xmodule.common.netty.codec;

import com.penglecode.xmodule.common.serializer.ObjectSerializer;
import com.penglecode.xmodule.common.serializer.protostuff.ProtostuffSerializer;

/**
 * 基于Protostuff的字节到对象的Decoder
 * 
 * @author 	pengpeng
 * @date	2018年12月14日 上午9:16:07
 */
public class ProtostuffObjectDecoder extends AbstractObjectDecoder {

	private final ObjectSerializer objectSerializer = ProtostuffSerializer.INSTANCE;
	
	@Override
	protected Object bytesToObject(byte[] datas) throws Exception {
		return objectSerializer.deserialize(datas);
	}

	public ObjectSerializer getObjectSerializer() {
		return objectSerializer;
	}

}
