package com.penglecode.xmodule.fabric.example.config;

import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.fabric.ChaincodeService;
import com.penglecode.xmodule.common.fabric.DefaultChaincodeService;
import com.penglecode.xmodule.common.fabric.FabricChaincode;
import com.penglecode.xmodule.common.fabric.FabricConfiguration;
import com.penglecode.xmodule.common.fabric.FabricOrganization;

@Configuration
@ConditionalOnProperty(prefix="example", name="running", havingValue=E2eExample2Configuration.EXAMPLE_APP_NAME)
public class E2eExample2Configuration extends AbstractSpringConfiguration {

	public static final String EXAMPLE_APP_NAME = "e2e_example2";
	
	@Bean(name="fabricConfiguration")
	public FabricConfiguration fabricConfiguration() {
		FabricConfiguration configuration = new FabricConfiguration("classpath:fabric/e2e/**");
		configuration.setEnableCATLS(true);
		configuration.setEnableTLS(true);
		return configuration;
	}
	
	@Bean(name="fabricOrganization")
	public FabricOrganization fabricOrganization() {
		FabricOrganization organization = new FabricOrganization();
		//organization.setAdminName("admin");
		//organization.setAdminSecret("adminpw");
		organization.setPeerAdminName("admin");
		organization.setCaUrl("https://172.16.18.247:7054");
		organization.setOrgName("org2");
		organization.setOrgMspId("Org2MSP");
		organization.setOrgDomainName("org2.example.com");
		organization.setConfiguration(fabricConfiguration());
		
		organization.addPeer("peer0.org1.example.com", "grpc://172.16.18.247:7051");
		organization.addPeer("peer0.org2.example.com", "grpc://172.16.18.247:9051");
		
		organization.addEventHub("peer0.org1.example.com", "grpc://172.16.18.247:7053");
		organization.addEventHub("peer0.org2.example.com", "grpc://172.16.18.247:9053");
		
		organization.addOrderer("orderer.example.com", "grpc://172.16.18.247:7050");
		
		return organization;
	}
	
	@Bean(name="fabricChaincode")
	public FabricChaincode fabricChaincode() {
		//参数值来自/fabric/examples/e2e_cli/scripts/script.sh#installChaincode()
		return new FabricChaincode("mychannel", "mycc", null, Type.GO_LANG, null);
	}
	
	@Bean(name="chaincodeService")
	public ChaincodeService chaincodeService() {
		return new DefaultChaincodeService(fabricConfiguration(), fabricOrganization(), fabricChaincode());
	}
	
}
