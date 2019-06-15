package com.penglecode.xmodule.common.security.oauth2;

public class OAuth2TokenConfigProperties {

	private String storeType;
	
	/**
	 * 在client端配置中,如果storeType=memory，则checkUrl必须配置,否则忽略checkUrl，
	 * 客户端通过TokenStore来检查Token的有效性
	 */
	private String checkUrl;

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getCheckUrl() {
		return checkUrl;
	}

	public void setCheckUrl(String checkUrl) {
		this.checkUrl = checkUrl;
	}
	
}
