package com.penglecode.xmodule.common.netty.support;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyChannelGroups {

	private static final Map<NettyChannelGroupName,ChannelGroup> channelGroups = new HashMap<NettyChannelGroupName,ChannelGroup>();
	
	private static ChannelGroup createDefaultChannelGroup() {
		return new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	}
	
	public static ChannelGroup getChannelGroup(NettyChannelGroupName groupName) {
		ChannelGroup channelGroup = channelGroups.get(groupName);
		if(channelGroup == null) {
			synchronized (channelGroups) {
				channelGroup = createDefaultChannelGroup();
				channelGroups.put(groupName, channelGroup);
			}
		}
		return channelGroup;
	}
}
