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
@ConditionalOnProperty(prefix="example", name="running", havingValue=FabcarExampleConfiguration.EXAMPLE_APP_NAME)
public class FabcarExampleConfiguration extends AbstractSpringConfiguration {

	public static final String EXAMPLE_APP_NAME = "fabcar_example";
	
	@Bean(name="fabricConfiguration")
	public FabricConfiguration fabricConfiguration() {
		FabricConfiguration configuration = new FabricConfiguration("classpath:fabric/fabcar/**");
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
		
		organization.addEventHub("peer0.org1.example.com", "grpc://172.16.96.15:7053");
		
		organization.addOrderer("orderer.example.com", "grpc://172.16.96.15:7050");
		
		return organization;
	}
	
	@Bean(name="fabricChaincode")
	public FabricChaincode fabricChaincode() {
		return new FabricChaincode("mychannel", "fabcar", "github.com/fabcar/go", Type.GO_LANG, null);
	}
	
	@Bean(name="chaincodeService")
	public ChaincodeService chaincodeService() {
		return new DefaultChaincodeService(fabricConfiguration(), fabricOrganization(), fabricChaincode());
	}
	
}
