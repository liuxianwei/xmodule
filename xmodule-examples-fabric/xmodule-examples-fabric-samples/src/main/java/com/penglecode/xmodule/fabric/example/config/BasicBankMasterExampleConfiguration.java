package com.penglecode.xmodule.fabric.example.config;

import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.fabric.ChaincodeService;
import com.penglecode.xmodule.common.fabric.DefaultChaincodeService;
import com.penglecode.xmodule.common.fabric.FabricChaincode;
import com.penglecode.xmodule.common.fabric.FabricChannel;
import com.penglecode.xmodule.common.fabric.FabricConfiguration;
import com.penglecode.xmodule.common.fabric.FabricOrganization;

@Configuration
@ConditionalOnProperty(prefix="example", name="running", havingValue=BasicBankMasterExampleConfiguration.EXAMPLE_APP_NAME)
public class BasicBankMasterExampleConfiguration extends AbstractSpringConfiguration {

	public static final String EXAMPLE_APP_NAME = "bankmaster_example";
	
	@Bean(name="fabricConfiguration")
	public FabricConfiguration fabricConfiguration() {
		FabricConfiguration configuration = new FabricConfiguration("classpath:fabric/bankmaster/**");
		configuration.setRegisterEvent(true);
		//configuration.setEnableFabricCATLS(true);
		return configuration;
	}
	
	@Bean(name="fabricOrganization")
	public FabricOrganization fabricOrganization() {
		FabricOrganization organization = new FabricOrganization();
		//organization.setAdminName("admin");
		//organization.setAdminSecret("adminpw");
		organization.setPeerAdminName("admin");
		organization.setCaUrl("http://172.16.96.15:7054");
		organization.setOrgName("org1");
		organization.setOrgMspId("Org1MSP");
		organization.setOrgDomainName("org1.example.com");
		organization.setConfiguration(fabricConfiguration());
		
		organization.addPeer("peer0.org1.example.com", "grpc://172.16.96.15:7051");
		
		organization.addEventHub("peer0.org1.example.com", "grpc://172.16.96.15:7051");
		
		organization.addOrderer("orderer.example.com", "grpc://172.16.96.15:7050");
		
		return organization;
	}
	
	@Bean(name="fabricChaincode")
	public FabricChaincode fabricChaincode() {
		return new FabricChaincode("bankmaster", "github.com/bankmaster/java", Type.JAVA, "1.0");
	}
	
	@Bean(name="fabricChannel")
	public FabricChannel fabricChannel() {
		return new FabricChannel(fabricConfiguration(), fabricOrganization(), "bankchannel");
	}
	
	@Bean(name="chaincodeService")
	public ChaincodeService chaincodeService() {
		return new DefaultChaincodeService(fabricChannel(), fabricChaincode());
	}
	
}
