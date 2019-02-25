package com.penglecode.xmodule.common.web.security.oauth2;

/**
 * OAuth2认证授权服务器配置
 * 
 * @author 	pengpeng
 * @date	2019年2月19日 下午2:37:40
 */
public class OAuth2ServerConfigProperties {

	private OAuth2TokenConfigProperties token;
	
	private OAuth2Client[] clients = {};
	
	public OAuth2TokenConfigProperties getToken() {
		return token;
	}

	public void setToken(OAuth2TokenConfigProperties token) {
		this.token = token;
	}

	public OAuth2Client[] getClients() {
		return clients;
	}

	public void setClients(OAuth2Client[] clients) {
		this.clients = clients;
	}
	
}
