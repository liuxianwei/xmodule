package com.penglecode.xmodule.common.web.security.oauth2;

/**
 * OAuth2客户端
 * 
 * @author 	pengpeng
 * @date	2019年2月19日 下午1:29:06
 */
public class OAuth2Client {
		
	/** 客户端ID */
	private String clientId;
	
	/** 客户端秘钥 */
    private String clientSecret;
    
    /** 重定向URL */
    private String redirectUrl = "http://www.baidu.com";

    /** accessToken有效期(秒) */
    private Integer accessTokenValiditySeconds = 60 * 60 * 2;
    
    /** refreshToken有效期(秒) */
    private Integer refreshTokenValiditySeconds = 60 * 60 * 24 * 7;
    
    /** 授权类型 */
    private String[] authorizedGrantTypes = {"refresh_token", "authorization_code"};
    
    /** 授权范围 */
    private String scope = "app";

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

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public Integer getAccessTokenValiditySeconds() {
		return accessTokenValiditySeconds;
	}

	public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}

	public Integer getRefreshTokenValiditySeconds() {
		return refreshTokenValiditySeconds;
	}

	public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}

	public String[] getAuthorizedGrantTypes() {
		return authorizedGrantTypes;
	}

	public void setAuthorizedGrantTypes(String[] authorizedGrantTypes) {
		this.authorizedGrantTypes = authorizedGrantTypes;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
}