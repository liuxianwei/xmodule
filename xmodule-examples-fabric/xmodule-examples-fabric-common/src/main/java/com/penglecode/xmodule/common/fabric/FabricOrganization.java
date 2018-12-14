package com.penglecode.xmodule.common.fabric;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.fabric.util.FabricUtils;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * Fabric联盟组织
 * 
 * @author 	pengpeng
 * @date	2018年11月8日 下午4:44:18
 */
public class FabricOrganization implements FabricCaService, InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(FabricOrganization.class);
	
	public static final String DEFAULT_ADMIN_NAME = "admin";
	
	public static final String DEFAULT_ADMIN_SECRET = "adminpw";
	
	/**
	 * 组织名称
	 */
	private String orgName;
	
	/**
	 * 组织MSPID
	 */
	private String orgMspId;
	
	/**
	 * 组织域名名称
	 */
	private String orgDomainName;
	
	/**
	 * CA名称
	 */
	private String caName;
	
	/**
	 * CA地址
	 */
	private String caUrl;
	
	/**
	 * CA客户端
	 */
	private HFCAClient caClient;

	/**
	 * 节点集合
	 */
	private Map<String,String> peers = new LinkedHashMap<String, String>();
	
	/**
	 * 排序服务集合
	 */
	private Map<String,String> orderers = new LinkedHashMap<String, String>();
	
	/**
	 * 事件集合
	 */
	private Map<String,String> eventHubs = new LinkedHashMap<String, String>();
	
	/**
	 * 联盟管理员用户名
	 */
	private String adminName;
	
	/**
	 * 联盟管理员密码
	 */
	private String adminSecret;
	
	/**
	 * 联盟管理员用户
	 */
	private FabricUser admin;
	
	/**
	 * 联盟单节点管理员用户名
	 */
	private String peerAdminName;
	
	/**
	 * 联盟单节点管理员用户
	 */
	private FabricUser peerAdmin;

	private FabricConfiguration configuration;
	
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgMspId() {
		return orgMspId;
	}

	public void setOrgMspId(String orgMspId) {
		this.orgMspId = orgMspId;
	}

	public String getOrgDomainName() {
		return orgDomainName;
	}

	public void setOrgDomainName(String orgDomainName) {
		this.orgDomainName = orgDomainName;
	}

	public String getCaName() {
		return caName;
	}

	public void setCaName(String caName) {
		this.caName = caName;
	}

	public String getCaUrl() {
		return caUrl;
	}

	public void setCaUrl(String caUrl) {
		this.caUrl = caUrl;
	}

	public HFCAClient getCaClient() {
		return caClient;
	}

	protected void setCaClient(HFCAClient caClient) {
		this.caClient = caClient;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminSecret() {
		return adminSecret;
	}

	public void setAdminSecret(String adminSecret) {
		this.adminSecret = adminSecret;
	}

	public FabricUser getAdmin() {
		return admin;
	}

	protected void setAdmin(FabricUser admin) {
		this.admin = admin;
	}

	public String getPeerAdminName() {
		return peerAdminName;
	}

	public void setPeerAdminName(String peerAdminName) {
		this.peerAdminName = peerAdminName;
	}

	public FabricUser getPeerAdmin() {
		return peerAdmin;
	}

	protected void setPeerAdmin(FabricUser peerAdmin) {
		this.peerAdmin = peerAdmin;
	}

	public FabricConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(FabricConfiguration configuration) {
		this.configuration = configuration;
	}

	public void addPeer(String peerName, String peerUrl) {
		peers.put(peerName, peerUrl);
	}
	
	public List<String> getPeerNames() {
		return new ArrayList<String>(peers.keySet());
	}
	
	public Map<String,String> getPeers() {
		return Collections.unmodifiableMap(peers);
	}
	
	public void addOrderer(String ordererName, String ordererUrl) {
		orderers.put(ordererName, ordererUrl);
	}
	
	public List<String> getOrdererNames() {
		return new ArrayList<String>(orderers.keySet());
	}
	
	public Map<String,String> getOrderers() {
		return Collections.unmodifiableMap(orderers);
	}
	
	public void addEventHub(String eventHubName, String eventHubUrl) {
		eventHubs.put(eventHubName, eventHubUrl);
	}
	
	public Map<String,String> getEventHubs() {
		return Collections.unmodifiableMap(eventHubs);
	}
	
	@Override
	public FabricUser enrollUser(FabricUser unenrolledUser) throws Exception {
		Enrollment enrollment = getCaClient().enroll(unenrolledUser.getName(), unenrolledUser.getSecret());
		LOGGER.info(">>> Enroll user successfully, enrollment = " + enrollment);
		LOGGER.info(">>> key = " + FabricUtils.toString(enrollment.getKey()));
		LOGGER.info(">>> cert = " + enrollment.getCert());
		unenrolledUser.setEnrollment(enrollment);
		return unenrolledUser;
	}

	protected void init() throws Exception {
		this.caUrl = configuration.httpTLSify(caUrl);
		this.caClient = FabricUtils.createHfCaClient(caName, caUrl, configuration.getCaProperties(orgDomainName));
		if(!StringUtils.isEmpty(adminName) && !StringUtils.isEmpty(adminSecret)) {
			FabricUser unenrolledUser = new FabricUser();
			unenrolledUser.setName(adminName);
			unenrolledUser.setSecret(adminSecret);
			unenrolledUser.setMspId(orgMspId);
			unenrolledUser.setAffiliation(orgName);
			this.admin = enrollUser(unenrolledUser);
		}
		File privateKeyFile = configuration.getPeerUserPrivateKeyFile(peerAdminName, orgDomainName);
		File certificateFile = configuration.getPeerUserCertificateFile(peerAdminName, orgDomainName);
		this.peerAdmin = FabricUtils.getUser(peerAdminName, orgName, orgMspId, privateKeyFile, certificateFile);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(orgName, "Property 'orgName' must be required!");
		Assert.hasText(orgMspId, "Property 'orgMspId' must be required!");
		Assert.hasText(orgDomainName, "Property 'orgDomainName' must be required!");
		Assert.isTrue(FabricUtils.isDomainName(orgDomainName), "Property 'orgDomainName' must be a domain name!");
		Assert.notNull(configuration, "Property 'configuration' must be required!");
		Assert.hasText(caUrl, "Property 'caUrl' must be required!");
		Assert.hasText(peerAdminName, "Property 'peerAdminName' must be required!");
		Assert.notEmpty(peers, "Property 'peers' can not be empty!");
		Assert.notEmpty(orderers, "Property 'orderers' can not be empty!");
		for(Map.Entry<String,String> entry : peers.entrySet()) {
			String peerName = entry.getKey();
			String peerUrl = entry.getValue();
			Assert.isTrue(FabricUtils.isDomainName(peerName), String.format("The peers's key peerName(%s) must be a domain name!", peerName));
			peerUrl = configuration.grpcTLSify(peerUrl);
			peers.put(peerName, peerUrl);
		}
		for(Map.Entry<String,String> entry : orderers.entrySet()) {
			String ordererName = entry.getKey();
			String ordererUrl = entry.getValue();
			Assert.isTrue(FabricUtils.isDomainName(ordererName), String.format("The orderers's key ordererName(%s) must be a domain name!", ordererName));
			ordererUrl = configuration.grpcTLSify(ordererUrl);
			orderers.put(ordererName, ordererUrl);
		}
		if(!CollectionUtils.isEmpty(eventHubs)) {
			for(Map.Entry<String,String> entry : eventHubs.entrySet()) {
				String eventHubName = entry.getKey();
				String eventHubUrl = entry.getValue();
				Assert.isTrue(peers.containsKey(eventHubName), String.format("The eventHubs's key eventHubName(%s) must be same as one of peers's key!", eventHubName));
				eventHubUrl = configuration.grpcTLSify(eventHubUrl);
				eventHubs.put(eventHubName, eventHubUrl);
			}
		}
		init();
	}

}
