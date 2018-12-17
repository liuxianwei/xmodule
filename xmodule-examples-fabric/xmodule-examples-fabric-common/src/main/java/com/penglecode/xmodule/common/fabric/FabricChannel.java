package com.penglecode.xmodule.common.fabric;

import java.util.EnumSet;
import java.util.Map;

import org.hyperledger.fabric.sdk.BlockListener;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Channel.PeerOptions;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.Peer.PeerRole;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.fabric.exception.FabricRuntimeException;
import com.penglecode.xmodule.common.fabric.support.DefaultBlockListener;
import com.penglecode.xmodule.common.util.CollectionUtils;

/**
 * Fabric网络
 * 
 * @author 	pengpeng
 * @date	2018年12月17日 上午11:25:05
 */
public class FabricChannel implements InitializingBean, DisposableBean {

	/**
	 * Fabric基础配置
	 */
	private final FabricConfiguration configuration;
	
	/**
	 * Fabric联盟组织
	 */
	private final FabricOrganization organization;
	
	/**
	 * Fabric网络名称
	 */
	private final String channelName;
	
	/**
	 * Fabric通道
	 */
	private volatile Channel channel;
	
	/**
	 * Fabric客户端
	 */
	private HFClient hfClient;
	
	/**
	 * 默认的BlockListener
	 */
	private BlockListener blockListener = new DefaultBlockListener();
	
	private final Object mutex = new Object();

	public FabricChannel(FabricConfiguration configuration, FabricOrganization organization, String channelName) {
		super();
		Assert.notNull(configuration, "Parameter 'configuration' must be required!");
		Assert.notNull(organization, "Parameter 'organization' must be required!");
		this.configuration = configuration;
		this.organization = organization;
		this.channelName = channelName;
	}
	
	protected void init() throws Exception {
		CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
        HFClient hfClient = HFClient.createNewInstance();
        hfClient.setCryptoSuite(cryptoSuite);
        this.hfClient = hfClient;
        this.hfClient.setUserContext(organization.getPeerAdmin());
        this.channel = getChannel();
	}
	
	protected Channel createChannel() throws Exception {
		Channel channel = hfClient.newChannel(getChannelName());
		Map<String,String> peers = organization.getPeers();
		for(Map.Entry<String,String> entry : peers.entrySet()) {
			String peerName = entry.getKey();
			String peerUrl = entry.getValue();
			Peer peer = hfClient.newPeer(peerName, peerUrl, configuration.getPeerProperties(peerName));
			channel.addPeer(peer);
		}
		
		Map<String,String> orderers = organization.getOrderers();
		for(Map.Entry<String,String> entry : orderers.entrySet()) {
			String ordererName = entry.getKey();
			String ordererUrl = entry.getValue();
			Orderer orderer = hfClient.newOrderer(ordererName, ordererUrl, configuration.getOrdererProperties(ordererName));
			channel.addOrderer(orderer);
		}
		
		Map<String,String> eventHubs = organization.getEventHubs();
		if(!CollectionUtils.isEmpty(eventHubs)) {
			PeerOptions eventPeerOptions = PeerOptions.createPeerOptions().setPeerRoles(EnumSet.of(PeerRole.EVENT_SOURCE));
			for(Map.Entry<String,String> entry : eventHubs.entrySet()) {
				String eventHubName = entry.getKey();
				String eventHubUrl = entry.getValue();
				Peer eventPeer = hfClient.newPeer(eventHubName, eventHubUrl, configuration.getEventHubProperties(eventHubName));
				channel.addPeer(eventPeer, eventPeerOptions.startEvents(0));
			}
		}
		
		channel.initialize(); //初始化通道
		
		if(configuration.isRegisterEvent()) {
			if(blockListener != null) { //注册区块事件
				channel.registerBlockListener(blockListener);
			}
		}
		return channel;
	}
	
	/**
	 * 获取Channel，如果channel为null则创建
	 * 如果channel没有初始化则先初始化
	 * @return
	 */
	public Channel getChannel() {
		try {
			if(channel == null) {
				synchronized(mutex) {
					if(channel == null) {
						channel = createChannel();
					}
				}
			}
			if(!channel.isInitialized()) {
				channel.initialize();
			}
			return channel;
		} catch (Exception e) {
			throw new FabricRuntimeException(e);
		}
	}

	public HFClient getHfClient() {
		return hfClient;
	}

	public FabricConfiguration getConfiguration() {
		return configuration;
	}

	public FabricOrganization getOrganization() {
		return organization;
	}
	
	public String getChannelName() {
		return channelName;
	}
	
	public BlockListener getBlockListener() {
		return blockListener;
	}

	public void setBlockListener(BlockListener blockListener) {
		this.blockListener = blockListener;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	@Override
	public void destroy() throws Exception {
		if(!channel.isShutdown()) {
			channel.shutdown(true);
		}
	}
	
}
