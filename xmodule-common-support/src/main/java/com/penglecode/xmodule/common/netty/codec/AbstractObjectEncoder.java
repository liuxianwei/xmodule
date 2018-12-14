package com.penglecode.xmodule.common.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public abstract class AbstractObjectEncoder<T> extends MessageToByteEncoder<T> {

	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
	
	@Override
	protected void encode(ChannelHandlerContext ctx, T msg, ByteBuf out) throws Exception {
		byte[] bodyBytes = objectToBytes(msg); //将对象转为byte[]
		
		int startIdx = out.writerIndex(); //标记消息开始位置
		
		out.writeBytes(LENGTH_PLACEHOLDER); //写入4字节空的消息头
		out.writeBytes(bodyBytes); //接着将消息内容写入

        int endIdx = out.writerIndex(); //标记消息结束位置

        out.setInt(startIdx, endIdx - startIdx - 4);
	}

	/**
	 * 将对象转换成字节
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	protected abstract byte[] objectToBytes(T msg) throws Exception;

}
