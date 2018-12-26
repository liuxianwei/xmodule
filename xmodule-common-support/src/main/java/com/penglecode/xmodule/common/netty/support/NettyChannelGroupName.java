package com.penglecode.xmodule.common.netty.support;

import io.netty.util.AbstractConstant;
import io.netty.util.ConstantPool;

public class NettyChannelGroupName extends AbstractConstant<NettyChannelGroupName> {

	private static final ConstantPool<NettyChannelGroupName> pool = new ConstantPool<NettyChannelGroupName>() {
		@Override
		protected NettyChannelGroupName newConstant(int id, String name) {
			return new NettyChannelGroupName(id, name);
		}
	};
	
	public static NettyChannelGroupName valueOf(String name) {
		return pool.valueOf(name);
	}
	
	private NettyChannelGroupName(int id, String name) {
		super(id, name);
	}

}
