package com.penglecode.xmodule.common.netty.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 　Netty 服务器的通信步骤为：
 * 
 *  	1、创建两个NIO线程组，一个专门用于接收来自客户端的连接，另一个则用于处理已经被接收的连接。
 *  	2、创建一个ServerBootstrap对象，配置Netty的一系列参数，例如接受传出数据的缓存大小等。
 *  	3、创建一个用于实际处理数据的类ChannelInitializer，进行初始化的准备工作，比如设置接受传出数据的字符集、格式以及实际处理数据的接口。
 *  	4、绑定端口，执行同步阻塞方法等待服务器端启动即可。
 * 
 * @author 	pengpeng
 * @date	2018年12月10日 下午2:14:56
 */
public abstract class AbstractSimpleNettyServer implements Runnable, InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSimpleNettyServer.class);
	
	private final int port;
	
	private final ChannelInitializer<SocketChannel> channelInitializer;
	
	public AbstractSimpleNettyServer(int port, ChannelInitializer<SocketChannel> channelInitializer) {
		super();
		Assert.isTrue(port > 0, "Netty Server port must be > 0");
		Assert.notNull(channelInitializer, "Netty Server channelInitializer can not be null!");
		this.port = port;
		this.channelInitializer = channelInitializer;
	}

	@Override
	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); //用来接收进来的连接
		EventLoopGroup workerGroup = new NioEventLoopGroup(); //用来处理已经被接收的连接
		
		ServerBootstrap bootstrap = new ServerBootstrap(); //服务端启动程序
		try {
			bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class) //指定通道类型
				.localAddress(port) //指定端口
				.childHandler(channelInitializer)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childOption(ChannelOption.TCP_NODELAY, true)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture channelFuture = bootstrap.bind().syncUninterruptibly(); //绑定的服务器,同步等待服务器关闭
			LOGGER.info(">>> Netty Server startup on port : {}", port);
			channelFuture.channel().closeFuture().syncUninterruptibly(); //同步关闭channel
		} finally {
			workerGroup.shutdownGracefully().syncUninterruptibly();
			bossGroup.shutdownGracefully().syncUninterruptibly();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		new Thread(this).start();
	}

	public int getPort() {
		return port;
	}

	protected ChannelInitializer<SocketChannel> getChannelInitializer() {
		return channelInitializer;
	}

}
