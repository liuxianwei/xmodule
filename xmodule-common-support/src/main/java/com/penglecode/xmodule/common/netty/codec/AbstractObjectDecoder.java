package com.penglecode.xmodule.common.netty.codec;

import com.penglecode.xmodule.common.util.IOUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public abstract class AbstractObjectDecoder extends LengthFieldBasedFrameDecoder {

	private static final int DEFAULT_MAX_OBJECT_SIZE = 2 * 1024 * 1024;
	
	public AbstractObjectDecoder() {
		this(DEFAULT_MAX_OBJECT_SIZE);
	}
	
	public AbstractObjectDecoder(int maxObjectSize) {
		super(maxObjectSize, 0, 4, 0, 4);
	}
	
	@Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        try(ByteBufInputStream bis = new ByteBufInputStream(frame, true)) {
            return bytesToObject(IOUtils.toByteArray(bis));
        }
    }

	/**
	 * 将字节转换成对象
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	protected abstract Object bytesToObject(byte[] datas) throws Exception; 
	
}
