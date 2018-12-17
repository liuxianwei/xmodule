package com.penglecode.xmodule.common.fabric;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * 智能合约chaincode
 * 
 * @author 	pengpeng
 * @date	2018年11月8日 下午4:30:47
 */
public class FabricChaincode {

	/**
	 * 智能合约名称,例如：mycc
	 */
	private final String chaincodeName;
	
	/**
	 * 智能合约服务端的安装路径,例如：github.com/chaincode/chaincode_example02/go/
	 */
	private final String chaincodePath;
	
	/**
	 * 智能合约编写语言类型
	 */
	private final Type chaincodeLanguage;
	
	/**
	 * 智能合约版本号,例如：1.0
	 */
	private final String chaincodeVersion;
	
	/**
	 * Fabric智能合约ID
	 */
	private ChaincodeID chaincodeID;
	
	/**
	 * 提案等待时间
	 */
	private Integer proposalWaitTime = 60000;
	
	/**
	 * 智能合约服务端的安装路径,例如：sdkintegration/javacc/sample1
	 */
	private String chaincodeLocalPath;
	
	public FabricChaincode(String chaincodeName) {
		this(chaincodeName, null, null, null);
	}

	public FabricChaincode(String chaincodeName, String chaincodePath, Type chaincodeLanguage, String chaincodeVersion) {
		super();
		Assert.hasText(chaincodeName, "Property 'chaincodeName' must be required!");
		this.chaincodeName = chaincodeName;
		this.chaincodePath = chaincodePath;
		this.chaincodeLanguage = ObjectUtils.defaultIfNull(chaincodeLanguage, Type.GO_LANG);
		this.chaincodeVersion = StringUtils.defaultIfEmpty(chaincodeVersion);
		ChaincodeID.Builder chaincodeIDBuilder = ChaincodeID.newBuilder().setName(chaincodeName);
		if(!StringUtils.isEmpty(chaincodeVersion)) {
			chaincodeIDBuilder.setVersion(chaincodeVersion);
		}
		if(!StringUtils.isEmpty(chaincodePath)) {
			chaincodeIDBuilder.setPath(chaincodePath);
		}
		this.chaincodeID = chaincodeIDBuilder.build();
	}

	public String getChaincodeName() {
		return chaincodeName;
	}

	public String getChaincodePath() {
		return chaincodePath;
	}

	public ChaincodeID getChaincodeID() {
		return chaincodeID;
	}

	protected void setChaincodeID(ChaincodeID chaincodeID) {
		this.chaincodeID = chaincodeID;
	}

	public Type getChaincodeLanguage() {
		return chaincodeLanguage;
	}

	public String getChaincodeVersion() {
		return chaincodeVersion;
	}

	public Integer getProposalWaitTime() {
		return proposalWaitTime;
	}

	public void setProposalWaitTime(Integer proposalWaitTime) {
		this.proposalWaitTime = proposalWaitTime;
	}

}