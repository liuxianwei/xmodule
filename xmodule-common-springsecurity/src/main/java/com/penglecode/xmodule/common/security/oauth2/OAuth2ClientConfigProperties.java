package com.penglecode.xmodule.common.security.oauth2;

/**
 * OAuth2客户端(资源服务)配置
 * 
 * @author 	pengpeng
 * @date	2019年2月19日 下午2:43:18
 */
public class OAuth2ClientConfigProperties {

	private OAuth2TokenConfigProperties token;
	
	/** 客户端ID */
	private String clientId;
	
	/** 客户端秘钥 */
    private String clientSecret;
    
    /** 客户端访问的资源ID */
    private String resourceId;
    
	public OAuth2TokenConfigProperties getToken() {
		return token;
	}

	public void setToken(OAuth2TokenConfigProperties token) {
		this.token = token;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

}
