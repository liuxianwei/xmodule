package com.penglecode.xmodule.common.netty.support;

import org.springframework.util.Assert;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public abstract class AbstractSimpleNettyClient {

	public static final NettyChannelGroupName ALL_CLIENT_CHANNELS = NettyChannelGroupName.valueOf("ALL_CLIENT_CHANNELS");
	
	private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
	
	private final Bootstrap bootstrap = new Bootstrap();
	
	private final ChannelGroup channelGroup;
	
	private final String host;
	
	private final int port;
	
	private int connectTimeoutMillis = 15000;
	
	private final ChannelInitializer<SocketChannel> channelInitializer;
	
	private volatile boolean started = false;
	
	public AbstractSimpleNettyClient(String host, int port, ChannelInitializer<SocketChannel> channelInitializer) {
		super();
		Assert.hasText(host, "Netty Server host must required!");
		Assert.isTrue(port > 0, "Netty Server port must be > 0");
		Assert.notNull(channelInitializer, "Netty Client channelInitializer can not be null!");
		this.host = host;
		this.port = port;
		this.channelInitializer = channelInitializer;
		this.channelGroup = NettyChannelGroups.getChannelGroup(ALL_CLIENT_CHANNELS);
	}

	/**
	 * 启动客户端
	 */
	public void start() {
		if(!started) {
			init();
		}
	}
	
	/**
	 * 初始化客户端
	 */
	protected synchronized void init() {
		if(!started) {
			bootstrap.group(eventLoopGroup)
				.channel(NioSocketChannel.class) //指定channel类型
				.remoteAddress(host, port) //设置远程服务端host,port
				.handler(channelInitializer)
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMillis);
			started = true;
		}
	}
	
	/**
	 * 连接到远程&等待连接完成
	 * @return
	 */
	public Channel connect() {
		Channel channel = bootstrap.connect().syncUninterruptibly().channel();
		channelGroup.add(channel);
		return channel;
	}
	
	/**
	 * 关闭线程池和释放所有资源
	 */
	public void stop() {
		eventLoopGroup.shutdownGracefully().syncUninterruptibly();
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int getConnectTimeoutMillis() {
		return connectTimeoutMillis;
	}

	public void setConnectTimeoutMillis(int connectTimeoutMillis) {
		this.connectTimeoutMillis = connectTimeoutMillis;
	}

	protected ChannelInitializer<SocketChannel> getChannelInitializer() {
		return channelInitializer;
	}

	public EventLoopGroup getEventLoopGroup() {
		return eventLoopGroup;
	}

	public Bootstrap getBootstrap() {
		return bootstrap;
	}

	public boolean isStarted() {
		return started;
	}

	public ChannelGroup getChannelGroup() {
		return channelGroup;
	}

}
