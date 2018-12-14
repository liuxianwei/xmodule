package com.penglecode.xmodule.common.fabric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ChaincodeEventListener;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Channel.PeerOptions;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.Peer.PeerRole;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.SDKUtils;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.fabric.exception.FabricRuntimeException;
import com.penglecode.xmodule.common.fabric.support.DefaultBlockListener;
import com.penglecode.xmodule.common.fabric.support.DefaultChaincodeEventListener;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.CollectionUtils;

/**
 * 默认的智能合约chaincode服务
 * 
 * @author 	pengpeng
 * @date	2018年11月10日 下午1:31:13
 */
public class DefaultChaincodeService implements ChaincodeService, InitializingBean, DisposableBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultChaincodeService.class);
	
	/**
	 * Fabric基础配置
	 */
	private final FabricConfiguration configuration;
	
	/**
	 * Fabric智能合约chaincode
	 */
	private final FabricChaincode chaincode;
	
	/**
	 * Fabric联盟组织
	 */
	private final FabricOrganization organization;
	
	/**
	 * Fabric通道
	 */
	private volatile Channel channel;
	
	/**
	 * Fabric客户端
	 */
	private HFClient hfClient;
	
	/**
	 * 智能合约chaincode事件监听器
	 */
	private ChaincodeEventListener chaincodeEventListener = new DefaultChaincodeEventListener();
	
	private final Object mutex = new Object();

	public DefaultChaincodeService(FabricConfiguration configuration, FabricOrganization organization,
			FabricChaincode chaincode) {
		super();
		Assert.notNull(configuration, "Parameter 'configuration' must be required!");
		Assert.notNull(chaincode, "Parameter 'chaincode' must be required!");
		Assert.notNull(organization, "Parameter 'organization' must be required!");
		this.configuration = configuration;
		this.organization = organization;
		this.chaincode = chaincode;
	}
	
	@Override
	public Result<String> executeQuery(String fcn, String[] args) throws Exception {
		return executeQuery(fcn, args, null);
	}

	@Override
	public Result<String> executeQuery(String fcn, String[] args, Map<String,byte[]> transientData) throws Exception {
		return execute(fcn, args, transientData, ChaincodeFunctionAccessType.QUERY, false);
	}
	
	@Override
	public Result<String> executeUpdate(String fcn, String[] args) throws Exception {
		return executeUpdate(fcn, args, null);
	}

	@Override
	public Result<String> executeUpdateAsync(String fcn, String[] args) throws Exception {
		return executeUpdateAsync(fcn, args, null);
	}
	
	@Override
	public Result<String> executeUpdate(String fcn, String[] args, Map<String, byte[]> transientData) throws Exception {
		return execute(fcn, args, transientData, ChaincodeFunctionAccessType.UPDATE, false);
	}
	
	@Override
	public Result<String> executeUpdateAsync(String fcn, String[] args, Map<String, byte[]> transientData) throws Exception {
		return execute(fcn, args, transientData, ChaincodeFunctionAccessType.UPDATE, true);
	}

	protected Result<String> execute(String fcn, String[] args, Map<String,byte[]> transientData, ChaincodeFunctionAccessType funcType, boolean asyncUpdate) throws Exception {
		LOGGER.info("【chaincode】>>> Prepare to invoke chaincode({}) now, fcn = {}, args = {} ", chaincode.getChaincodeName(), fcn, Arrays.toString(args));
		Channel channel = getChannel();
		TransactionProposalRequest transactionProposalRequest = hfClient.newTransactionProposalRequest();
		transactionProposalRequest.setChaincodeID(chaincode.getChaincodeID());
		transactionProposalRequest.setChaincodeLanguage(chaincode.getChaincodeLanguage());
		transactionProposalRequest.setFcn(fcn);
		if(args != null) {
			transactionProposalRequest.setArgs(args);
		}
		if(!CollectionUtils.isEmpty(transientData)) {
			transactionProposalRequest.setTransientMap(transientData);
		}
		transactionProposalRequest.setProposalWaitTime(chaincode.getProposalWaitTime());
		
		List<ProposalResponse> successResponses = new ArrayList<ProposalResponse>();
		List<ProposalResponse> failureResponses = new ArrayList<ProposalResponse>();
		
		long startTime = System.currentTimeMillis();
		Collection<ProposalResponse> proposalResponses = channel.sendTransactionProposal(transactionProposalRequest, channel.getPeers());
		for (ProposalResponse proposalResponse : proposalResponses) {
			if (proposalResponse.getStatus() == ProposalResponse.Status.SUCCESS) { //成功的提案
				successResponses.add(proposalResponse);
				LOGGER.info("【chaincode】>>> Successful transaction proposal response Txid: {} from peer {}", proposalResponse.getTransactionID(), proposalResponse.getPeer().getName());
			} else { //失败的提案
				failureResponses.add(proposalResponse);
				LOGGER.error("【chaincode】>>> Failed transaction proposal response Txid: {} from peer {}", proposalResponse.getTransactionID(), proposalResponse.getPeer().getName());
			}
		}
		LOGGER.info("【chaincode】>>> Channel send transaction proposal all done, it takes time : " + ( System.currentTimeMillis() - startTime));
		LOGGER.info("【chaincode】>>> Received {} transaction proposal responses, successful and verified：{}, failed：{}", proposalResponses.size(), successResponses.size(), failureResponses.size());
		
		if(!failureResponses.isEmpty()) { //存在失败的提案
			ProposalResponse firstProposalResponse = failureResponses.iterator().next();
			LOGGER.error("【chaincode】>>> Not enough endorsers for invoke chaincode({})：{}, endorser error：{} . Was verified：{}", failureResponses.size(), firstProposalResponse.getMessage(), firstProposalResponse.isVerified());
			return Result.failure().message("endorser error：" + firstProposalResponse.getMessage()).build();
		} else { //全部是成功的提案
			Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils.getProposalConsistencySets(proposalResponses); //检测各个节点成功背书结果的一致性
			if(proposalConsistencySets.size() != 1) { //如果所有节点的背书结果存在不同结果
				LOGGER.error("【chaincode】>>> Expected only one set of consistent proposal responses but got {}.", proposalConsistencySets.size());
			}
			if(ChaincodeFunctionAccessType.UPDATE.equals(funcType)) { //如果是更新操作，则需要最终提交更新的事务
				CompletableFuture<TransactionEvent> future = channel.sendTransaction(successResponses); //最终提交事务
				if(!asyncUpdate) {
					future.get(); //同步操作
				}
			}
			ProposalResponse successResponse = successResponses.iterator().next();
			LOGGER.info("【chaincode】>>> Successfully received transaction proposal response：{}", successResponse);
			byte[] dataBytes = successResponse.getChaincodeActionResponsePayload(); // chaincode返回结果
			String dataString = new String(dataBytes, configuration.getDefaultCharset());
			return Result.success().data(dataString).build();
		}
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
		Channel channel = hfClient.newChannel(chaincode.getChannelName());
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
		
		if(configuration.isRegisterEvent()) { //注册事件
			if(chaincodeEventListener != null) {
				channel.registerBlockListener(new DefaultBlockListener());
				//channel.registerChaincodeEventListener(Pattern.compile(".*"), Pattern.compile(Pattern.quote("event")), chaincodeEventListener);
			}
		}
		return channel;
	}
	
	protected Channel getChannel() {
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

	protected void setChannel(Channel channel) {
		this.channel = channel;
	}

	protected HFClient getHfClient() {
		return hfClient;
	}

	protected void setHfClient(HFClient hfClient) {
		this.hfClient = hfClient;
	}

	public FabricConfiguration getConfiguration() {
		return configuration;
	}

	public FabricChaincode getChaincode() {
		return chaincode;
	}

	public FabricOrganization getOrganization() {
		return organization;
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
