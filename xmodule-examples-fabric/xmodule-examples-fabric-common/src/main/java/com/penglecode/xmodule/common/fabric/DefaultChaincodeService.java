package com.penglecode.xmodule.common.fabric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ChaincodeEventListener;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.SDKUtils;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.CollectionUtils;

/**
 * 默认的智能合约chaincode服务
 * 
 * @author 	pengpeng
 * @date	2018年11月10日 下午1:31:13
 */
public class DefaultChaincodeService implements ChaincodeService, InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultChaincodeService.class);
	
	/**
	 * fabric网络
	 */
	private final FabricChannel fabricChannel;
	
	/**
	 * fabric智能合约
	 */
	private final FabricChaincode fabricChaincode;
	
	/**
	 * 智能合约chaincode事件监听器
	 */
	private ChaincodeEventListener chaincodeEventListener;
	
	public DefaultChaincodeService(FabricChannel fabricChannel, FabricChaincode fabricChaincode) {
		super();
		Assert.notNull(fabricChaincode, "Parameter 'fabricChaincode' must be required!");
		Assert.notNull(fabricChannel, "Parameter 'fabricChannel' must be required!");
		this.fabricChannel = fabricChannel;
		this.fabricChaincode = fabricChaincode;
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
		LOGGER.info("【chaincode】>>> Prepare to invoke chaincode({}) now, fcn = {}, args = {} ", fabricChaincode.getChaincodeName(), fcn, Arrays.toString(args));
		Channel channel = fabricChannel.getChannel();
		TransactionProposalRequest transactionProposalRequest = fabricChannel.getHfClient().newTransactionProposalRequest();
		transactionProposalRequest.setChaincodeID(fabricChaincode.getChaincodeID());
		transactionProposalRequest.setChaincodeLanguage(fabricChaincode.getChaincodeLanguage());
		transactionProposalRequest.setFcn(fcn);
		if(args != null) {
			transactionProposalRequest.setArgs(args);
		}
		if(!CollectionUtils.isEmpty(transientData)) {
			transactionProposalRequest.setTransientMap(transientData);
		}
		transactionProposalRequest.setProposalWaitTime(fabricChaincode.getProposalWaitTime());
		
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
			String dataString = new String(dataBytes, fabricChannel.getConfiguration().getDefaultCharset());
			return Result.success().data(dataString).build();
		}
	}


	protected void init() throws Exception {
		if(fabricChannel.getConfiguration().isRegisterEvent()) {
			if(chaincodeEventListener != null) {
				//fabricChannel.getChannel().registerChaincodeEventListener(Pattern.compile(".*"), Pattern.compile(Pattern.quote("event")), chaincodeEventListener);
			}
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

}
