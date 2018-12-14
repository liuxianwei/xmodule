package com.penglecode.xmodule.common.fabric;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.hyperledger.fabric.sdk.helper.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.fabric.exception.FabricRuntimeException;
import com.penglecode.xmodule.common.fabric.util.FabricUtils;
import com.penglecode.xmodule.common.util.FileUtils;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * Fabric基础配置
 * 
 * @author 	pengpeng
 * @date	2018年11月8日 下午5:24:46
 */
public class FabricConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(FabricConfiguration.class);
	
	/**
	 * 默认的Fabric SDK配置文件所在位置
	 */
	private static final String DEFAULT_FABRIC_SDK_CONFIG_LOCATION = "classpath:fabric/sdk-config.properties";
	
	/**
	 * 默认的crypto-config目录名
	 */
	private static final String DEFAULT_CRYPTO_CONFIG_DIRECTORY_NAME = "crypto-config";
	
	/**
	 * 默认的crypto-config所在路径
	 */
	private static final String DEFAULT_FABRIC_CONFIG_ROOT_PATH = "classpath:fabric/**";
	
	/**
	 * CA客户端配置
	 */
	private final Map<String,Properties> allCAProperties = new HashMap<String,Properties>();
	
	/**
	 * 所需配置文件所在根路径路径
	 */
	private final String fabricConfigRootPath;
	
	/**
	 * crypto-config所在路径
	 */
	private final String cryptoConfigPath;
	
	/**
	 * Fabric SDK配置文件所在位置
	 */
	private final String fabricSdkConfigLocation;
	
	/**
	 * Peer/Orderer启用TLS(grpcs)?
	 */
	private boolean enableTLS;
	
	/**
	 * CA启用TLS(https)?
	 */
	private boolean enableCATLS;
	
	/**
	 * peer注册事件?
	 */
	private boolean registerEvent;
	
	/**
	 * 运行在测试环境下?
	 */
	private boolean runInTestEnvironment = true;
	
	/**
	 * 默认的字符集
	 */
	private String defaultCharset = GlobalConstants.DEFAULT_CHARSET;
	
	/**
	 * named peer的配置属性
	 */
	private final Map<String,Properties> allPeerProperties = new HashMap<String,Properties>();
	
	/**
	 * named orderer的配置属性
	 */
	private final Map<String,Properties> allOrdererProperties = new HashMap<String,Properties>();
	
	public FabricConfiguration() {
		this(DEFAULT_FABRIC_CONFIG_ROOT_PATH);
	}

	public FabricConfiguration(String fabricConfigRootPath) {
		this(fabricConfigRootPath, DEFAULT_FABRIC_SDK_CONFIG_LOCATION);
	}
	
	public FabricConfiguration(String fabricConfigRootPath, String fabricSdkConfigLocation) {
		super();
		this.fabricConfigRootPath = StringUtils.defaultIfEmpty(fabricConfigRootPath, DEFAULT_FABRIC_CONFIG_ROOT_PATH);
		this.fabricSdkConfigLocation = StringUtils.defaultIfEmpty(fabricSdkConfigLocation, DEFAULT_FABRIC_SDK_CONFIG_LOCATION);
		System.setProperty(Config.ORG_HYPERLEDGER_FABRIC_SDK_CONFIGURATION, this.fabricSdkConfigLocation);
		try {
			Resource[] resources = ApplicationConstants.RESOURCE_PATTERN_RESOLVER.getResources(fabricConfigRootPath);
			Assert.notEmpty(resources, String.format("The 'fabricConfigRootPath' (%s) is not exists or is empty no file found!", fabricConfigRootPath));
			String path = resources[0].getFile().getPath();
			this.cryptoConfigPath = FileUtils.formatFilePath(path.substring(0, path.indexOf(DEFAULT_CRYPTO_CONFIG_DIRECTORY_NAME)) + DEFAULT_CRYPTO_CONFIG_DIRECTORY_NAME);
		} catch (Exception e) {
			LOGGER.error(">>> 初始化Fabric配置失败！错误信息：{}", e.getMessage());
			throw new FabricRuntimeException(e);
		}
	}

	/**
	 * 获取CA的配置
	 * @param orgDomainName
	 * @return
	 */
	public Properties getCaProperties(String orgDomainName) {
		Properties caProperties = allCAProperties.get(orgDomainName);
		if(caProperties == null) {
			synchronized (allCAProperties) {
				caProperties = allCAProperties.get(orgDomainName);
				if(caProperties == null) {
					caProperties = doGetCaProperties(orgDomainName);
					allCAProperties.put(orgDomainName, caProperties);
				}
			}
		}
		return caProperties;
	}
	
	protected Properties doGetCaProperties(String orgDomainName) {
		Properties properties = new Properties();
		if(enableCATLS) {
			Assert.hasText(orgDomainName, "Parameter 'orgDomainName' can not be empty!");
			File caCert = Paths.get(cryptoConfigPath, "/peerOrganizations/", orgDomainName, String.format("/ca/ca.%s-cert.pem", orgDomainName)).toFile();
			Assert.isTrue(caCert.exists() && caCert.isFile(), String.format("No CA cert file found for '%s' with location : %s", orgDomainName, caCert.getAbsolutePath()));
			properties.setProperty("pemFile", caCert.getAbsolutePath());
			if(runInTestEnvironment) {
				properties.setProperty("allowAllHostNames", "true");
			}
		}
		return properties;
	}
	
	/**
	 * 获取节点的配置
	 * @param peerName
	 * @return
	 */
	public Properties getPeerProperties(String peerName) {
		Properties peerProperties = allPeerProperties.get(peerName);
		if(peerProperties == null) {
			synchronized (allPeerProperties) {
				peerProperties = allPeerProperties.get(peerName);
				if(peerProperties == null) {
					peerProperties = doGetPeerProperties(peerName);
					allPeerProperties.put(peerName, peerProperties);
				}
			}
		}
		return peerProperties;
    }
	
	protected Properties doGetPeerProperties(String peerName) {
        Properties properties = new Properties();
        String orgDomainName = FabricUtils.getDomainName(peerName);

        File peerCertFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", orgDomainName, "peers", peerName, "tls/server.crt").toFile();
        Assert.isTrue(peerCertFile.exists() && peerCertFile.isFile(), String.format("No peer cert file found for '%s' with location : %s", peerName, peerCertFile.getAbsolutePath()));

        if(enableTLS) {
        	File clientCertFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", orgDomainName, "users/Admin@" + orgDomainName, "tls/client.crt").toFile();
            Assert.isTrue(clientCertFile.exists() && clientCertFile.isFile(), String.format("No peer TLS client cert file found for '%s' with location : %s", peerName, clientCertFile.getAbsolutePath()));
            
            File clientKeyFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", orgDomainName, "users/Admin@" + orgDomainName, "tls/client.key").toFile();
            Assert.isTrue(clientKeyFile.exists() && clientKeyFile.isFile(), String.format("No peer TLS client key file found for '%s' with location : %s", peerName, clientKeyFile.getAbsolutePath()));
            
            properties.setProperty("clientCertFile", clientCertFile.getAbsolutePath());
            properties.setProperty("clientKeyFile", clientKeyFile.getAbsolutePath());
        }
        
        properties.setProperty("pemFile", peerCertFile.getAbsolutePath());
        if(runInTestEnvironment) {
        	properties.setProperty("trustServerCertificate", "true");
        }
        
        properties.setProperty("hostnameOverride", peerName);
        properties.setProperty("sslProvider", "openSSL");
        properties.setProperty("negotiationType", "TLS");
        properties.setProperty("ordererWaitTimeMilliSecs", "300000");
        properties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", 9000000);
        return properties;
    }
	
	/**
	 * 获取排序服务的配置
	 * @param ordererName
	 * @return
	 */
	public Properties getOrdererProperties(String ordererName) {
		Properties ordererProperties = allOrdererProperties.get(ordererName);
		if(ordererProperties == null) {
			synchronized (allOrdererProperties) {
				ordererProperties = allOrdererProperties.get(ordererName);
				if(ordererProperties == null) {
					ordererProperties = doGetOrdererProperties(ordererName);
					allOrdererProperties.put(ordererName, ordererProperties);
				}
			}
		}
		return ordererProperties;
    }
	
	protected Properties doGetOrdererProperties(String ordererName) {
        Properties properties = new Properties();
        String ordererDomainName = FabricUtils.getDomainName(ordererName);

        File ordererCertFile = Paths.get(cryptoConfigPath, "/ordererOrganizations/", ordererDomainName, "orderers", ordererName, "tls/server.crt").toFile();
        Assert.isTrue(ordererCertFile.exists() && ordererCertFile.isFile(), String.format("No orderer cert file found for '%s' with location : %s", ordererName, ordererCertFile.getAbsolutePath()));

        if(enableTLS) {
        	File clientCertFile = Paths.get(cryptoConfigPath, "/ordererOrganizations/", ordererDomainName, "users/Admin@" + ordererDomainName, "tls/client.crt").toFile();
            Assert.isTrue(clientCertFile.exists() && clientCertFile.isFile(), String.format("No orderer TLS client cert file found for '%s' with location : %s", ordererName, clientCertFile.getAbsolutePath()));
            
            File clientKeyFile = Paths.get(cryptoConfigPath, "/ordererOrganizations/", ordererDomainName, "users/Admin@" + ordererDomainName, "tls/client.key").toFile();
            Assert.isTrue(clientKeyFile.exists() && clientKeyFile.isFile(), String.format("No orderer TLS client key file found for '%s' with location : %s", ordererName, clientKeyFile.getAbsolutePath()));
            
            properties.setProperty("clientCertFile", clientCertFile.getAbsolutePath());
            properties.setProperty("clientKeyFile", clientKeyFile.getAbsolutePath());
        }
        
        properties.setProperty("pemFile", ordererCertFile.getAbsolutePath());
        properties.setProperty("hostnameOverride", ordererName);
        properties.setProperty("sslProvider", "openSSL");
        properties.setProperty("negotiationType", "TLS");
        properties.setProperty("ordererWaitTimeMilliSecs", "300000");
        properties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", 9000000);
        properties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[] {5L, TimeUnit.MINUTES});
        properties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[] {10L, TimeUnit.SECONDS});
        properties.put("grpc.NettyChannelBuilderOption.keepAliveWithoutCalls", new Object[] {true});
        return properties;
    }
	
	/**
	 * 获取节点事件配置
	 * @param peerName
	 * @return
	 */
	public Properties getEventHubProperties(String peerName) {
		return getPeerProperties(peerName);
	}
	
	/**
	 * 根据用户名,组织域名从/peerOrganizations/下获取privatekey文件
	 * @param userName
	 * @param orgDomainName
	 * @return
	 */
	public File getPeerUserPrivateKeyFile(String userName, String orgDomainName) {
		File directory = Paths.get(cryptoConfigPath, "/peerOrganizations/", orgDomainName, String.format("/users/%s@%s/msp/keystore", userName, orgDomainName)).toFile();
		File[] matchedFiles = directory.listFiles((dir, name) -> name.endsWith("_sk"));
		Assert.notEmpty(matchedFiles, String.format("No sk file found under path: %s", directory));
		Assert.isTrue(matchedFiles.length == 1, String.format("Expected only 1 sk file but found %d under path: %s", matchedFiles.length, directory));
		return matchedFiles[0];
	}
	
	/**
	 * 根据用户名,组织域名从/peerOrganizations/下获取certificate文件
	 * @param userName
	 * @param orgDomainName
	 * @return
	 */
	public File getPeerUserCertificateFile(String userName, String orgDomainName) {
		return Paths.get(cryptoConfigPath, "/peerOrganizations/", orgDomainName, String.format("/users/%s@%s/msp/signcerts/%s@%s-cert.pem", userName, orgDomainName, userName, orgDomainName)).toFile();
	}
	
	public String grpcTLSify(String grpcUrl) {
		grpcUrl = grpcUrl.trim();
		try {
			new URI(grpcUrl);
		} catch (URISyntaxException e) {
			Assert.isTrue(false, String.format("Invalid protocol expected grpc or grpcs and found %s", grpcUrl));
		}
        return enableTLS ? grpcUrl.replaceFirst("^grpc://", "grpcs://") : grpcUrl;
    }
	
	public String httpTLSify(String httpUrl) {
		httpUrl = httpUrl.trim();
		try {
			new URI(httpUrl);
		} catch (URISyntaxException e) {
			Assert.isTrue(false, String.format("Invalid protocol expected http or https and found %s", httpUrl));
		}
        return enableCATLS ? httpUrl.replaceFirst("^http://", "https://") : httpUrl;
    }
	
	public String getFabricSdkConfigLocation() {
		return fabricSdkConfigLocation;
	}

	public boolean isEnableTLS() {
		return enableTLS;
	}

	public void setEnableTLS(boolean enableTLS) {
		this.enableTLS = enableTLS;
	}

	public boolean isEnableCATLS() {
		return enableCATLS;
	}

	public void setEnableCATLS(boolean enableCATLS) {
		this.enableCATLS = enableCATLS;
	}

	public boolean isRegisterEvent() {
		return registerEvent;
	}

	public void setRegisterEvent(boolean registerEvent) {
		this.registerEvent = registerEvent;
	}

	public String getDefaultCharset() {
		return defaultCharset;
	}

	public void setDefaultCharset(String defaultCharset) {
		this.defaultCharset = defaultCharset;
	}

	public boolean isRunInTestEnvironment() {
		return runInTestEnvironment;
	}

	public void setRunInTestEnvironment(boolean runInTestEnvironment) {
		this.runInTestEnvironment = runInTestEnvironment;
	}

	protected Map<String, Properties> getAllCAProperties() {
		return allCAProperties;
	}

	public String getFabricConfigRootPath() {
		return fabricConfigRootPath;
	}

	public String getCryptoConfigPath() {
		return cryptoConfigPath;
	}

	protected Map<String, Properties> getAllPeerProperties() {
		return allPeerProperties;
	}

	protected Map<String, Properties> getAllOrdererProperties() {
		return allOrdererProperties;
	}
	
}
